# Personalio — Wish List API

Spring Boot backend for a modern, multi-user **Wish List / Gift Registry** platform. Users create personal or event-based wish lists, add products (manually or via URL scraping), share lists, and let others reserve or mark items as purchased.

The project demonstrates clean architecture, complex domain logic, integrations, real-time updates, and production-grade practices.

## Getting Started

```bash
./mvnw spring-boot:run
```

Copy `.env.example` to `.env` and adjust database settings before running locally.

---

## Project Vision

A platform where users can:

- Create wish lists (Personal, Birthday, Wedding, Baby Shower, etc.)
- Add items with rich metadata and optional product URL scraping
- Share lists publicly, privately, or via unique link
- Let viewers reserve items (“I’ll buy this”) and mark purchases without double-gifting

---

## Functional Requirements

### Core MVP Features

#### User Management

- Registration, login (JWT or OAuth2 — Google/Apple)
- Profile management

#### Wish List Management

- Create multiple wish lists (Personal, Birthday, Wedding, Baby Shower, etc.)
- Add/Edit/Delete items with: name, description, price, quantity, priority, image URL, purchase URL
- Categories / tags
- Set list as public, private, or shared via link

#### Item Addition

- Manual entry
- Paste product URL (basic scraping — title, price, image using Jsoup)

#### Collaboration & Gifting

- Share list via unique link or email invitation
- Viewers can **reserve** an item (mark as “I’ll buy this”)
- Mark item as purchased (by owner or with permission)
- Prevent double-gifting with optimistic locking or reservations

#### Search & Discovery

- Search own lists and public lists (if enabled)

### Nice-to-Have (Phase 2)

- Notifications (email or in-app) when item is reserved/purchased
- Wish list analytics (progress bar — % fulfilled, total value)
- Comments on items
- Multiple currencies
- Export as PDF / printable version
- Integration with price tracking (optional)

---

## Non-Functional Requirements

| Category           | Requirement                                      | Target / Notes                              |
|--------------------|--------------------------------------------------|---------------------------------------------|
| **Performance**    | Page load < 300ms, API responses < 150ms        | Heavy use of caching                        |
| **Scalability**    | Support 1000+ concurrent viewers on popular lists| Design for horizontal scaling               |
| **Security**       | Protect private lists, prevent unauthorized reservations, URL validation | OWASP practices                             |
| **Data Integrity** | Atomic reservation/purchase, no race conditions  | Transactions + locking                      |
| **Usability**      | Responsive UI, clean mobile experience           | Thymeleaf or simple React/Vue               |
| **Observability**  | Metrics, logs, health checks                     | Actuator + Micrometer                       |

---

## User Roles & Stories

### Regular User

- Create & manage own wish lists
- Add items, share lists
- View shared lists and reserve items

### List Viewer / Friend

- View shared/public list
- Reserve or purchase items
- Cannot edit list

### List Owner

- Full control + can approve/cancel reservations

---

## Technical & Architecture Requirements

- **Backend**: Spring Boot 3.x + Java 21/23
- **Architecture**: **Hexagonal (Ports & Adapters)** or **Clean Architecture**
- **Database**: PostgreSQL

### Key Features to Showcase

- Domain-Driven Design elements (Aggregates, Domain Events)
- Optimistic/Pessimistic locking for reservations
- Spring Events or simple messaging for notifications
- Caching (Redis or Caffeine) for popular lists
- Web Scraping service (Jsoup + retry + sanitization)
- Background jobs (`@Scheduled` or Quartz) for cleanup/price updates

### Testing

- Unit + Integration tests (Testcontainers)
- Domain model tests
- Contract testing for external scraping

### DevOps

- Docker + docker-compose (app + Postgres + Redis)
- OpenAPI / Swagger
- GitHub Actions CI

---

## Core Domain Model

### User

Account and profile for list owners and viewers.

### WishList (Aggregate Root)

- `id`, `title`, `description`, `eventDate`, `visibility`, `ownerId`, `shareToken`

### WishListItem (Entity inside WishList)

- `id`, `name`, `description`, `price`, `currency`, `quantityDesired`, `quantityReserved`, `imageUrl`, `productUrl`, `priority`, `status` (`AVAILABLE`, `RESERVED`, `PURCHASED`)
- Reservation info (`reservedByUserId`, `reservedAt`)

### Reservation (optional)

Separate entity for reservation history and audit.

---

## API Endpoints (REST)

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/wishlists` | Create new wish list | Required |
| GET | `/api/wishlists` | List my wish lists | Required |
| GET | `/api/wishlists/{id}` | Get list details | Required or share token |
| POST | `/api/wishlists/{id}/items` | Add item | Owner |
| POST | `/api/wishlists/{id}/items/{itemId}/reserve` | Reserve item | Viewer |
| POST | `/api/wishlists/{id}/items/{itemId}/purchase` | Mark as purchased | Owner/Viewer |
| GET | `/shared/{shareToken}` | Public/shared list view | No |

---

## Project Structure (Hexagonal)

```
com.example.wishlist
├── domain/
│   ├── model/          (WishList, WishListItem, User)
│   ├── port/           (in/out ports)
│   └── service/        (domain services)
├── application/
│   ├── dto/
│   └── usecase/        (orchestrators)
├── infrastructure/
│   ├── persistence/
│   ├── scraping/
│   └── security/
├── interfaces/         (controllers, mappers)
└── config/
```

---

## Senior-Level Focus

- Strong emphasis on **domain modeling** and **concurrency safety** (reservations are the core technical depth)
- Responsive frontend (Thymeleaf + Tailwind or simple React/Vue)
- Public deployment (Railway / Render / Fly.io)
- Architecture Decision Records (ADR) in the repo

---

## Next Steps

1. Define MVP scope (auth + wish lists + items + share/reserve/purchase)
2. Draw **C4 Context & Container** diagrams
3. Design the **domain model**, aggregates, and ports
4. Set up project skeleton (Spring Initializr + Docker + Postgres + Redis)
5. Implement wish list CRUD and reservation flow first (critical path with locking)

### Optional Deep-Dives

- Detailed **database schema** (SQL)
- **Domain model** classes with code examples
- Full **package structure** + key interfaces
- **Reservation flow** with concurrency handling
- OpenAPI examples or ER diagram
- ADRs for architecture choices
