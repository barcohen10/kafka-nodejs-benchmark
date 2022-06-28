import * as express from "express";
import { AsyncRouter } from "express-async-router";

import isAlive from "./routes/isAlive";
import produce from "./routes/produce";

const router = AsyncRouter();

router.get("/isAlive", isAlive);
router.get("/produce", produce);

export default express().use(router);
