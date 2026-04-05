FROM ubuntu:22.04

RUN apt-get update && apt-get install -y wget tar curl unzip git build-essential && rm -rf /var/lib/apt/lists/*

RUN wget https://download.oracle.com/java/25/latest/jdk-25_linux-x64_bin.tar.gz -O /tmp/jdk25.tar.gz \
    && mkdir -p /opt/jdk25 \
    && tar -xzf /tmp/jdk25.tar.gz -C /opt/jdk25 --strip-components=1 \
    && rm /tmp/jdk25.tar.gz

ENV JAVA_HOME=/opt/jdk25
ENV PATH="$JAVA_HOME/bin:$PATH"

RUN wget https://archive.apache.org/dist/maven/maven-3/3.9.3/binaries/apache-maven-3.9.3-bin.tar.gz -O /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt \
    && rm /tmp/maven.tar.gz

ENV MAVEN_HOME=/opt/apache-maven-3.9.3
ENV PATH="$MAVEN_HOME/bin:$PATH"

RUN java -version
RUN mvn -version

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
RUN chmod +x ./mvnw
RUN mvn dependency:go-offline

COPY src src

RUN mvn clean package -DskipTests

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]