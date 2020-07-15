#!/usr/bin/env bash
./gradlew clean build
docker build -t melon-gateway-server:latest .
docker container stop melon-gateway
docker container rm melon-gateway
docker run --ip 172.19.0.111 -p 8765:8765 --name melon-gateway melon-gateway-server:latest