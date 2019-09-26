package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/22 0022 - 下午 16:21
 */

@Data
@Table(name = "tb_brand")
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private String image;
    private Character letter;
}
