# Spring Boot and Kafka Streams and Interactive Queries Demo

## Use Case: Real-Time Vehicle Status Monitoring
A logistics company manages a fleet of delivery trucks across the country and wants to query and monitor the real-time status of each truck, including its location, speed, and fuel level.
This needs to be a streaming application with **Kafka Streams** and **interactive queries** which can run in both a single instance and a distributed environment with multiple instances. For simplicity, the implementation only handles multiple instances running on the same machine.
The application exposes a `/vehicles` endpoint to query a list of current status for all vehicles and `/vehicles/{vehicle_id}` endpoint to query the current status of a specific vehicle ID.


## Overview
This is a Java and Spring Boot and Kafka Streams API microservice that consumes vehicle messages from a Kafka topic. The key represents the vehicle ID and the value is the Vehicle object.
It has been designed to handle and process streaming data in both a single instance and a distributed environment with multiple instances.


## Prerequisites
* Java 17 or higher
* Maven
* Docker (optional, for running Docker Compose which include Zookeeper and Apache Kafka)


## Running the Application
1. **Start Kafka and Zookeeper by using Docker Compose file in the repository**:
   ```sh
   docker-compose up
   ```

2. **Build**:
   ```sh
   mvn clean package
   ```

3. **Run the application**

   Run one instance of the application on port 8080 and another one on port 8081:

   For Unix-like systems:
    ```sh
    java -jar ./target/spring-boot-kafka-streams-interactive-demo-1.0-SNAPSHOT.jar --server.port=8080
    java -jar ./target/spring-boot-kafka-streams-interactive-demo-1.0-SNAPSHOT.jar --server.port=8081
    ```
   For Windows:
    ```sh
    java -jar .\target\spring-boot-kafka-streams-interactive-demo-1.0-SNAPSHOT.jar --server.port=8080
    java -jar .\target\spring-boot-kafka-streams-interactive-demo-1.0-SNAPSHOT.jar --server.port=8081
    ```

4. **Run Producer**

   Run [producer](./src/main/java/space/zeinab/demo/kafka/producer/VehicleMockDataProducer.java) to producer mock data.

5. **calling endpoints**:

   Shell script to run the application on Unix-like systems:
   ```sh
   ./scripts/run.sh
   ```
   Batch script to run the application on Windows:
   ```sh
   scripts\run.bat
   ```