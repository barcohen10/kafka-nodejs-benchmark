import {
  kafkaBootstrapServer,
  kafkaUsername,
  kafkaPassword,
  kafkaTopicMultiplePartitions,
} from "./framework/environment";
import { KafkaConsumer, Message, ConsumerStream } from "node-rdkafka";
import Performance from "./performance";

let kafkaConsumerStream: ConsumerStream;

export const initKafka = async () =>
    () =>
        new Promise((resolve) => {
            try {
                const kafkaConsumerStream = KafkaConsumer.createReadStream(
                    {
                        "group.id": "node-rdkafka-consumer-group",
                        "bootstrap.servers": kafkaBootstrapServer,
                        "security.protocol": "sasl_ssl",
                        "sasl.mechanism": "PLAIN",
                        "sasl.username": kafkaUsername,
                        "sasl.password": kafkaPassword,
                    },
                    { "auto.offset.reset": "earliest" },
                    {
                        streamAsBatch: true,
                        topics: [kafkaTopicMultiplePartitions],
                    }
                );

                kafkaConsumerStream.consumer.connect();

                kafkaConsumerStream.consumer
                    .on("ready", () => {
                        console.log("Kafka Consumer connected and waiting for events");
                        resolve(null);
                    })
                    .on("disconnected", (metrics) => {
                        console.log(`consumer disconnected`, { extra: { metrics } });
                    });

                kafkaConsumerStream.on("data", (messages: Message[]) => {
                    Performance.getInstance().monitor(messages);
                });
            } catch (err) {
                console.error(`initKafka error`, { err });
            }
        });

export default () => {
  return kafkaConsumerStream.consumer;
};
