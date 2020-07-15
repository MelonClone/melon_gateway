FROM adoptopenjdk/openjdk11:slim

# 기본 툴 설치
RUN apt-get update
RUN apt-get upgrade -y

RUN apt-get install git -y && \
	apt-get install vim -y 

# Local time, timezone 지정
# RUN /bin/cp -f /etc/localtime /etc/localtime.org && \
# 	/bin/cp -f /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 소스 Pull
# RUN mkdir -p /src/app
# WORKDIR /src/app
# RUN git clone -b master https://github.com/MelonClone/melon_gateway.git
# WORKDIR ./melon_gateway
# RUN chmod +x ./gradlew
# CMD ["./gradlew", "bootRun"]

# Jar Pull
RUN mkdir -p /src/app/melon_gateway
WORKDIR /src/app/melon_gateway
# RUN git clone -b build https://github.com/MelonClone/melon_gateway.git
COPY ./melon_gateway-0.0.1-SNAPSHOT.jar ./melon_gateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "melon_gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 22 80 8765