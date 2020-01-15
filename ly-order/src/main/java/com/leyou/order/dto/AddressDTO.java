package com.leyou.order.dto;

import lombok.Data;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 上午 9:50
 */
@Data
public class AddressDTO {
    private Long id;
    private String name;
    private String phone;
    private String state;
    private String city;
    private String district;
    private String address;
    private String zipCode;
    private Boolean isDefault;



}
