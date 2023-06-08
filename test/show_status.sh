#!/bin/bash

while true; do
    kubectl top pods --namespace wiki-search-engine
    sleep 5
done