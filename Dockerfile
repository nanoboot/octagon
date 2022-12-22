## Stage 1 (building)
# Compile sources and create the final application artifacts
FROM maven:3.8.3-openjdk-17 as builder

ARG OCTAGON_MAVEN_REPO_PASSWORD
RUN echo OCTAGON_MAVEN_REPO_PASSWORD=$OCTAGON_MAVEN_REPO_PASSWORD
RUN microdnf install zip

WORKDIR /usr/src

COPY . .

RUN unzip ./m2-cache-repository.zip -d m2-cache-repository

RUN mkdir /root/.m2&&mv /usr/src/m2-cache-repository/m2-cache-repository /root/.m2/repository&&mv /usr/src/maven/settings.xml /root/.m2/settings.xml

RUN sed -i "s/OCTAGON_MAVEN_REPO_PASSWORD/$OCTAGON_MAVEN_REPO_PASSWORD/" /root/.m2/settings.xml

RUN mvn -s /root/.m2/settings.xml dependency:go-offline
RUN mvn -Dmaven.test.skip=true \
        -s /root/.m2/settings.xml \
        -f ./pom.xml clean install

RUN mvn -Dmaven.test.skip=true \
        -s /root/.m2/settings.xml \
        -f octagon-clazz-loader/pom.xml clean install

## Stage 2 (Tomcat server)
# Copy application artifacts and Tomcat configuration files
FROM tomcat:9.0.54-jdk17

COPY --from=builder /usr/src/octagon-web/target/*.war /usr/local/tomcat/webapps/octagon.war
COPY --from=builder /usr/src/octagon-clazz-loader/target/octagon-clazz-loader-*.jar /usr/local/tomcat/lib/octagon-clazz-loader.jar
#COPY --from=builder /usr/src/oct-cnf/oct.confpath /usr/local/oct.confpath
COPY --from=builder /usr/src/entrypoint.sh /usr/local/entrypoint.sh


ARG TAG_VERSION="0.0.0"
ARG TAG_BUILD="000"
ARG TAG_COMMIT="NOT DEFINED"
ARG BUILD_DATE="NOT DEFINED"

LABEL COMMIT=$TAG_COMMIT
LABEL Version=$TAG_VERSION
LABEL maintainer="robertvokac@nanoboot.org"


ENV TAG_COMMIT=$TAG_COMMIT
ENV ASI=$ASI
ENV BUILD_DATE=$BUILD_DATE
ENV TAG_VERSION=$TAG_VERSION
ENV TAG_BUILD=$TAG_BUILD

EXPOSE 8080
CMD ["sh", "/usr/local/entrypoint.sh", "2>&1"]
