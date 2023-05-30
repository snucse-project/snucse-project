#!/bin/bash

./node_modules/.bin/pm2 start app.js
echo $(netstat -lntp | grep 3000)

curl -d '{ "path": "../tmp/data/enwiki_test_articles.json" }' \
     -H "Content-Type: application/json" \
     -X POST localhost:3000/init
curl -X GET localhost:3000/article/Anarchism # test query
