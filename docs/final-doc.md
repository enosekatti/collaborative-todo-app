# Design Plan - Collaborative To-Do List Application
## Deliverable 1: Initial Planning and Design

---

## 1. Application Design

### Overview
A command-line and GUI-based collaborative to-do list application supporting multiple concurrent users. Users can create, manage, and organize tasks with categorization, status tracking, and real-time concurrent access.

### Core Requirements Mapping

#### Requirement 1: Data Storage & User-Specific Views
- Persistent data storage for tasks and users
- User-specific task filtering and retrieval
- Support for viewing assigned and category-filtered tasks

#### Requirement 2: Task Categorization & Status Tracking
- **Categories**: Work, Personal, Shopping, Health, Other (extensible)
- **Status States**: Pending, In Progress, Completed
- **Task Properties**: Title, Description, Category, Status, Priority, Assignee, Timestamps

#### Requirement 3: Concurrent Access Support
- Thread-safe operations (Java)
- Async/Await patterns (JavaScript)
- Lock mechanisms to prevent data corruption

---

## 2. Component Breakdown & Architecture

### Core Application Components
1. **User Management**: Authentication, profiles, and user registration
2. **Task Management**: CRUD operations with categorization
3. **Category Management**: Task categorization system
4. **Status Management**: Task lifecycle tracking
5. **Concurrency Control**: Thread-safe and async-safe operations
6. **Data Persistence**: File/JSON storage and retrieval
7. **User Interface**: CLI-based menu system for user interaction

### Language-Specific Implementation Differences

#### Java Implementation (Object-Oriented + Threading)
**Architecture Pattern**: Object-Oriented Design with Thread-based Concurrency

**Key Components**:
| Component | Purpose | Features |
|-----------|---------|----------|
| `User` class | User entity | userId, username, email, createdAt, updatedAt |
| `Task` class | Task entity | taskId, title, description, category, status, priority, assigneeId, timestamps |
| `Category` enum | Task categories | WORK, PERSONAL, SHOPPING, HEALTH, OTHER |
| `TaskStatus` enum | Task lifecycle states | PENDING, IN_PROGRESS, COMPLETED |
| `TaskManager` class | Task operations | CRUD with thread-safe filtering by category, status, and assignee |
| `UserManager` class | User operations | Registration, authentication, profile management |
| `TaskRepository` class | Data persistence | JSON serialization/deserialization, file I/O |
| `Main` class | User interface | CLI menu system, command processing |

**Concurrency Mechanism**:
- `ReentrantReadWriteLock` for TaskManager operations (multiple readers, single writer)
- `synchronized` keyword for critical sections in UserManager
- Thread-safe collections: `ConcurrentHashMap` (task storage), `CopyOnWriteArrayList` (user list)
- `ExecutorService` for simulating concurrent user access

**Language-Specific Advantages**:
- Strong compile-time type checking prevents errors early
- Built-in threading support with explicit lock mechanisms
- Native JSON libraries (Jackson/Gson) simplify serialization
- Object-oriented structure naturally maps to domain entities

**Anticipated Challenges**:
- Managing multiple threads and preventing deadlocks
- Verbose boilerplate code for data models
- File I/O blocking operations (mitigated with thread pooling)
- Complex lock ordering to avoid circular deadlocks

---

#### JavaScript Implementation (Functional + Async/Await)
**Architecture Pattern**: Functional/Modular Design with Async/Await Concurrency

**Key Components**:
| Component | Purpose | Features |
|-----------|---------|----------|
| `User.js` | User module | User object, profile methods, user validation |
| `Task.js` | Task module | Task object, task methods, task validation |
| `Category.js` | Category constants | WORK, PERSONAL, SHOPPING, HEALTH, OTHER definitions |
| `TaskStatus.js` | Status constants | PENDING, IN_PROGRESS, COMPLETED definitions |
| `TaskManager.js` | Task operations | Async CRUD, filtering by category/status/assignee, Promise-based |
| `UserManager.js` | User operations | Async authentication, registration, profile management |
| `TaskRepository.js` | Data persistence | Async JSON file I/O using fs.promises |
| `index.js` | User interface | Async CLI menu system, concurrent user simulation |

**Concurrency Mechanism**:
- Promises and async/await for non-blocking I/O operations
- Promise.all() for parallel concurrent operations
- Custom Mutex class for critical section protection (queue-based locking)
- Event-driven architecture for handling concurrent requests

**Language-Specific Advantages**:
- Native async/await simplifies concurrent operation syntax
- JSON is native data format (no serialization overhead)
- Lightweight and flexible object structures
- Excellent for I/O-bound operations (file handling)
- Non-blocking event loop naturally handles concurrency

**Anticipated Challenges**:
- Lack of compile-time type checking (mitigated with JSDoc comments)
- Callback complexity in nested operations (mitigated by async/await)
- Simulating true mutual exclusion without native threading
- Managing promise rejection and error handling across async chains
- Race conditions with asynchronous operations

---

## 3. Data Model

### Task Schema
```json
{
  "taskId": "string (UUID)",
  "title": "string",
  "description": "string",
  "category": "WORK | PERSONAL | SHOPPING | HEALTH | OTHER",
  "status": "PENDING | IN_PROGRESS | COMPLETED",
  "priority": "LOW | MEDIUM | HIGH",
  "assigneeId": "string (userId)",
  "createdAt": "ISO 8601 timestamp",
  "updatedAt": "ISO 8601 timestamp"
} 

### User Schema
"userId": "string (UUID)",
"username": "string",
"email": "string",
"createdAt": "ISO 8601 timestamp",
"updatedAt": "ISO 8601 timestamp"

## Category System Definition
WORK: Professional tasks, meetings, deadlines, projects
PERSONAL: Personal errands, hobbies, self-improvement
SHOPPING: Shopping lists, purchases, vendor research
HEALTH: Health appointments, fitness, medical tasks
OTHER: Miscellaneous tasks, general purpose

## Status Transition Rules
PENDING -> IN_PROGRESS -> COMPLETED
PENDING -> COMPLETED (skip in progress)
COMPLETED -> PENDING (reopen task)
IN_PROGRESS -> PENDING (pause task)

## 5. Task Assignment & Roles

Member 1: Basanta Ghorashainee
Role: Java Implementation Lead

Primary Responsibilities:

Design and implement User class with validation
Design and implement Task class with all properties
Create Category enum with category definitions
Create TaskStatus enum with status definitions
Develop TaskManager class with:
Thread-safe task CRUD operations
Category-based filtering
Status-based filtering
Assignee-based filtering
ReentrantReadWriteLock implementation
Develop UserManager class with:
User registration
User authentication
Profile management
User lookup and filtering
Implement TaskRepository with:
JSON serialization (Jackson/Gson)
File I/O operations
Data loading and saving
Develop Main class CLI interface with:
Menu-driven navigation
User input handling
Command processing
Write unit tests for Java components
Implement concurrency tests using ExecutorService
Documentation of Java API and usage
Skills Leveraged: Object-oriented design, threading, Java collections, JSON serialization

Member 2: Enose Katti
Role: JavaScript Implementation Lead

Primary Responsibilities:

Design and implement User module with validation
Design and implement Task module with all properties
Create Category constants module
Create TaskStatus constants module
Develop custom Mutex class for mutual exclusion
Develop TaskManager class with:
Async task CRUD operations
Category-based filtering
Status-based filtering
Assignee-based filtering
Promise.all() concurrent operations
Develop UserManager class with:
Async user registration
Async user authentication
Async profile management
User lookup and filtering
Implement TaskRepository with:
Async JSON file I/O (fs.promises)
Data loading and saving
Error handling for file operations
Develop index.js CLI interface with:
Async menu system
User input handling
Concurrent user simulation
Promise-based command processing
Write unit tests for JavaScript components using Jest
Implement async concurrency tests
Documentation of JavaScript API and usage
Skills Leveraged: Asynchronous programming, Promises, Node.js file system, functional programming patterns


---

## Project Overview

| Phase | Day | Focus | Java | JavaScript | Status |
|-------|-----|-------|------|-----------|--------|
| **Phase 1** | Day 1 | Setup & Data Models | Entity Classes + Enums | Modules + Constants | ⏳ |
| **Phase 2** | Day 2 | Business Logic | Managers + Repository | Async Managers + Repository | ⏳ |
| **Phase 3** | Day 3 | Concurrency & UI | Thread-safe CLI App | Async-safe CLI App | ⏳ |

---

## Key Milestones

- **End of Day 1**: ✅ Data models complete, GitHub repo ready
- **End of Day 2**: ✅ Core business logic functional, persistence working
- **End of Day 3**: ✅ Full application with concurrency, testing, documentation

---

# Anticipated Challenges by Language & Feature

## Java Challenges

### User Management
- **Concurrent Registration**: Use synchronized blocks to prevent duplicate users
- **Thread-Safe Authentication**: ReentrantReadWriteLock for user lookups

### Task Management
- **Concurrent CRUD**: ConcurrentHashMap + ReentrantReadWriteLock for atomicity
- **Data Corruption**: Implement copy-on-write for task updates

### Task Categorization
- **Concurrent Filtering**: CopyOnWriteArrayList to prevent ConcurrentModificationException
- **Category Validation**: Enum type safety with fail-fast validation

### Task Status Tracking
- **Invalid Transitions**: Validate transitions under write lock before updating
- **Status Consistency**: Use synchronized blocks for status change operations

### Concurrency Control
- **Deadlock Prevention**: Lock ordering hierarchy (UserManager → TaskManager → Repository)
- **Reader/Writer Starvation**: ReentrantReadWriteLock with fairness enabled
- **Lock Timeout**: Use tryLock(timeout) to prevent indefinite blocking

### Data Persistence
- **File I/O Blocking**: ExecutorService for async file operations
- **Concurrent Writes**: Atomic writes using temp file + rename pattern
- **Data Consistency**: Create snapshots before serialization

### User Interface
- **Console Output Corruption**: synchronized blocks for println operations
- **UI Responsiveness**: Offload long operations to ExecutorService

---

## JavaScript Challenges

### User Management
- **Race Conditions in Async Registration**: Implement Mutex for registration critical section
- **Promise Rejection Handling**: Add .catch() blocks and unhandledRejection handlers

### Task Management
- **Concurrent Async Updates**: Lock with Mutex before modifying shared task state
- **Promise Sequencing**: Use async/await with proper error handling

### Task Categorization
- **Array Modification During Filter**: Create snapshot of array before filtering
- **Invalid Categories**: Object.freeze() for constants with runtime validation

### Task Status Tracking
- **Race Conditions in Status Transitions**: Mutex lock for status update operations
- **Invalid State Changes**: Validate transitions under lock with VALID_TRANSITIONS map

### Concurrency Control
- **Simulating Mutual Exclusion**: Custom Mutex class with queue-based locking
- **Promise Race Conditions**: Promise.all() with sequential mutex acquisition
- **Timeout Management**: Implement timeout in mutex lock with Promise.race()

### Data Persistence
- **Async File I/O Errors**: Handle ENOENT, EACCES, and SyntaxError separately
- **Concurrent File Writes**: Mutex lock + atomic writes (temp file + fs.promises.rename)
- **Memory Issues**: Load data in batches with setImmediate() for garbage collection

### User Interface
- **Unresponsive CLI**: Use setImmediate() to allow event loop processing
- **Console Output Garbling**: BufferQueue for ordered console output with delays

