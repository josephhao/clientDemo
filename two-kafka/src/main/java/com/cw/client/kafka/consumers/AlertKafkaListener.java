package com.cw.client.kafka.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;

import java.util.List;

/**
 * @author joseph
 */
@Slf4j
@Component
public class AlertKafkaListener {


    @KafkaListener(id="alert", groupId = "LocalIp", topics={"kafka.alertconsumer.topicNames"}, containerFactory ="kafkaListenerContainerFactoryFacecenter" )
    public void listen8(List<ConsumerRecord<String, byte[]>> consumerRecords)  {

        int count = 0;
        if(consumerRecords.size()>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                for (ConsumerRecord<String, byte[]> record : consumerRecords) {
                    count++;
                    //读取数据key value 反序列化
                    //引入protoBuf 进行数据反序列化，转成抓拍对象传出
                    if (record.key() == null && record.value() == null) {
                        continue;
                    } else {
                        log.info("alert topic record: " + new String(record.value()));
                    }
                }
            } finally {
            }
        }

    }







}
