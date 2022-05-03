FROM openjdk:8-jdk
COPY build/distributions/MetaHeuristics-1.0-SNAPSHOT-java8.zip /usr/src/
WORKDIR /usr/src/
RUN unzip MetaHeuristics-1.0-SNAPSHOT-java8.zip
RUN ls /usr/src/
COPY resources/* resources/
RUN ls MetaHeuristics-1.0-SNAPSHOT-java8/
RUN ./MetaHeuristics-1.0-SNAPSHOT-java8/bin/MetaHeuristics
