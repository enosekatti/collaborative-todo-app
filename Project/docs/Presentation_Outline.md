# Presentation Outline: Collaborative To-Do (JavaScript vs. Java)
## 5–10 Minute Walkthrough

**For full 20-minute read-aloud speaker notes, see [Presentation_Speaker_Notes_20min.md](Presentation_Speaker_Notes_20min.md).**

Use this outline for your live presentation. Demo both applications and highlight the comparison points below.

---

### Slide 1: Title and Overview (≈30 sec)
- **Title:** Collaborative To-Do Application: JavaScript and Java Implementations
- **What we built:** CLI to-do app with user auth, task CRUD, categories (Work, Personal, Shopping, Health, Other), status (Pending, In Progress, Completed), and filters (by category, status, assignee).
- **Repo:** [Add your GitHub repository link here]

---

### Slide 2: Architecture (Same in Both) (≈45 sec)
- **Components:** User, Task, Category, TaskStatus, Mutex, TaskRepository, TaskManager, UserManager, CLI (index.js / Main.java).
- **Flow:** CLI → UserManager/TaskManager → Mutex → TaskRepository → JSON files (tasks.json, users.json).
- **Concurrency:** Mutex protects file read/write so concurrent access does not corrupt data.

---

### Slide 3: Demo – JavaScript (≈2 min)
1. Open terminal; run `npm start` from project root.
2. **Register:** Choose 1 → enter username (e.g. `alice`), password (4+ chars), optional display name/email.
3. **Login:** Choose 2 → same credentials.
4. **Create task:** Choose 5 → title, category (e.g. WORK), priority, optional assignee.
5. **List:** Choose 1 (my tasks), then 2 (by category), then 4 (assigned to me).
6. **Update/Delete:** Choose 6 (update task by ID), 7 (delete by ID).
7. **Logout:** 8; **Exit:** 0.

*Mention:* “Same menu and behavior in Java; we’ll run that next.”

---

### Slide 4: Demo – Java (≈2 min)
1. `cd java` then `mvn compile` and `mvn exec:java -q -Dexec.mainClass="com.enose.todo.Main"` (or `java -jar target/collaborative-todo-1.0.0.jar`).
2. Repeat the same flow: Register → Login → Create task → List (my tasks, by category, assigned to me) → Update/Delete → Logout → Exit.
3. *Note:* Data is in `java/data/`; JS uses project root `data/`. Same JSON structure.

---

### Slide 5: Key Code Differences (≈2 min)

**Concurrency**
- **JS:** Promise-based mutex; `async/await` for non-blocking I/O.
- **Java:** `ReentrantLock`; synchronous I/O with `Files` API.

**Data & types**
- **JS:** Plain objects, `Object.freeze` for constants, spread for updates.
- **Java:** `final` classes, `List.of()`, `Optional<T>`, `Result<T>`.

**Async vs sync**
- **JS:** `async function loadTasks()` and `await readJson(...)`.
- **Java:** `List<Task> loadTasks() throws IOException`.

Show one short snippet from each (e.g. Mutex or loadTasks) from the comparison report if the audience is technical.

---

### Slide 6: Testing (≈1 min)
- **JavaScript:** `npm test` — Node built-in test runner; tests for Category, TaskStatus, User, Task.
- **Java:** `mvn test` — JUnit 5; same modules tested.
- Both verify validation, constants, and immutable updates.

---

### Slide 7: Summary and Takeaways (≈1 min)
- **Same features** in both: register/login, CRUD tasks, categories, status, filters, mutex-protected storage.
- **JS:** Fast to write, async-native, minimal boilerplate; less static safety.
- **Java:** Strong typing, clear concurrency (locks), more boilerplate; good for larger teams.
- **Report:** 2–3 page comparison in `docs/ComparisonReport_JavaScript_Java.md` (APA 7 style).

---

### Q&A
- Be ready to run either app again or show a specific file (e.g. Mutex, TaskManager, or a test file) if asked.

---

**Timing tip:** If you have only 5 minutes, do one short demo (e.g. JS only), then show the comparison slide and summary. If you have 10 minutes, do both demos and the code-difference slide.
