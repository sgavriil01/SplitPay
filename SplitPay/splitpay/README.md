# 💸 SplitPay – Group Expense Manager

A Java Spring Boot microservice that simplifies group expenses and optimizes debt settlement using algorithmic logic. Designed with scalability, testability, and real-world use cases in mind.

---

## 🚀 Features

- ✅ RESTful APIs for **Users**, **Groups**, and **Expenses**
- 🔄 **Debt Simplification** using Greedy & MinCashFlow algorithms
- 🧠 DTO Mapping with **MapStruct**
- 💾 PostgreSQL schema versioning with **Flyway**
- ⚡ Redis Caching for fast balance lookups
- 📘 Swagger UI for interactive API exploration
- 🧪 JUnit tests with CI using **GitHub Actions**
- 📦 Docker + Docker Compose integration
- ☁️ Optional deployment on Heroku or GCP

---

## 🧠 Architecture Overview

```
Spring Boot 3 (Java 17)
├── Controller Layer        → API Endpoints
├── Service Layer           → Business Logic
├── Repository Layer        → JPA-based DB Access
├── Model / DTO / Mapper    → Clean Data Separation
├── Redis                   → Caching Balance Sheets
├── Flyway                  → SQL Migrations
└── PostgreSQL              → Relational Data Store
```

---

## 🛠️ Tech Stack

| Layer      | Technology               |
| ---------- | ------------------------ |
| Language   | Java 17                  |
| Framework  | Spring Boot 3            |
| Database   | PostgreSQL + Flyway      |
| Caching    | Redis                    |
| Mapping    | MapStruct                |
| Testing    | JUnit 5                  |
| Docs       | Swagger / OpenAPI        |
| CI/CD      | GitHub Actions           |
| Container  | Docker, Docker Compose   |
| Deployment | GCP or Heroku (optional) |

---

## 🧪 Getting Started

### 🔧 Prerequisites

- Java 17+
- Maven (`mvn`)
- Docker & Docker Compose
- (Optional) Postman for testing APIs

---

### 📦 Run PostgreSQL via Docker

```bash
docker-compose up -d
```

This starts a PostgreSQL container with the credentials:

- **DB name**: `splitpay`
- **Username**: `splitpay_user`
- **Password**: `splitpay_pass`
- **Port**: `5432`

---

### ⚙️ Configure Spring Boot

In `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/splitpay
    username: splitpay_user
    password: splitpay_pass
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
```

---

### ▶️ Run the App

Use Maven wrapper (preferred):

```bash
./mvnw spring-boot:run
```

Or with installed Maven:

```bash
mvn spring-boot:run
```

---

### 🛠️ Run Flyway Migrations

Flyway runs automatically on app startup.

Place migration SQL scripts in:

```
src/main/resources/db/migration/
```

Example filenames:

```
V1__init_schema.sql
V2__add_expenses_table.sql
```

---

### 🔍 Access API Documentation (Swagger)

Once the app is running, go to:

```
http://localhost:8080/swagger-ui.html
```

This UI provides:

- Interactive API testing
- Model schemas
- Error codes

---

### 📬 Example Endpoints

| Method | Endpoint             | Description                 |
| ------ | -------------------- | --------------------------- |
| POST   | `/users`             | Create new user             |
| GET    | `/users/{id}`        | Get user info               |
| POST   | `/groups`            | Create group                |
| POST   | `/expenses`          | Add expense to group        |
| GET    | `/groups/{id}/debts` | View simplified debt report |

---

### ✅ Run Tests

```bash
./mvnw test
```

Unit and integration tests will run using **JUnit 5**. Test coverage will grow as features are added.

---

### 🧹 Stop and Clean Docker

```bash
docker-compose down -v
```

This will stop the PostgreSQL container and remove volumes (database data).

---

## 🤝 Contributing

Pull requests are welcome! If you'd like to contribute, feel free to fork this repo, branch out, and submit a PR 🚀

---

## 📄 License

MIT License. © 2025 [Your Name]

---

## 👨‍💻 Author

**[Your Name]**  
🎓 Java Backend Developer | Aspiring Revolut Intern  
🔗 [LinkedIn](https://www.linkedin.com/) | [GitHub](https://github.com/yourusername)
