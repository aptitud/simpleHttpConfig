FROM java:8-jre
MAINTAINER johan.elmstrom@aptitud.se

COPY target/scala-2.11/http-config-assembly-*.jar http-config.jar

CMD java -jar http-config.jar

EXPOSE 8080