package application;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Consumer implements Runnable {
    private final KafkaConsumer<String, String> consumer;
    private final List<String> topics;
    private final AtomicBoolean shutdown;
    private final CountDownLatch shutdownLatch;
    private int messagesCount;
    private long bytesRead = 0;

    Monitor monitor;

    public Consumer(Properties config, List<String> topics, Monitor monitor) {
        this.consumer = new KafkaConsumer<>(config);
        this.topics = topics;
        this.shutdown = new AtomicBoolean(false);
        this.shutdownLatch = new CountDownLatch(1);
        this.messagesCount = 0;
        this.monitor = monitor;
    }

    public void process(ConsumerRecord<String, String> record) {
        System.out.printf("Messages consumed, topic:%s Key:%s Value:%s [partition %s]\n",
                record.topic(), record.key(), record.value(), record.partition());

        bytesRead += record.key().getBytes(StandardCharsets.UTF_8).length;
        bytesRead += record.value().getBytes(StandardCharsets.UTF_8).length;
        messagesCount++;
    };

    public void run() {
        try {
            boolean isStarted = false;

            consumer.subscribe(topics);

            while (!shutdown.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1));

                if (!isStarted && !records.isEmpty()) {
                    isStarted = true;
                    Monitor.consumeAllStart();
                } else if(isStarted && records.isEmpty()) {
                    isStarted = false;
                    Monitor.consumeAllEnd(topics.toString(), messagesCount);
                    messagesCount = 0;
                    bytesRead = 0;
                }

                records.forEach(record -> process(record));
            }
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    public void shutdown() throws InterruptedException {
        shutdown.set(true);
        shutdownLatch.await();
    }
}