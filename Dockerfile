FROM openjdk:8u141-jdk
VOLUME /tmp
VOLUME /log
EXPOSE 8080
ADD target/shorturl-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Xmx128m -Djava.security.egd=file:/dev/./urandom -Djava.net.preferIPv4Stack=true -jar /app.jar