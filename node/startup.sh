#!/bin/bash

npm start
curl -d '{ "path": "../tmp/data/enwiki_test_articles.json" }' \
     -H "Content-Type: application/json" \
     -X POST localhost:3000/init
