# Collaborative To-Do List Application

A command-line collaborative to-do list supporting multiple concurrent users. Tasks support categorization, status tracking, assignees, and real-time-safe concurrent access via async/await and a Mutex lock.

## Features

- **User management**: Registration, authentication, user-specific task views
- **Task CRUD**: Create, read, update, delete with category and status
- **Categories**: Work, Personal, Shopping, Health, Other (extensible)
- **Status**: Pending, In Progress, Completed
- **Concurrency**: Async/await, Promise-based I/O, Mutex for critical sections
- **Persistence**: JSON file storage for tasks and users

## Project Structure

**JavaScript (Node)** — `src/` and `data/`:

```
src/
  Category.js      - Category constants
  TaskStatus.js    - Status constants
  User.js          - User model and validation
  Task.js          - Task model and validation
  Mutex.js         - Queue-based lock for critical sections
  TaskRepository.js - Async JSON file I/O
  TaskManager.js   - Async task CRUD and filtering
  UserManager.js   - Async auth and user operations
  index.js         - CLI menu entry point
data/              - Created at runtime (tasks.json, users.json)
```

**Java** — same design under `java/`:

```
java/
  pom.xml
  src/main/java/com/enose/todo/
    Category.java, TaskStatus.java, User.java, Task.java
    Mutex.java, TaskRepository.java, TaskManager.java, UserManager.java
    Result.java, Main.java
  data/   - Created when run from java/ (tasks.json, users.json)
```

See [java/README.md](java/README.md) for Java build and run.

## Setup

```bash
npm install   # No external deps; Node.js built-ins only
npm start     # Run CLI
```

## Usage

Run `npm start` and follow the CLI menu to:

- Register / login
- Create, list, update, delete tasks
- Filter by category, status, or assignee
- Simulate concurrent operations (optional)

Requires **Node.js 18+** (for native `fs.promises` and modern syntax).

## Testing

- **JavaScript:** `npm test` (Node built-in test runner; see `tests/*.test.js`).
- **Java:** `cd java && mvn test` (JUnit 5; see `java/src/test/java/com/enose/todo/*Test.java`).

## Deliverables (Day 2–3)

- **Comparison report (APA 7):** [docs/ComparisonReport_JavaScript_Java.md](docs/ComparisonReport_JavaScript_Java.md) — 2–3 page comparison of JavaScript vs Java implementation with code snippets.
- **Presentation (5–10 min):** [docs/Presentation_Outline.md](docs/Presentation_Outline.md) — Outline for demo and comparison walkthrough.
- **GitHub:** Add your repository link to the report and presentation when published.
