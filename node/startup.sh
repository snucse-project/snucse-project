#!/bin/bash

./node_modules/.bin/pm2 start app.js
sleep 10

echo $(netstat -lntp | grep 3000) # for ubuntu
# echo $(netstat -vanp tcp | grep 3000) # for mac

curl -d '{ "path": "../tmp/data/enwiki_test_articles.json" }' \
     -H "Content-Type: application/json" \
     -X POST localhost:3000/init
sleep 300
curl -X GET localhost:3000/article/Anarchism # test query

exit(0)