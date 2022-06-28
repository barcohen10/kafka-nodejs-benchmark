package application;

import io.prometheus.client.Histogram;
import java.util.Date;
import org.json.JSONObject;

public class Monitor {
    private static Histogram consumeMultipleMessagesLatency;
    private static double[] buckets = {0.003 ,0.03 ,0.1 ,0.3,1.5,10.0};
    private static long consumeAllMessagesStart = -1;

    public static void init() {
        consumeMultipleMessagesLatency =
                Histogram
                        .build()
                        .buckets(buckets)
                        .name("consume_multiple_messages_latency")
                        .labelNames("topic", "amountOfMessages")
                        .help("consume_multiple_messages_latency")
                        .register();
    }

    public static void consumeAllStart() {
        consumeAllMessagesStart = (new Date().getTime());
    }
    public static void consumeAllEnd(String topic, int amountOfMessages) {
        double executionTimeConsumeAllMessages = ((double) (new Date().getTime() - consumeAllMessagesStart)) / 1000;

        JSONObject log = new JSONObject()
                .put("level", "info")
                .put("message", "consume all messages execution time")
                .put(
                        "extra",
                        new JSONObject()
                                .put("topic", topic)
                                .put("executionTimeConsumeAllMessages", executionTimeConsumeAllMessages)
                                .put("amountOfMessages", amountOfMessages)
                );

        write(log);

        consumeMultipleMessagesLatency.labels(topic, Integer.toString(amountOfMessages));
    }

    public static void started() {
        JSONObject log = new JSONObject().put("level", "info").put("message", "kafka-consumer started");
        write(log);
    }

    public static void serviceShutdown() {
        JSONObject log = new JSONObject().put("level", "info").put("message", "kafka-consumer shutdown");
        write(log);
    }

    private static void write(JSONObject log) {
        System.out.println(log.toString());
    }
}
