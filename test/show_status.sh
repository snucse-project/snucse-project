#!/bin/bash

while true; do
    kubectl top pods --namespace snucse-project
    sleep 5
    clear
done