import * as express from "express";
import { AsyncRouter } from "express-async-router";

import isAlive from "./routes/isAlive";

const router = AsyncRouter();

router.get("/isAlive", isAlive);

export default express().use(router);
