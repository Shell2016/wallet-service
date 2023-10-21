FROM amazoncorretto:17-alpine
ENV APP_HOME=/opt/app
ARG JAR_FILE=./target/application*-jar-with-dependencies.jar
WORKDIR $APP_HOME
COPY $JAR_FILE app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]