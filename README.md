# MeetBase Platform 🎓

MeetBase is an enterprise-grade, event-driven microservices platform designed for educational companies. It provides a centralized system to manage the entire lifecycle of classes—from the initial proposal by a manager to the final automated booking on Google Calendar.

## 🏗️ System Architecture

```mermaid
flowchart TB
    %% External Entities
    User([Client / Browser])
    GCal["Google Calendar API"]
    Gmail["Gmail SMTP Server"]

    subgraph Cluster [Kubernetes Cluster]
        direction TB
        
        %% Entrypoint
        Ingress["NGINX Ingress<br>api.meetbase.local"]
        Gateway["Gateway Microservice<br>(gateway-ms)"]

        subgraph Microservices [Backend Microservices]
            direction LR
            Auth["Auth Service<br>(auth-ms)"]
            Meet["Meet Service<br>(meet-ms)"]
            Notif["Notification Service<br>(notification-ms)"]
        end

        subgraph DataLayer [Data Persistence]
            direction LR
            AuthDB[("Auth PostgreSQL")]
            MeetDB[("Meet PostgreSQL")]
        end

        subgraph EventBus [Event Streaming]
            direction LR
            Kafka{"Apache Kafka<br>(Message Broker)"}
        end

        subgraph Observability [Observability Stack]
            direction LR
            Prom[("Prometheus")]
            Graf["Grafana"]
        end
    end

    %% Synchronous Traffic Flow
    User -->|HTTP Request| Ingress
    Ingress -->|Forward| Gateway
    Gateway -->|Login / Validate JWT| Auth
    Gateway -->|Appends Custom Headers & Routes| Meet

    %% Database Connections
    Auth <-->|Read/Write User Data| AuthDB
    Meet <-->|Read/Write Meet State| MeetDB

    %% Asynchronous / Event-Driven Flow
    Meet -.->|Publish Event| Kafka
    Kafka -.->|Consume Event| Notif

    %% External API Integrations
    Meet -->|Sync Approved Meets| GCal
    Notif -->|Send Emails| Gmail

    %% Observability Flow
    Prom -.->|Scrapes /prometheus| Microservices
    Graf -->|Queries Metrics| Prom

    %% Styling (Matches your dark/neon green presentation theme)
    classDef default fill:#1a1a1a,stroke:#555,color:#ddd;
    classDef external fill:#121212,stroke:#deff9a,stroke-width:2px,color:#fff;
    classDef proxy fill:#1a1a1a,stroke:#daffde,stroke-width:2px,color:#fff,stroke-dasharray: 5 5;
    classDef service fill:#2d4a4e,stroke:#daffde,stroke-width:2px,color:#fff;
    classDef db fill:#1a1a1a,stroke:#deff9a,stroke-width:2px,color:#fff;
    classDef broker fill:#1a1a1a,stroke:#3498db,stroke-width:2px,color:#fff;
    classDef obs fill:#1a1a1a,stroke:#ffb86c,stroke-width:2px,color:#fff;

    class User,GCal,Gmail external;
    class Ingress proxy;
    class Gateway,Auth,Meet,Notif service;
    class AuthDB,MeetDB db;
    class Kafka broker;
    class Prom,Graf obs;
```

---

## 🛠️ Microservices Breakdown

### **Gateway Service** (`gateway-ms`)

* **Validates** JWT via custom filters.
* **Injects** `role`, `email`, and `companyID` into headers for downstream authorization.
* **Routes** traffic to the appropriate backend services.

### **Auth Service** (`auth-ms`)

* **Handles** secure Login and Registration (BCrypt password encoding).
* **Issues** JWTs with custom role claims.
* **Exposes** internal endpoints for the Notification Service to fetch user emails by ID or Company Role.

### **Meet Service** (`meet-ms`)

* **Manages** complex state machine transitions (`CREATED` $\rightarrow$ `PENDING` $\rightarrow$ `APPROVED`).
* **Tracks** student registration thresholds (`min_stud_count`).
* **Publishes** asynchronous lifecycle events to Kafka.
* **Syncs** approved meetings directly to Google Calendar.

### **Notification Service** (`notification-ms`)

* **Consumes** Kafka event streams asynchronously.
* **Triggers** user alerts without blocking the main business workflows.
* **Delivers** emails dynamically using a Gmail SMTP server integration.

---

## 🔄 Core Business Workflow (The State Machine)

1. **Creation:** A `Manager` creates a class and sets a `min_stud_count`. The state is set to `CREATED`.
2. **Assignment:** A notification is sent to the assigned `Lecturer`. If accepted, the state shifts to `PENDING`. (If rejected, `CANCELLED`).
3. **Registration:** The meeting becomes visible to `Students`. As students register, the system tracks the count.
4. **Threshold Met:** Once `registered_students == min_stud_count`, an automated Kafka event notifies the Managers that the meeting is ready for approval.
5. **Approval & Sync:** The `Manager` approves the meet. The state becomes `APPROVED`, the Meet Service generates a Google Calendar Hangout link, saves the state, and fires a final notification to all participants.

---

## 📊 Observability & Metrics

MeetBase is fully instrumented for production-grade monitoring using **Spring Boot Actuator**, **Prometheus**, and **Grafana**.

**Custom Business Metrics Tracked:**

* `business.meet.created`: Tracks total meetings initialized.
* `business.failed.meet.approved`: Tracks approval failures (business rule violations or Google API crashes).
* `business.failed.meet.cancel`: Tracks failures to remove external Google events upon cancellation.
* `business.failed.email.fetch`: Tracks internal communication failures between `notification-ms` and `auth-ms`.
* `business.failed.notification.send`: Tracks SMTP delivery failures.


---

## 🚀 Getting Started

### Prerequisites

* Docker and Docker Compose
* Google Calendar API Credentials (`google-credentials.json`)
* Gmail App Password (for SMTP)

### Running the Stack

The entire infrastructure and microservices stack is containerized and orchestrated via Docker Compose.

1. Clone the repository.
2. Ensure your environment variables (like `MAIL_USERNAME` and JWT Secrets) are configured in the `docker-compose.yml`.
3. Boot the cluster:

```bash
docker-compose up -d --build

```

### Access Points

* **Main API Gateway:** `http://localhost:8000`
* **Kafka UI:** `http://localhost:8081`
* **Prometheus:** `http://localhost:9090`
* **Grafana:** `http://localhost:3000` *(Default Login: admin / admin)*# MeetBase
