FROM openjdk:8-jdk-slim
VOLUME /tmp
VOLUME /log
EXPOSE 8080
ADD target/shorturl-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Xmx128m -Djava.security.egd=file:/dev/./urandom -Djava.net.preferIPv4Stack=true -jar /app.jar