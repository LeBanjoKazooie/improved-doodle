# Report Service (Order Management)

## Description

The **Report Service** is a microservice responsible for receiving completed orders from the **Purchase Service** via RabbitMQ, saving them to a PostgreSQL database, and confirming the successful order by sending the generated `order_id` back to the Purchase Service.

This service acts as the order logger in the system and offers a REST API to view or delete order records.

---

## Features

- ✅ Consumes order messages from RabbitMQ
- ✅ Saves orders to PostgreSQL using Hibernate ORM (Panache)
- ✅ Sends `order_id` back to the Purchase Service via RabbitMQ
- ✅ REST API for viewing and deleting orders
- ✅ Uses Quarkus, MicroProfile Reactive Messaging, and Panache

---

## Installation Instructions

### Requirements

- Java 17+
- Maven 3.8+
- Docker (for PostgreSQL and RabbitMQ)

### Setup

1. **Clone the repository:**

   ```bash
   git clone <your-repo-url>
   cd report-service
   ```

2. **Start PostgreSQL and RabbitMQ using Docker:**

   ```bash
   docker-compose up -d
   ```

3. **Configure environment (optional):**
   All configurations are in `src/main/resources/application.properties`.

---

## How to Run

To run the service locally:

```bash
./mvnw quarkus:dev
```

This will start the Report Service on **port `8083`**.

---

## REST API

| Method | Endpoint       | Description            |
| ------ | -------------- | ---------------------- |
| GET    | `/orders`      | Returns all orders     |
| GET    | `/orders/{id}` | Returns an order by ID |
| DELETE | `/orders/{id}` | Deletes an order by ID |

Example:

```bash
curl http://localhost:8083/orders
```

---

## Messaging Channels

### Incoming

| Channel   | Exchange         | Routing Key | Queue Name     |
| --------- | ---------------- | ----------- | -------------- |
| orders-in | `order-exchange` | `order.new` | `report-queue` |

### Outgoing

| Channel       | Exchange           | Routing Key       |
| ------------- | ------------------ | ----------------- |
| order-confirm | `confirm-exchange` | `order.confirmed` |

---

## File Structure

```plaintext
.
├── src/
│   ├── main/
│   │   ├── docker/
│   │   │   └── Dockerfile.jvm
│   │   │   └── Dockerfile.legacy-jar
│   │   │   └── dockerfile.native
│   │   │   └── dockerfile.native-micro
│   │   ├── java/
│   │   │   └── edu/
│   │   │       └── franklin/
│   │   │           ├── Order.java
│   │   │           └── OrderResource.java
│   │   │           └── OrderConsumer.java
│   │   ├── resources/
│   │   │   └── application.properties
│   │   │   └── import.sql
└── .dockerignore
└── .gitignore
└── mvnw
└── mvnw.cmd
├── pom.xml
└── README.md

```

---

## Known Issues

- Order payload from Purchase must match the entity fields, or a deserialization error will occur.

---

## Author

**Prem Dahal**
