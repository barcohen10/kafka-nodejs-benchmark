import { CompressionTypes, Message } from "kafkajs";

import getKafka from "./kafka";
import * as _ from "lodash";
import processSignalTrapsAndErrors from "./processSignalTrapsAndErrors";

export default async (topic: string, messages: Message[]) => {
  try {
    const producer = getKafka().producer();

    processSignalTrapsAndErrors(producer);

    await producer.connect();

    const MAX_MESSAGES_AT_ONCE = 6000;
    const chunkMessages = _.chunk(messages, MAX_MESSAGES_AT_ONCE);

    for (const chunk of chunkMessages) {
      await producer.send({
        topic,
        messages: chunk,
        compression: CompressionTypes.GZIP,
      });
    }
  } catch (e) {
    console.error(`produce error`, { e });
    throw e;
  }
};
