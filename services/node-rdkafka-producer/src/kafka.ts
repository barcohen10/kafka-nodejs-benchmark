import {
  kafkaBootstrapServer,
  kafkaUsername,
  kafkaPassword,
  serviceName,
} from "./framework/environment";
import { HighLevelProducer, ProducerGlobalConfig } from "node-rdkafka";

let kafkaProducer: HighLevelProducer;

const initConfig: ProducerGlobalConfig = {
  "client.id": `${serviceName}-client`,
  "bootstrap.servers": kafkaBootstrapServer,
  "security.protocol": "sasl_ssl",
  "sasl.mechanism": "PLAIN",
  "batch.num.messages": 1000000,
  "sasl.username": kafkaUsername,
  "sasl.password": kafkaPassword,
};

export const initKafka = async () =>
    new Promise((resolve) => {
      try {
        console.log("init config", { extra: { initConfig } });
        kafkaProducer = new HighLevelProducer(initConfig, {});

        if (kafkaProducer) {
          console.log("calling kafkaProducer connect", {});

          kafkaProducer
              .connect({}, (err) => {
                if (err) {
                  console.error(`connection error`, { err });
                }
              })
              .on("ready", () => {
                console.log("Kafka Producer connected");
                kafkaProducer.setPollInterval(100);
                resolve(null);
              })
              .on("disconnected", (metrics) => {
                console.log(`producer disconnected`, { extra: { metrics } });
              })
              .on("event.error", function (err) {
                console.error(`Error in producer!`, { err });
              });

          console.log("kafkaProducer AFTER connect", {});
        }
      } catch (err) {
        console.error(`initKafka error`, { err});
      }
    })

export default () => {
  return kafkaProducer;
};
