package application;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import org.json.JSONObject;

import java.util.Date;

public class Monitor {
    private static Counter produceSuccess;
    private static Counter produceError;
    private static Histogram produceLatency;
    private static Histogram produceMultipleMessagesLatency;
    private static double[] buckets = {0.003 ,0.03 ,0.1 ,0.3,1.5,10.0};
    private static Monitor instance = null;

    private Monitor() {
    }

    public static Monitor getInstance()
    {
        if (instance == null)
            instance = new Monitor();

        return instance;
    }
    public void init() {
        produceSuccess = Counter.build().name("produce_success").labelNames("topic").help("produce_success").register();
        produceError = Counter.build().name("produce_error").help("produce_error").register();

        produceLatency =
            Histogram
                .build()
                .buckets(buckets)
                .name("produce_latency")
                .labelNames("topic")
                .help("produce_latency")
                .register();

        produceMultipleMessagesLatency =
                Histogram
                        .build()
                        .buckets(buckets)
                        .name("produce_multiple_messages_latency")
                        .labelNames("topic", "amountOfMessages")
                        .help("produce_multiple_messages_latency")
                        .register();
    }

    public void produceAllEnd(String topic, int amountOfMessages, long produceAllMessagesStart) {
        double executionTimeProduceAllMessages = ((double) (new Date().getTime() - produceAllMessagesStart)) / 1000;

        JSONObject log = new JSONObject()
                .put("level", "info")
                .put("message", "produce all messages execution time")
                .put(
                        "extra",
                        new JSONObject()
                                .put("executionTimeProduceAllMessages", executionTimeProduceAllMessages)
                                .put("amountOfMessages", amountOfMessages)
                );

        write(log);

        produceMultipleMessagesLatency.labels(topic, Integer.toString(amountOfMessages));
    }

    public void produceSuccess(ProducerRequest producerRequest, long executionStart) {
        double executionTime = ((double) (new Date().getTime() - executionStart)) / 1000;

        produceSuccess.labels(producerRequest.topic).inc();
        produceLatency.labels(producerRequest.topic).observe(executionTime);

        JSONObject log = new JSONObject()
            .put("level", "info")
            .put("message", "produce success")
            .put(
                "extra",
                new JSONObject()
                        .put("topic", producerRequest.topic)
                        .put("key", producerRequest.key)
                        .put("executionTime", executionTime)
            );

        write(log);
    }

    public void produceError(Exception exception) {
        JSONObject log = new JSONObject()
            .put("level", "error")
            .put("message", "produce failed")
            .put("err", new JSONObject().put("message", exception.getMessage()));

        write(log);

        produceError.inc();
    }

    public void started() {
        JSONObject log = new JSONObject().put("level", "info").put("message", "kafka-producer started");
        write(log);
    }

    private static void write(JSONObject log) {
        System.out.println(log.toString());
    }
}
