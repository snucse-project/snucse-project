# Build stage
FROM node:18.12-alpine3.16

WORKDIR /app

COPY package.json .
# cmake, build-essentials, libglib2.0-dev required for node-btree
RUN apk update && \
apk --no-cache --virtual add \
g++ \
cmake build-base glib-dev

RUN npm install

RUN chown -R node:node /app/node_modules
COPY . .

USER node

EXPOSE 3000

CMD ["npm", "start"]