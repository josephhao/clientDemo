package com.joseph.study.zk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration;

@SpringBootApplication(exclude = ZookeeperAutoConfiguration.class)
public class ZkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkApplication.class, args);
    }

}
