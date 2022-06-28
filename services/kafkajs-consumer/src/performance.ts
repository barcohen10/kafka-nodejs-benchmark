import { Batch } from "kafkajs";
import { benchmarkMessagesCount } from "./framework/environment";

const getStringLengthInBytes = (str) => Buffer.byteLength(str, "utf-8");

export default class Performance {
  private static instance: Performance;
  messagesRead: number;
  bytesRead: number;
  startTime?: number;
  isConsumeStarted: boolean;

  private constructor() {
    this.messagesRead = 0;
    this.bytesRead = 0;
    this.isConsumeStarted = false;
  }

  public static getInstance(): Performance {
    if (!Performance.instance) {
      Performance.instance = new Performance();
    }

    return Performance.instance;
  }

  public monitor({ messages }: Batch): Promise<void> {
    return new Promise((resolve) => {
      const currentTime = new Date().getTime();

      if (!this.isConsumeStarted) {
        this.isConsumeStarted = true;
        console.log(`Start consume of ${benchmarkMessagesCount} messages`);
        this.startTime = currentTime;
      }

      this.messagesRead += messages.length;

      console.log(`Reading a batch`, {
        extra: {
          benchmarkMessagesCount,
          batchSize: messages.length,
          messagesRead: this.messagesRead,
          messagesLeftToRead: benchmarkMessagesCount - this.messagesRead,
        },
      });

      for (const message of messages) {
        this.bytesRead += getStringLengthInBytes(message.key);
        this.bytesRead += getStringLengthInBytes(message.value);
      }

      if (this.messagesRead >= benchmarkMessagesCount) {
        const dataConsumedInMB = this.bytesRead / 1000000;
        const executionTime = currentTime - (this.startTime || 0);
        const secondsPassed = executionTime / 1000;
        const mbPerSecond = dataConsumedInMB / secondsPassed;

        console.log(`End consume of ${benchmarkMessagesCount} messages`, {
          extra: {
            startTime: this.startTime,
            endTime: currentTime,
            executionTime,
            secondsPassed,
            bytesRead: this.bytesRead,
            dataConsumedInMB,
            mbPerSecond,
            amountOfMessages: benchmarkMessagesCount,
          },
        });

        this.messagesRead = 0;
        this.bytesRead = 0;
        this.isConsumeStarted = false;
      }

      resolve();
    });
  }
}
