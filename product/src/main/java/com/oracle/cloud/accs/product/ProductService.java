package com.oracle.cloud.accs.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
@Configuration
public class ProductService {

    public static void main(String[] args) {
        SpringApplication.run(ProductService.class, args);
    }
    
    @Autowired
    private InventoryClient iClient;
    
    @RequestMapping(method = RequestMethod.GET, value = "/product/{name}", produces = "application/json")
    @ResponseBody
    public ProductInfo info(@PathVariable("name") String name) {
        
        System.out.println("Looking for product "+ name); // /product/iPhoneX
        
        InventoryInfo inventory = iClient.getInventory(name);
        return new ProductInfo(name, inventory);
    }
}