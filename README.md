# Restaurant Management System

Spring Boot 3 (Java 17) backend with a lightweight browser UI. Take orders by table, track them through the kitchen, manage the menu and tables, and see today's revenue.

## Run it

Requirements: JDK 17+ and Maven 3.9+.

    mvn spring-boot:run

Open http://localhost:8080. Sample menu items and 8 tables are added on first start.
Data is stored in `./data/rmsdb` (H2 file database). H2 console: http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:file:./data/rmsdb`, user `sa`, empty password).

## REST API

| Method | Path | Purpose |
|---|---|---|
| GET | /api/menu?category= | List menu (cached) |
| POST / PUT / DELETE | /api/menu, /api/menu/{id} | Create, update, delete dish |
| PATCH | /api/menu/{id}/availability | `{"status":"true"}` or `"false"` |
| GET / POST | /api/tables | List, create table |
| PATCH | /api/tables/{id}/status | `{"status":"AVAILABLE|OCCUPIED|RESERVED"}` |
| DELETE | /api/tables/{id} | Delete table |
| GET | /api/orders?active=true | Open orders (`active=false&size=30` for history) |
| POST | /api/orders | `{"tableId":1,"items":[{"menuItemId":3,"quantity":2}]}` |
| PATCH | /api/orders/{id}/status | NEW, PREPARING, SERVED, PAID, CANCELLED |
| GET | /api/dashboard | Open orders, tables in use, today's revenue |

Order flow: NEW -> PREPARING -> SERVED -> PAID (or CANCELLED at any open stage).
Placing an order marks the table occupied; paying or cancelling the last open order frees it.

## Efficiency choices

- Menu reads are cached and evicted on every menu write.
- One query loads all menu items for an order (no per-line lookups); duplicate lines are merged.
- Order name and price are copied onto each line, so history stays correct and no extra joins are needed.
- Lazy associations, `@EntityGraph` for the table, and Hibernate batch fetching avoid N+1 queries.
- Indexes on order status, table, created time, and menu category.
- Read-only transactions for queries, `open-in-view` disabled, JDBC batching on, response compression on.
- DTOs for orders; validation on all inputs; one global error handler (400 / 404 / 409).

## Switching to MySQL or PostgreSQL

Replace the `h2` dependency in `pom.xml` with your driver and change the `spring.datasource.*` properties in
`src/main/resources/application.properties`. For production, set `spring.jpa.hibernate.ddl-auto=validate` and manage the schema with Flyway or Liquibase.

## Project layout

    src/main/java/com/restaurant/rms
      model/       JPA entities and enums
      repository/  Spring Data repositories
      service/     business logic and transactions
      controller/  REST endpoints
      dto/         request/response records
      exception/   error handling
      config/      sample data loader
    src/main/resources/static/index.html   the UI
