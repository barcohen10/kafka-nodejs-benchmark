import { AsyncRouter } from "express-async-router";
import { Request } from "express";
import produceMultipleMessages from "../../../produceMultipleMessages";

const restartPodAfterDelay = (milliseconds: number) => {
  setTimeout(() => {
    console.log("Restarting pod after produce", { extra: { milliseconds } });
    process.exit(1);
  }, milliseconds);
};

const router = AsyncRouter();

router.get(
  "/produce",
  async (req: Request<{}, {}, {}, { amountOfMessages: string }>, res) =>
  {
      try {
          const { amountOfMessages } = req.query;

          if (!Number(amountOfMessages)) {
              throw new Error("amountOfMessages is not a number");
          }

          await produceMultipleMessages(parseInt(amountOfMessages));

          restartPodAfterDelay(20000);

          return res.status(200);
      } catch (e) {
          console.error(`GET /produce error`, { e });
          return res.status(500);
      }
  }
);

export default router;
