FROM node:16.3.0-alpine as base
WORKDIR /service

RUN apk --no-cache add \
      bash \
      g++ \
      ca-certificates \
      lz4-dev \
      musl-dev \
      cyrus-sasl-dev \
      openssl-dev \
      make \
      python3
RUN apk add --no-cache lz4-dev cyrus-sasl

ARG version
ENV VERSION=$version

FROM base as dependencies
COPY package.json yarn.lock tsconfig.json ./
RUN yarn --pure-lockfile --ignore-engines --production true
RUN rm -rf node_modules/node-rdkafka/deps node_modules/node-rdkafka/build/deps/*.a

FROM dependencies as build
RUN yarn --pure-lockfile --ignore-engines --production false
COPY ./src ./src/
RUN yarn build

FROM base as release
COPY --from=dependencies /service/node_modules ./node_modules
COPY --from=dependencies /service/package.json ./package.json
COPY --from=build /service/dist ./dist

ENV NODE_ENV=production

CMD [ "node", "dist/index.js" ]