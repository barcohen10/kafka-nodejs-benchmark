package application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@EnableKafka
@Configuration
public class Config {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() throws IOException {
        final Properties config = Utils.loadProperties("configuration/prod.properties");
        Map<String, Object> consumerConfig = (Map)config;

        return new DefaultKafkaConsumerFactory<>(consumerConfig);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() throws IOException {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // enable batch listening
        factory.setBatchListener(true);
        factory.setBatchErrorHandler(new BatchLoggingErrorHandler());
        return factory;
    }

}