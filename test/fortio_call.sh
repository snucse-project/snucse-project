#!/usr/bin/bash

docker pull fortio/fortio
docker run -p 8088:8080 --name fortio fortio/fortio server # run fortio server port=8088
fortio curl -stdclient -payload-file config.json "http://localhost:8088/fortio/rest/run?jsonPath=.metadata" > result.json

