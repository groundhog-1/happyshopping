package com.leyou.order.client;

import com.leyou.order.dto.AddressDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
public class AddressClient {
    public static final List<AddressDTO> addressList = new ArrayList<AddressDTO>(){
        {
            AddressDTO address1 = new AddressDTO();
            address1.setId(1L);

            add(address1);

            AddressDTO address2 = new AddressDTO();
            address2.setId(2L);
            add(address2);
        }
    };

    public static AddressDTO findById(Long id){
        for (AddressDTO addressDTO : addressList) {
            if (addressDTO.getId() == id) return addressDTO;
        }
        return null;
    }
}
