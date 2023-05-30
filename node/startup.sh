#!/bin/bash

./node_modules/.bin/pm2 start app.js
curl -d '{ "path": "../tmp/data/enwiki_test_articles.json" }' \
     -H "Content-Type: application/json" \
     -X POST localhost:3000/init
curl -X GET localhost:3000/article/Anarchism # test query
