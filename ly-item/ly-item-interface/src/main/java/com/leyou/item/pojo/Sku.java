package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/26 0026 - 下午 12:51
 */
@Data
@Table(name = "tb_sku")
public class Sku {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;
    private String indexes;
    private Boolean enable;
    private Date createTime;
    private Date lastUpdateTime;

    @Transient
    private Integer stock;
}
