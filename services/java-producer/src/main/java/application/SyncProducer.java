package application;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class SyncProducer extends AbstractProducer {

    private static SyncProducer instance = null;

    private SyncProducer(Monitor monitor) {
        super(monitor);
    }

    public static SyncProducer getInstance()
    {
        if (instance == null)
            instance = new SyncProducer(Monitor.getInstance());

        return instance;
    }

    @Override
    public void produce(ProducerRequest producerRequest) {
        long executionStart = (new Date()).getTime();

        try {
            kafkaProducer.send(createRecord(producerRequest, executionStart)).get();
            Monitor.getInstance().produceSuccess(producerRequest, executionStart);
        } catch (InterruptedException | ExecutionException e) {
            Monitor.getInstance().produceError(e);
        }
    }
}
