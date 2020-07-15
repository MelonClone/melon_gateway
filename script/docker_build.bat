@echo off
pushd "%~dp0"

cd ../
call ./gradlew.bat clean build
cd ./docker
docker build -t melon-gateway:latest .
docker container stop melon-gateway
docker container rm melon-gateway
docker run -d --net mynet --ip 172.19.0.111 -p 8765:8765 --name melon-gateway melon-gateway:latest
cd ../

:exit
popd
@echo on