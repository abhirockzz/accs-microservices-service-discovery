package com.oracle.cloud.accs.product;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("InventoryService")
public interface InventoryClient {

    @RequestMapping(value = "/inventory/{item}", method = RequestMethod.GET, produces = {"application/json"} )
    @ResponseBody
    public InventoryInfo getInventory(@PathVariable("item") String item);
    
}
