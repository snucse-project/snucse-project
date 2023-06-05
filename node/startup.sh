#!/bin/bash

sleep 10

echo $(netstat -lntp | grep 3000) # for ubuntu
# echo $(netstat -vanp tcp | grep 3000) # for mac

curl -d '{ "path": "../tmp/data" }' \
     -H "Content-Type: application/json" \
     -X POST localhost:3000/init
sleep 30
curl -X GET localhost:3000/article/Anarchism # test query
