package application;

import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Date;

public class AsyncProducer extends AbstractProducer {

    private static AsyncProducer instance = null;

    private AsyncProducer(Monitor monitor) {
        super(monitor);
    }

    public static AsyncProducer getInstance() {
        if (instance == null)
            instance = new AsyncProducer(Monitor.getInstance());

        return instance;
    }

    @Override
    public void produce(ProducerRequest producerRequest) {
        long executionStart = (new Date()).getTime();

        kafkaProducer.send(
                createRecord(producerRequest, executionStart),
                (RecordMetadata metadata, Exception err) -> {
                    if (err != null) {
                        Monitor.getInstance().produceError(err);
                        return;
                    }
                    Monitor.getInstance().produceSuccess(producerRequest, executionStart);
                }
        );
    }
}
