import "dotenv/config";

import handleDisaster from "./framework/handleDisaster";
import { initKafka } from "./kafka";
import app from "./server/express";

(async () => {
  handleDisaster();

  app.listen(3000, async () => {
    console.log("node-rdkafka consumer service is running on port 3000");
    await initKafka();
  });
})().catch((err) => console.error("error caught at index", { err }));
