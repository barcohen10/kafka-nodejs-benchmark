import { Kafka, logLevel } from "kafkajs";
import {
  kafkaBootstrapServer,
  kafkaUsername,
  kafkaPassword,
} from "./framework/environment";

let kafka: Kafka;

export const initKafka = () => {
  try {
    kafka = new Kafka({
      brokers: [kafkaBootstrapServer],
      ssl: true,
      logLevel: logLevel.ERROR,
      sasl: {
        username: kafkaUsername,
        password: kafkaPassword,
        mechanism: "plain",
      },
    });
  } catch (err) {
    console.error(`initKafka error`, { err });
  }
};

export default () => {
  return kafka;
};
