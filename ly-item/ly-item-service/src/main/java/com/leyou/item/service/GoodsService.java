package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/25 0025 - 下午 16:26
 */

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper detailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+ key +"%");
        }

        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }

        example.setOrderByClause("last_update_time DESC");

        List<Spu> list = spuMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }

        loadCategoryAndBrandName(list);

        PageInfo<Spu> info = new PageInfo<>(list);

        return new PageResult<>(info.getTotal(),list);
    }

    private void loadCategoryAndBrandName(List<Spu> list) {
        for (Spu spu : list) {
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());

            spu.setCname(StringUtils.join(names,"/"));

            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

        }
    }

    @Transactional
    public void saveGoods(Spu spu) {
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);

        int count = spuMapper.insert(spu);
        if(count != 1){
            throw new LyException(ExceptionEnums.GOODS_SAVE_TYPE);
        }

        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        detailMapper.insert(detail);

        saveSkuAndStock(spu);
    }

    private void saveSkuAndStock(Spu spu) {
        int count;List<Stock> stockList = new ArrayList<>();

        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            count = skuMapper.insert(sku);
            if(count != 1){
                throw new LyException(ExceptionEnums.GOODS_SAVE_TYPE);
            }

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
        }

        count = stockMapper.insertList(stockList);

        if(count!=stockList.size()){
            throw new LyException(ExceptionEnums.GOODS_SAVE_TYPE);
        }
    }

    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail detail = detailMapper.selectByPrimaryKey(spuId);
        if(detail==null){
            throw new LyException(ExceptionEnums.GOODS_DETAIL_NOT_FOND);
        }
        return detail;
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(ExceptionEnums.GOODS_SKU_NOT_FOND);
        }

        /*for (Sku s : skuList) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock == null){
                throw new LyException(ExceptionEnums.GOODS_STOCK_NOT_FOND);
            }

            s.setStock(stock.getStock());
        }*/

        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(ids);

        if(CollectionUtils.isEmpty(stockList)){
            throw new LyException(ExceptionEnums.GOODS_STOCK_NOT_FOND);
        }

        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return skuList;
    }


    @Transactional
    public void updateGoods(Spu spu) {
        if(spu.getId()==null){
            throw new LyException(ExceptionEnums.GOODS_ID_CANNOT_BE_NULL);
//            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());

        List<Sku> skuList = skuMapper.select(sku);
        if(!CollectionUtils.isEmpty(skuList)){
            skuMapper.delete(sku);
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }

        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);

        int count = spuMapper.updateByPrimaryKeySelective(spu);

        if(count!=1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }

        count = detailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count!=1){
            throw new LyException(ExceptionEnums.GOODS_UPDATE_ERROR);
        }

        saveSkuAndStock(spu);
    }

    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu==null){
            throw new LyException(ExceptionEnums.GOODS_NOT_FOND);
        }
        //查询sku
        spu.setSkus(querySkuBySpuId(id));

        //查询detail
        spu.setSpuDetail(queryDetailById(id));
        return spu;
    }
}
