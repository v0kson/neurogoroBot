FROM eclipse-temurin:17 AS jre-build
CMD ["echo", "start building"]
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:17-jdk
WORKDIR /opt/app
COPY --from=jre-build /opt/app/target/*.jar /opt/app/*.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]

RUN echo 'конец сборки'