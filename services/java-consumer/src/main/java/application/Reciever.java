package application;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class Receiver {
    boolean isStarted = false;
    int messagesCount = 0;

    @KafkaListener(id = "kafka-batch", topics = "java-multiple-partitions")
    public void receive(@Payload List<ConsumerRecord> messages) {
        if (!isStarted) {
            isStarted = true;
            Monitor.consumeAllStart();
        }

        System.out.println(messages.size() + " batch messages received");

        messagesCount += messages.size();

         if (messagesCount == 10000)  {
             Monitor.consumeAllEnd("java-multiple-partitions", messagesCount);
             isStarted = false;
             messagesCount = 0;
        }
        System.out.println("end of batch receive");
    }

}