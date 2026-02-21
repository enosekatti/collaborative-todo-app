# Design Plan - Collaborative To-Do List Application

## Application Overview
A command-line and GUI-based collaborative to-do list application supporting multiple concurrent users. Users can create, manage, and organize tasks with categorization, status tracking, and real-time concurrent access.

## Core Requirements Mapping

### Requirement 1: Data Storage & User-Specific Views
- Persistent data storage for tasks and users
- User-specific task filtering and retrieval
- Support for viewing assigned and category-filtered tasks

### Requirement 2: Task Categorization & Status Tracking
- **Categories**: Work, Personal, Shopping, Health, Other (extensible)
- **Status States**: Pending, In Progress, Completed
- **Task Properties**: Title, Description, Category, Status, Priority, Assignee, Timestamps

### Requirement 3: Concurrent Access Support
- Thread-safe operations (Java)
- Async/Await patterns (JavaScript)
- Lock mechanisms to prevent data corruption

---

## Application Architecture

### Core Components
1. **User Management**: Authentication, profiles, and user registration
2. **Task Management**: CRUD operations with categorization
3. **Category Management**: Task categorization system
4. **Status Management**: Task lifecycle tracking
5. **Concurrency Control**: Thread-safe and async-safe operations
6. **Data Persistence**: File/JSON storage and retrieval

---

## Language-Specific Implementation

### Java Implementation (Member 1: Enose Katti)
**Architecture Pattern**: Object-Oriented Design with Thread-based Concurrency

**Key Components**:
- `User` class: Represents a user with userId, username, email, and createdAt properties.
- `Task` class: Encapsulates task data including taskId, title, description, category, status, priority, assigneeId, and timestamps.
- `Category` enum: Defines task categories (WORK, PERSONAL, SHOPPING, HEALTH, OTHER).
- `TaskStatus` enum: Defines task lifecycle states (PENDING, IN_PROGRESS, COMPLETED).
- `TaskManager` class: Manages all task operations with thread-safe mechanisms, including filtering by category and status.
- `UserManager` class: Handles user registration, authentication, and profile management.
- `TaskRepository` class: Handles data persistence with JSON serialization for tasks and users.

**Concurrency Strategy**: Use Java's `ReentrantReadWriteLock` for task operations, `synchronized` keyword for critical sections, and thread-safe collections (ConcurrentHashMap, CopyOnWriteArrayList).

### JavaScript Implementation (Member 2: Basanta Ghorashainee)
**Architecture Pattern**: Functional/Modular Design with Async/Await

**Key Components**:
- `User` object: Represents user data with userId, username, email, and createdAt properties, plus methods for profile management.
- `Task` object: Encapsulates task properties including taskId, title, description, category, status, priority, assigneeId, and timestamps.
- `Category` constants: Defines task categories (WORK, PERSONAL, SHOPPING, HEALTH, OTHER).
- `TaskStatus` constants: Defines task lifecycle states (PENDING, IN_PROGRESS, COMPLETED).
- `TaskManager` class: Manages task operations with asynchronous patterns, including filtering by category and status.
- `UserManager` class: Handles user authentication and profile management.
- `TaskRepository` class: Handles data persistence with JSON file storage and async file operations.

**Concurrency Strategy**: Use Promises and async/await for non-blocking operations, Promise.all() for concurrent task execution, and mutex/lock pattern simulation for critical sections.

## Data Model

### User Schema
- userId: string (UUID)
- username: string
- email: string
- createdAt: timestamp
- updatedAt: timestamp

### Task Schema
- taskId: string (UUID)
- title: string
- description: string
- category: WORK | PERSONAL | SHOPPING | HEALTH | OTHER
- status: PENDING | IN_PROGRESS | COMPLETED
- priority: LOW | MEDIUM | HIGH
- assigneeId: string (userId)
- createdAt: timestamp
- updatedAt: timestamp

### Category System
- **WORK**: Professional tasks and projects
- **PERSONAL**: Personal errands and activities
- **SHOPPING**: Shopping lists and purchases
- **HEALTH**: Health-related tasks and appointments
- **OTHER**: Miscellaneous tasks

## Testing Strategy
- Unit tests for individual components
- Integration tests for user and task interactions
- Concurrency tests to ensure thread/async safety