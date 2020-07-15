#!/usr/bin/env bash
docker run --net mynet --ip 172.19.0.111 -p 8765:8765 --name melon-gateway melon-gateway:latest