import getKafka from "../kafka";

export default () => {
  process.on("uncaughtException", (err: Error) => {
    console.error("An uncaught exception has happened", { err });
    getKafka()?.disconnect();
  });

  process.on("unhandledRejection", (reason) => {
    console.error("handle disaster found uncaught rejection", {
      extra: { reason },
    });
    getKafka()?.disconnect();
  });

  process.on("warning", (err: Error) => {
    console.log("handle disaster found warning", { err });
  });

  process.on("SIGINT", () => {
    console.log("Disconnecting consumer ...");
    getKafka()?.disconnect();
  });
};
