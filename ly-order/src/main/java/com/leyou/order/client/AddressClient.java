package com.leyou.order.client;

import com.leyou.order.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 上午 9:52
 */
public abstract class AddressClient {
    public static final List<AddressDTO> addressList = new ArrayList<AddressDTO>(){
        {
            AddressDTO address = new AddressDTO();
            address.setId(1L);
            address.setAddress("盘龙城领袖城 丙X栋X单元XXX");
            address.setCity("武汉");
            address.setDistrict("黄陂区");
            address.setName("故梦良缘丶");
            address.setPhone("13995880443");
            address.setState("武汉");
            address.setZipCode("430000");
            address.setIsDefault(true);
            add(address);

            AddressDTO address2 = new AddressDTO();
            address2.setId(2L);
            address2.setAddress("盘龙城领袖城 丙X栋X单元XXX");
            address2.setCity("武汉");
            address2.setDistrict("黄陂区");
            address2.setName("GHOSTLaycoo");
            address2.setPhone("13554401527");
            address2.setState("武汉");
            address2.setZipCode("430000");
            address2.setIsDefault(false);
            add(address2);
        }
    };

    public static AddressDTO findById(Long id){
        for (AddressDTO addressDTO : addressList) {
            if(addressDTO.getId() == id){
                return addressDTO;
            }
        }
        return null;
    }
}
