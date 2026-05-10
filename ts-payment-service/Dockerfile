# Build stage
FROM maven:3.8.5-openjdk-8-slim AS build
WORKDIR /app

# Copy the parent pom and remove all modules first
COPY pom.xml .
RUN sed -i '/<modules>/,/<\/modules>/d' pom.xml

# Copy the common library and the specific service
COPY ts-common ./ts-common
COPY ts-payment-service ./ts-payment-service

# Add ONLY ts-common and the specific service back to the modules section so Maven can find them
RUN sed -i '/<\/project>/i <modules><module>ts-common</module><module>ts-payment-service</module></modules>' pom.xml

# Build the project
RUN mvn package -pl ts-payment-service -am -DskipTests

# Run stage
FROM eclipse-temurin:8-jre

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

WORKDIR /app
COPY --from=build /app/ts-payment-service/target/*.jar /app/service.jar

# Maintain original JVM flags (usually -Xmx200m)
CMD ["java", "-Xmx200m", "-jar", "/app/service.jar"]

EXPOSE 19001
