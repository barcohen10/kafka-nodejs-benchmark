import { AsyncRouter } from "express-async-router";

let serverRunning = true;
process.on("SIGTERM", () => {
  serverRunning = false;
});

const router = AsyncRouter();

router.get("/isAlive", async (_, res) => {
  if (serverRunning) {
    return true;
  }
  res.status(500);
  return false;
});

export default router;
