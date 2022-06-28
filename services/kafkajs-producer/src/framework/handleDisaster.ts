export default () => {
  process.on("uncaughtException", (err: Error) => {
    console.error("An uncaught exception has happened", { err });
  });

  process.on("unhandledRejection", (reason) => {
    console.error("handle disaster found uncaught rejection", {
      extra: { reason },
    });
  });

  process.on("warning", (err: Error) => {
    console.log("handle disaster found warning", { err });
  });
};
