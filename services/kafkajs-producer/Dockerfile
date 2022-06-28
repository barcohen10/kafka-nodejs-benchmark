FROM node:16.3.0-alpine as base
WORKDIR /service

ARG version
ENV VERSION=$version

RUN apk --no-cache add curl

FROM base as dependencies
COPY package.json yarn.lock tsconfig.json ./
RUN yarn --pure-lockfile --ignore-engines --production true

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
