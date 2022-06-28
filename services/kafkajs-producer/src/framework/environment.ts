import "dotenv/config";

import * as env from "env-var";

const getString = (key: string) => env.get(key).required().asString();

export const kafkaBootstrapServer = getString("KAFKA_BOOTSTRAP_SERVER");
export const kafkaUsername = getString("KAFKA_USERNAME");
export const kafkaPassword = getString("KAFKA_PASSWORD");
export const kafkaTopicMultiplePartitions = getString(
  "TOPIC_MULTIPLE_PARTITIONS"
);
