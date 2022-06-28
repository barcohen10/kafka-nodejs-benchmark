# node-rdkafka Consumer

Kafka consumer built using the node-rdkafka library

## Installation

```bash
npm install
```

## Running locally

Create .env file with the following params:

```javascript
KAFKA_BOOTSTRAP_SERVER=""
KAFKA_USERNAME=""
KAFKA_PASSWORD=""
TOPIC_MULTIPLE_PARTITIONS=""
BENCHMARK_MESSAGES_COUNT=""
```

and run the command:
```bash
npm run build && npm run start:dev
```