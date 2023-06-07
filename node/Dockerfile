# Build stage
FROM node:18.12-alpine3.16

WORKDIR /app

COPY . .
# cmake, build-essentials, libglib2.0-dev required for node-btree
RUN apk update && \
apk --no-cache --virtual add \
g++ cmake build-base glib-dev \
bash curl vim python3 py3-pip

RUN npm install

RUN chown -R node:node ./node_modules
RUN mkdir out
RUN chown -R node:node ./out
RUN chmod +x ./startup.sh

USER node

EXPOSE 3000

CMD ["npm", "start"]