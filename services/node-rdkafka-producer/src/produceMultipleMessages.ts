import runInParallel from "./runInParallel";
import produce from "./produce";
import { kafkaTopicMultiplePartitions } from "./framework/environment";
import * as _ from "lodash";

const UUID_KEYS = [
  "db653823-eed8-4f49-b104-5939bfa2427c",
  "9675519e-1a6f-4142-a8d3-5ab6b7cd9a66",
  "d7d73f18-2aa9-4f80-bfcc-be0efe23e8a9",
  "f1ba2417-afd7-4193-a794-1a9c797b9baa",
  "24798e62-7d7a-4a16-8637-48f998036e1c",
  "abe209a9-1a3e-492d-b4cd-ede817631307",
  "253b155b-773c-4dad-8267-5efa5bc0cb38",
  "d4bef748-931c-4d67-80cd-81e3d1e7caa7",
  "d4bef748-931c-4d67-80cd-81e3d1e7caa7",
  "3da07737-3588-41f7-b336-fc7f630a852f",
];

export default async (amountOfMessages: number) =>
{
    await runInParallel(
        produce.bind(this, kafkaTopicMultiplePartitions, {
            key: _.sample(UUID_KEYS),
            value: "same-value",
        }),
        amountOfMessages
    );
}
