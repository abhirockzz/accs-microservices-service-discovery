package com.oracle.cloud.accs.inventory;

import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.xbill.DNS.Address;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class InventoryApplication {

    static Random GEN = new Random();
    static final String HOSTNAME = System.getenv().getOrDefault("ORA_APP_NAME", "localhost");
    static final String INSTANCE_ID = HOSTNAME + ":" + System.getenv().getOrDefault("APAAS_CONTAINER_NAME", UUID.randomUUID().toString());

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
    
    @Bean
    public ZookeeperDiscoveryProperties zkConfigBean(InetUtils utils) {
        System.out.println("Configuring Eureka client");

        ZookeeperDiscoveryProperties config = new ZookeeperDiscoveryProperties(utils);
       
        String ip = ip();
        config.setInstanceIpAddress(ip);
        System.out.println("Set IP to " + ip);
        
        config.setInstancePort(8080); //all ACCS app instances will be reachable on this static port
        System.out.println("Set port to 8080");
        
        config.setPreferIpAddress(true);
        System.out.println("Prefer IP address");
        
        config.setInstanceId(INSTANCE_ID);
        System.out.println("HOST INSTANCE_ID -- " + INSTANCE_ID);
        
        return config;
    }

   

    private static String ip() {
        String hostIP = null;
        System.out.println("Finding host IP");
        try {

            System.out.println("Searching address for hostname " + HOSTNAME);
            InetAddress inetAddress = Address.getByName(HOSTNAME);
            hostIP = inetAddress.getHostAddress();

        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Error occurred while trying to find Host IP "+ ex.getMessage());
            hostIP = "127.0.0.1";
        }

        System.out.println("Host IP -- " + hostIP);
        return hostIP;
    }

    @RequestMapping(value = "/inventory/{item}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public InventoryInfo getInventory(@PathVariable("item") String item) {
        System.out.println("Fetching inventory for item " + item);
        return new InventoryInfo(GEN.nextInt(10) + 1);
    }
}
