# Check-in Service

## Document Link (Q&A / Design Notes)
Here's a link to a detailed paper that answers the design questions and explains the main decisions:
  <a href="Answers.pdf" target="blank" rel="noopener noreferrer">
  Document (Design Q&A)
  </a>
## Project Overview
This project implements a **production-oriented backend service** (“Check-in Service”) for handling **worker clock-in / clock-out events** in a factory environment.  
Card readers call a single REST endpoint with an employee ID. The service tracks working time and, on check-out, asynchronously triggers:

- total working hours to a **legacy labor cost recording system.**
- sending an **email notification** to the employee.

This project majorly focuses on **reliability**, **consistency**, and **failure handling**, not just regular CRUD operations.

## System Architecture & its key features
![alt text](System_Architecture.png)

### Worker Attendance Management
- Simple single endpoint for both clock-in and clock-out.
- Stores check-in/check-out values in postgreSQL database.
- `OPEN` status → worker currently inside
- `CLOSED` status → check-out done and total hours calculated and saved.
 
### Duplicate Scan Detection
- Stops double scans by mistake within a short time frame.
- Provides a buffer time of 60s after check-in and check-out.

### Event Preservation before publishing to kafka
- Once check-out is done successfully, the data which need to be sent to the legacy system & email notification is saved in an `Event` table.
- This prevents the loss of data, if kafka is down.
- A **Scheduler** runs at every **30s** intervals which is responsible for publishing the data to the kafka brokers.

### Kafka
- Consists of two kafka topics `legacy-topic` & `email-notification-topic`, where legacy system having the highest priority.

### Retry Handling
- Scheduler tries `4` times to publish to kafka, while each time recording the failure in the `Event` db.
- In case of a failed legacy system api call or email notification api call, the consumer tries few times and finally dumps it in DLT topic.

### Utility Frameworks
- Kafka UI included for inspecting topics & message flow.
- Mailhog included to capture outgoing emails in UI.
- WireMock included to simulate unpredictable legacy API behavior.

## How to Run?

### 1) Prerequisites
- Docker + Docker Compose
- Java 17+
- Maven (or use `./mvnw`)

### 2) Start Infrastructure (Postgres, Kafka, Kafka UI, WireMock, Mailhog)
From project root:

```bash
docker compose up -d
```
or 
```bash
docker compose -f src\main\resources\docker-compose.yml up -d
```

## Helpful URLs
- Swagger: `http://localhost:8080/swagger-ui/index.html#/check-in-check-out-controller/checkInOrOut`
- Kafka UI: `http://localhost:8090/ui/clusters/local/all-topics/`
- WireMock: `http://localhost:8081/__admin/requests`
- Mailhog: `http://localhost:8025/#`
