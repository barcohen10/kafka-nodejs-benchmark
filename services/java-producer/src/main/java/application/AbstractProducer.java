package application;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public abstract class AbstractProducer {
    Monitor monitor;
    KafkaProducer<String, String> kafkaProducer;

    AbstractProducer(Monitor monitor) {
        this.monitor = monitor;
    }

    public void initializeProducer() throws IOException {
        final Properties props = Utils.loadProperties("configuration/prod.properties");
        kafkaProducer = new KafkaProducer<>(props);
    }

    public abstract void produce(ProducerRequest producerRequest) throws ExecutionException, InterruptedException;

    protected ProducerRecord<String, String> createRecord(ProducerRequest producerRequest, long executionStart) {
        return new ProducerRecord<>(
            producerRequest.topic,
            null,
            executionStart,
            producerRequest.key,
            producerRequest.value
        );
    }

    public void close() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }
 }
