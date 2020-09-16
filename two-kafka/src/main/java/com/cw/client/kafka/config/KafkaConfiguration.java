package com.cw.client.kafka.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.convert.Delimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.BatchMessageConverter;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.MessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaAwareTransactionManager;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * for configuration two kafka clusters;
 * @author joseph
 */
@Slf4j
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Configuration
@EnableKafka
@Getter
public class KafkaConfiguration {

    private static KafkaConfiguration instance;

    @Getter
    @Delimiter(",")
    @Value("${kafka.alert.brokers}")
    private List<String> kafkaAlertBrokers;

    @PostConstruct
    public void init() {
        instance = this;
    }


    @Slf4j
    public static class KafkaPropertiesWrapper extends KafkaProperties {

        public KafkaPropertiesWrapper(){
        }

        public void init() {
            this.setBootstrapServers(instance.getKafkaAlertBrokers());
            log.info("facecenterKafkaProperties: " + JSONObject.toJSONString(this));
        }
    }

    /**
     * for local kafka
     * @return
     */
    @Bean(name = "kafkaProperties")
//    @Primary
    @ConfigurationProperties(prefix = "spring.kafka")
    public KafkaProperties kafkaProperties(){
        KafkaProperties kafkaProperties = new KafkaProperties();
        return kafkaProperties;
    }


    /**
     * for main server kafka
     * @return
     */
    @Bean(name = "facecenterKafkaProperties", initMethod = "init")
    public KafkaProperties facecenterKafkaProperties(@Qualifier("kafkaProperties")KafkaProperties kafkaProperties){
        log.info("kafkaProperties: " + JSONObject.toJSONString(kafkaProperties));
        KafkaProperties facecenterKafkaProperties = new KafkaPropertiesWrapper();
        BeanUtils.copyProperties(kafkaProperties,facecenterKafkaProperties);
        return facecenterKafkaProperties;
    }

    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<Object, Object> kafkaTemplate(
            @Qualifier("kafkaProperties") KafkaProperties kafkaProperties,
            @Qualifier("kafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
            @Qualifier("kafkaProducerListener") ProducerListener<Object, Object> kafkaProducerListener,
            ObjectProvider<RecordMessageConverter> messageConverter
    ) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory, kafkaProperties.buildProducerProperties());
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        return kafkaTemplate;
    }

    @Bean(name = "kafkaProducerFactory")
    public ProducerFactory<Object, Object> kafkaProducerFactory(
            @Qualifier("kafkaProperties") KafkaProperties kafkaProperties,
            ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers
    ) {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties());
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean(name = "kafkaProducerListener")
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    @Bean(name = "kafkaConsumerFactory")
    public ConsumerFactory<Object, Object> kafkaConsumerFactory(
            @Qualifier("kafkaProperties") KafkaProperties kafkaProperties,
            ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers            
    ) {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties());
                
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean(name = "kafkaListenerContainerFactory")
    //理解为默认优先选择当前容器下的消费者工厂
    ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            @Qualifier("kafkaProperties") KafkaProperties kafkaProperties,
            @Qualifier("kafkaConsumerFactory") ConsumerFactory<Object, Object> kafkaConsumerFactory,
            ObjectProvider<RecordMessageConverter> messageConverter,
            ObjectProvider<BatchMessageConverter> batchMessageConverter,
            ObjectProvider<KafkaTemplate<Object, Object>> kafkaTemplate,
            ObjectProvider<KafkaAwareTransactionManager<Object, Object>> kafkaTransactionManager,
            ObjectProvider<ConsumerAwareRebalanceListener> rebalanceListener,
            ObjectProvider<ErrorHandler> errorHandler,
            ObjectProvider<BatchErrorHandler> batchErrorHandler,
            ObjectProvider<AfterRollbackProcessor<Object, Object>> afterRollbackProcessor,
            ObjectProvider<RecordInterceptor<Object, Object>> recordInterceptor
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);

        initKafkaListenerContainerFactory(
                kafkaProperties,
                factory,
                messageConverter,
                batchMessageConverter,
                kafkaTemplate,
                kafkaTransactionManager,
                rebalanceListener,
                errorHandler,
                batchErrorHandler,
                afterRollbackProcessor,
                recordInterceptor
        );

        return factory;
    }

    /** second kafka configuration  section start */
    @Bean(name = "kafkaTemplateFacecenter")
    public KafkaTemplate<Object, Object> kafkaTemplateFacecenter(
            @Qualifier("facecenterKafkaProperties") KafkaProperties facecenterKafkaProperties,
            @Qualifier("kafkaProducerFactoryFacecenter") ProducerFactory<Object, Object> kafkaProducerFactory,
            @Qualifier("kafkaProducerListenerFacecenter") ProducerListener<Object, Object> kafkaProducerListener,
            ObjectProvider<RecordMessageConverter> messageConverter
    ) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory, facecenterKafkaProperties.buildProducerProperties());
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(facecenterKafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean(name = "kafkaProducerFactoryFacecenter")
    public ProducerFactory<Object, Object> kafkaProducerFactoryFacecenter(
            @Qualifier("facecenterKafkaProperties") KafkaProperties facecenterKafkaProperties,
            ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers
    ) {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
                facecenterKafkaProperties.buildProducerProperties());
        String transactionIdPrefix = facecenterKafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean(name = "kafkaProducerListenerFacecenter")
    public ProducerListener<Object, Object> kafkaProducerListenerFacecenter() {
        return new LoggingProducerListener<>();
    }

    @Bean(name = "kafkaConsumerFactoryFacecenter")
    public ConsumerFactory<Object, Object> kafkaConsumerFactoryFacecenter(
            @Qualifier("facecenterKafkaProperties") KafkaProperties facecenterKafkaProperties,
            ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers
    ) {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(
                facecenterKafkaProperties.buildConsumerProperties());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    //理解为默认优先选择当前容器下的消费者工厂
    @Bean(name = "kafkaListenerContainerFactoryFacecenter")
    ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactoryFacecenter(
            @Qualifier("facecenterKafkaProperties") KafkaProperties facecenterKafkaProperties,
            @Qualifier("kafkaConsumerFactoryFacecenter") ConsumerFactory<Object, Object> kafkaConsumerFactory,
            ObjectProvider<RecordMessageConverter> messageConverter,
            ObjectProvider<BatchMessageConverter> batchMessageConverter,
            ObjectProvider<KafkaTemplate<Object, Object>> kafkaTemplate,
            ObjectProvider<KafkaAwareTransactionManager<Object, Object>> kafkaTransactionManager,
            ObjectProvider<ConsumerAwareRebalanceListener> rebalanceListener,
            ObjectProvider<ErrorHandler> errorHandler,
            ObjectProvider<BatchErrorHandler> batchErrorHandler,
            ObjectProvider<AfterRollbackProcessor<Object, Object>> afterRollbackProcessor,
            ObjectProvider<RecordInterceptor<Object, Object>> recordInterceptor
    ) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);

        initKafkaListenerContainerFactory(
                facecenterKafkaProperties,
                factory,
                messageConverter,
                batchMessageConverter,
                kafkaTemplate,
                kafkaTransactionManager,
                rebalanceListener,
                errorHandler,
                batchErrorHandler,
                afterRollbackProcessor,
                recordInterceptor
        );
        return factory;
    }
    /** second kafka configuration  section end */



    /**
     * 之前未加 此方法， kafka
     *
     * @param kafkaProperties
     * @param factory
     * @param messageConverter
     * @param batchMessageConverter
     * @param kafkaTemplate
     * @param kafkaTransactionManager
     * @param rebalanceListener
     * @param errorHandler
     * @param batchErrorHandler
     * @param afterRollbackProcessor
     * @param recordInterceptor
     */
    private void initKafkaListenerContainerFactory(KafkaProperties kafkaProperties,
                                                   ConcurrentKafkaListenerContainerFactory<Object, Object> factory,
                                                   ObjectProvider<RecordMessageConverter> messageConverter,
                                                   ObjectProvider<BatchMessageConverter> batchMessageConverter,
                                                   ObjectProvider<KafkaTemplate<Object, Object>> kafkaTemplate,
                                                   ObjectProvider<KafkaAwareTransactionManager<Object, Object>> kafkaTransactionManager,
                                                   ObjectProvider<ConsumerAwareRebalanceListener> rebalanceListener,
                                                   ObjectProvider<ErrorHandler> errorHandler,
                                                   ObjectProvider<BatchErrorHandler> batchErrorHandler,
                                                   ObjectProvider<AfterRollbackProcessor<Object, Object>> afterRollbackProcessor,
                                                   ObjectProvider<RecordInterceptor<Object, Object>> recordInterceptor
    ) {

        MessageConverter batchMessageConverter0 = batchMessageConverter
                .getIfUnique(() -> new BatchMessagingMessageConverter(messageConverter.getIfUnique()));
        MessageConverter messageConverterToUse =
                (kafkaProperties.getListener().getType().equals(KafkaProperties.Listener.Type.BATCH))
                        ? batchMessageConverter0 : messageConverter.getIfUnique();

        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaProperties.Listener properties = kafkaProperties.getListener();
        map.from(properties::getConcurrency).to(factory::setConcurrency);
        map.from(messageConverterToUse).to(factory::setMessageConverter);
        map.from(kafkaTemplate.getIfUnique()).to(factory::setReplyTemplate);
        if (properties.getType().equals(KafkaProperties.Listener.Type.BATCH)) {
            factory.setBatchListener(true);
            factory.setBatchErrorHandler(batchErrorHandler.getIfUnique());
        }
        else {
            factory.setErrorHandler(errorHandler.getIfUnique());
        }

        map.from(afterRollbackProcessor.getIfUnique()).to(factory::setAfterRollbackProcessor);
        map.from(recordInterceptor.getIfUnique()).to(factory::setRecordInterceptor);
        ContainerProperties container = factory.getContainerProperties();
        map.from(properties::getAckMode).to(container::setAckMode);
        map.from(properties::getClientId).to(container::setClientId);
        map.from(properties::getAckCount).to(container::setAckCount);
        map.from(properties::getAckTime).as(Duration::toMillis).to(container::setAckTime);
        map.from(properties::getPollTimeout).as(Duration::toMillis).to(container::setPollTimeout);
        map.from(properties::getNoPollThreshold).to(container::setNoPollThreshold);
        map.from(properties::getIdleEventInterval).as(Duration::toMillis).to(container::setIdleEventInterval);
        map.from(properties::getMonitorInterval).as(Duration::getSeconds).as(Number::intValue)
                .to(container::setMonitorInterval);
        map.from(properties::getLogContainerConfig).to(container::setLogContainerConfig);
        map.from(properties::isMissingTopicsFatal).to(container::setMissingTopicsFatal);
        map.from(kafkaTransactionManager.getIfUnique()).to(container::setTransactionManager);
        map.from(rebalanceListener.getIfUnique()).to(container::setConsumerRebalanceListener);
    }





}
