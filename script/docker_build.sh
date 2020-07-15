#!/usr/bin/env bash
./gradlew clean build
docker build -t melon-gateway:latest .
docker container stop melon-gateway
docker container rm melon-gateway
docker run -d --net mynet --ip 172.19.0.111 -p 8765:8765 --name melon-gateway melon-gateway:latest