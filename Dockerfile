FROM openjdk:11-jdk

ARG JAR_FILE="build/libs/*.jar"

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=deploy", "-Dspring-boot.run.arguments=--db.url=${DB_URL}, --db.username=${DB_USERNAME}, --db.password=${DB_PASSWORD}, --jwt.secret=${JWT_SECRET}", "-jar", "/app.jar"]