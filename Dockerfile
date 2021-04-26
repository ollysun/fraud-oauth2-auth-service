FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn -B -f ./pom.xml clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
#Production packaging
FROM openjdk:8-jre-alpine


ARG DEPENDENCY=target/dependency
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/billa-auth-service.jar /app/billa-auth-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "billa-auth-service.jar"]
