FROM maven:3.8.6-eclipse-temurin-17-alpine AS BUILDER
COPY . /opt/workspace
RUN --mount=type=cache,target=/root/.m2 cd /opt/workspace && mvn package -Dmaven.test.skip=true

# Main image
FROM amazoncorretto:17-alpine3.15
COPY --from=BUILDER /opt/workspace/target/app.jar /opt/
ENTRYPOINT [ "java", "-jar", "/opt/app.jar" ]
