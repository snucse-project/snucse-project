FROM openjdk:11-jdk
WORKDIR /app
COPY ./springboot /app
RUN ./gradlew bootJar
EXPOSE 8080
CMD java -jar build/libs/wiki-search-engine-api.jar
