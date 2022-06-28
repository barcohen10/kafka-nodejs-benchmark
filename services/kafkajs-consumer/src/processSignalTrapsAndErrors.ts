import { Consumer } from "kafkajs";

const errorTypes = ["unhandledRejection", "uncaughtException"];
const signalTraps = ["SIGTERM", "SIGINT", "SIGUSR2"];

export default (consumer: Consumer) => {
  errorTypes.map((type) => {
    process.on(type, async (err) => {
      try {
        console.error(type, { err });
        await consumer.disconnect();
        process.exit(0);
      } catch (_) {
        process.exit(1);
      }
    });
  });

  signalTraps.map((type) => {
    process.once(type, async () => {
      try {
        console.error(`signal traps: ${type}`);
        await consumer.disconnect();
      } finally {
        process.kill(process.pid, type);
      }
    });
  });
};
