FROM openjdk:17-alpine

WORKDIR /app/

COPY ./build/libs/cloud-0.0.1-SNAPSHOT.jar ./kumo-backend.jar

EXPOSE 8080 

ENTRYPOINT ["java","-jar","./kumo-backend.jar"]