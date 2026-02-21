# Collaborative To-Do (Java)

Same application as the JavaScript version: CLI to-do list with user management, task CRUD, categories, status, and mutex-protected JSON storage.

## Build, Test, Run

- **Java 17+** required. You do **not** need Maven installed — use the Maven Wrapper (`./mvnw`) in this directory.

```bash
cd java
mvn compile
mvn test
mvn exec:java -q -Dexec.mainClass="com.basanta.todo.Main"

```

Or run the JAR:

```bash
./mvnw package
java -jar target/collaborative-todo-1.0.0.jar
```

*(If you have Maven installed, you can use `mvn` instead of `./mvnw`.)*

Run from the `java/` directory so `data/` is created there.

## Structure

`src/main/java/com/enose/todo/` — Category, TaskStatus, User, Task, Mutex, Result, TaskRepository, TaskManager, UserManager, Main.  
`src/test/java/com/enose/todo/` — JUnit 5 tests for Category, TaskStatus, User, Task.

## Dependencies

- Gson (JSON)
- JUnit Jupiter (test scope)
