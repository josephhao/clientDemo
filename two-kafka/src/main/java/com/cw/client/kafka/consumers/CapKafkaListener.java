package com.cw.client.kafka.consumers;



import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * @author joseph
 */
@Slf4j
@Component
public class CapKafkaListener {

    @Autowired
    @Qualifier("kafkaTemplate")
    KafkaTemplate template;

    @KafkaListener(id="phone", groupId = "phone_semi", topics={"phone.kafka-cap-topic-name"}, containerFactory ="kafkaListenerContainerFactory")
    public void listen8(List<ConsumerRecord<String, byte[]>> records)  {

        if (records != null && !records.isEmpty()) {

            try {
                for (ConsumerRecord<String, byte[]> record : records) {
                    if (record.value() != null) {
                        log.info("topic phone record: " + new String(record.value()));
                    }
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
//                acknowledgment.acknowledge();
            }

        }
    }

}
