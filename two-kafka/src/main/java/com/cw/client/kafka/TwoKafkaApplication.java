package com.cw.client.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @author joseph
 */
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class TwoKafkaApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(TwoKafkaApplication.class);
    }
}
