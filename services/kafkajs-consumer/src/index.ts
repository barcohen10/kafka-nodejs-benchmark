import "dotenv/config";

import handleDisaster from "./framework/handleDisaster";
import getKafka, { initKafka } from "./kafka";
import { kafkaTopicMultiplePartitions } from "./framework/environment";
import app from "./server/express";
import processSignalTrapsAndErrors from "./processSignalTrapsAndErrors";
import Performance from "./performance";

(async () => {
  handleDisaster();
  initKafka();

  app.listen(3000, async () => {
    console.log("KafkaJS consumer service is running");

    const consumer = getKafka().consumer({ groupId: "consumer-group-id" });

    processSignalTrapsAndErrors(consumer);

    await consumer.connect();
    await consumer.subscribe({ topic: kafkaTopicMultiplePartitions });

    await consumer.run({
      partitionsConsumedConcurrently: 10,
      eachBatch: async ({ batch }) => Performance.getInstance().monitor(batch),
    });
  });
})().catch((err) => console.error("error caught at index", { err }));
