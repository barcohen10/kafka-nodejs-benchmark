import { AsyncRouter } from "express-async-router";
import produce from "../../../produce";
import { kafkaTopicMultiplePartitions } from "../../../framework/environment";
import { Request } from "express";
import { getMessagesArray } from "../../../utils";

const restartPodAfterDelay = (milliseconds: number) => {
  setTimeout(() => {
    console.log("Restarting pod after produce", { milliseconds });
    process.exit(1);
  }, milliseconds);
};

const router = AsyncRouter();

router.get(
  "/produce",
  async (req: Request<{}, {}, {}, { amountOfMessages: string }>, res) => {
    try {
      const { amountOfMessages } = req.query;

      if (!Number(amountOfMessages)) {
        throw new Error("amountOfMessages is not a number");
      }

      res.status(200).send({ success: true });

      await produce(
        kafkaTopicMultiplePartitions,
        getMessagesArray(parseInt(amountOfMessages))
      );

      restartPodAfterDelay(20000);
    } catch (e) {
      console.error("GET /produce", { e });
      res.status(500).send({ success: false });
    }
  }
);

export default router;
