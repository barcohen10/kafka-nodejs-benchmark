import getKafkaProducer from "./kafka";
import { Message } from "./types";

export default async (topic: string, message: Message) =>
  new Promise((resolve, reject) => {
    getKafkaProducer().produce(
      // Topic to send the message to
      topic,
      null,
      Buffer.from(message.value),
      message.key,
      Date.now(),
      (err, offset) => {
        // Callback to receive delivery reports for messages
        if (err) {
          reject(err);
        }
        resolve(offset);
      }
    );
  });
