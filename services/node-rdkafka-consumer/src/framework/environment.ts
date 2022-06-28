import "dotenv/config";

import * as env from "env-var";

const getString = (key: string) => env.get(key).required().asString();
const getInt = (key: string) => env.get(key).required().asInt();

export const kafkaBootstrapServer = getString("KAFKA_BOOTSTRAP_SERVER");
export const kafkaUsername = getString("KAFKA_USERNAME");
export const kafkaPassword = getString("KAFKA_PASSWORD");
export const kafkaTopicMultiplePartitions = getString(
  "TOPIC_MULTIPLE_PARTITIONS"
);
export const benchmarkMessagesCount = getInt("BENCHMARK_MESSAGES_COUNT");
