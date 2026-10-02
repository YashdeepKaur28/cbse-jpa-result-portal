# CBSE Result Portal — JPA + Servlets + EclipseLink 🎓

A **full-stack Java web application** for managing CBSE student results, built with **JPA (EclipseLink), Servlets, JDBC, Oracle XE, and JavaMail**, and deployed on **Oracle WebLogic Server**.

> 💡 **Note:** This is the **JPA refactor** of my earlier JDBC-based CBSE Result Portal. See [cbse-result-portal](#) for the original JDBC + JNDI version. Both implement the same functionality — this one replaces raw JDBC with a proper ORM layer.

---

## 📖 Overview

The **CBSE Result Portal (JPA Edition)** is a web application that lets administrators manage student records and results while students securely view their own data. Unlike the original JDBC version, this build uses **JPA with EclipseLink** for object-relational mapping, giving cleaner persistence code and better maintainability.

**Built to practice:**
- **JPA / EclipseLink** — `@Entity`, `@Table`, `EntityManager`, `TypedQuery`, transactions
- **Servlet MVC** — servlets as controllers, HTML as views
- **Session & cookies** — login state, "Remember Me"
- **JavaMail** — registration confirmation emails
- **Oracle XE** — as the persistence layer
- **WebLogic deployment** — WAR packaging with `weblogic.xml` for context-root

---

## ✨ Features

- 🔐 **Role-based login** with session management and cookie-based "Remember Me"
- ➕ **Add / Update / Delete / Search** students (full CRUD)
- 🧮 **Automatic total, percentage, and grade calculation** (`GradeUtil`)
- 📊 **Display all students** in a formatted table
- 📥 **Excel export** — all students or a single result card
- 📧 **Email notifications** on registration (JavaMail)
- 🗄️ **JPA entities** for `Student` and `User`
- 🛡️ **Server-side validation** — marks restricted to 0–100
- 🚨 **Duplicate ID handling** — catches `ORA-00001` and shows a friendly message
- 🧩 **Clean persistence layer** via `JPAUtil` singleton `EntityManagerFactory`

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java (JDK 1.6 — matches WebLogic 12.1.1) |
| Web | Servlets, HTML, CSS |
| ORM | **JPA 2.0 (EclipseLink)** |
| Database | Oracle XE |
| Server | Oracle WebLogic Server 12.1.1 |
| Email | JavaMail |
| Build | Manual (`build.bat`) + `jar` |

---

## 🗄️ Database Schema

The schema is created manually (no auto-DDL — `eclipselink.ddl-generation` is set to `none`):

```sql
CREATE TABLE users (
    username VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(100) NOT NULL,
    email    VARCHAR2(100) NOT NULL
);

CREATE TABLE cbse_result (
    student_id    NUMBER PRIMARY KEY,
    name          VARCHAR2(100) NOT NULL,
    class         VARCHAR2(20) NOT NULL,
    math_marks    NUMBER,
    science_marks NUMBER,
    english_marks NUMBER,
    hindi_marks   NUMBER,
    sst_marks     NUMBER,
    total_marks   NUMBER,
    percentage    NUMBER,
    grade         VARCHAR2(5)
);
```

---

## ⚙️ Setup & Deployment

### Prerequisites
- **JDK 1.6** (bundled with WebLogic 12.1.1 at `C:\Oracle\Middleware\jdk160_29`)
- **Oracle WebLogic Server 12.1.1** (`C:\Oracle\Middleware\wlserver_12.1`)
- **Oracle XE** running on `localhost:1521`
- **EclipseLink JAR** + **Oracle JDBC driver (ojdbc6)** + **JavaMail** in `WEB-INF/lib`

### Step 1 — Create Oracle user and schema

```sql
-- As SYSDBA
CREATE USER jpa IDENTIFIED BY your_strong_password;
GRANT CONNECT, RESOURCE TO jpa;

-- Connect as jpa
CREATE TABLE users (...);  -- see schema above
CREATE TABLE cbse_result (...);
```

### Step 2 — Edit `persistence.xml`

Open `web/WEB-INF/classes/META-INF/persistence.xml` and set:

```xml
<property name="javax.persistence.jdbc.user"     value="jpa"/>
<property name="javax.persistence.jdbc.password" value="your_strong_password"/>
```

### Step 3 — Set email credentials as environment variables

**Do NOT hardcode in `Email.java`.** Instead:

```cmd
set MAIL_USER=yash20133@gmail.com
set MAIL_PASS=your_16_char_app_password
```

`Email.java` should read them via `System.getenv("MAIL_USER")`.

### Step 4 — Build the WAR

```cmd
build.bat
```

This compiles all Java sources to `web/WEB-INF/classes/` and produces `JpaWebsite.war`.

### Step 5 — Deploy to WebLogic

1. Start WebLogic (`startWebLogic.cmd`)
2. Open the Admin Console: `http://localhost:7001/console`
3. **Deployments → Install** → select `JpaWebsite.war`
4. Start the deployment

### Step 6 — Access the app

```
http://localhost:7002/JpaWebsite/
```

Register a user, log in, and manage students.

---

## 🧮 Grade Calculation Logic

`GradeUtil.java` computes total, percentage, and grade:

| Percentage | Grade |
|-----------|-------|
| ≥ 91 | A1 |
| ≥ 81 | A2 |
| ≥ 71 | B1 |
| ≥ 61 | B2 |
| ≥ 51 | C1 |
| ≥ 41 | C2 |
| ≥ 33 | D |
| < 33 | E |

---

## 🔗 Servlet Mappings

| URL | Servlet | Purpose |
|-----|---------|---------|
| `/add` | `AddStudentServlet` | Add student |
| `/delete` | `DeleteStudentServlet` | Delete by ID |
| `/update` | `UpdateStudentServlet` | Update marks |
| `/search` | `SearchStudentServlet` | Search by ID |
| `/displayall` | `DisplayAllServlet` | List all students |
| `/excel` | `ExcelServlet` | Export to `.xls` |
| `/login` | `LoginServlet` | Login |
| `/register` | `RegisterServlet` | Register new user |
| `/logout` | `LogoutServlet` | Invalidate session |

---

## 🧠 Key Concepts Demonstrated

- **JPA ORM** — `@Entity`, `@Table`, `@Id`, `@Column`, `EntityManager`, `EntityTransaction`
- **JPQL queries** — `TypedQuery<Student>` and `TypedQuery<User>` with parameter binding
- **EclipseLink-specific config** — provider, target database, logging level
- **Persistence unit lifecycle** — singleton `EntityManagerFactory` in `JPAUtil`
- **Transactional persistence** — `tx.begin()`, `tx.commit()`, `tx.rollback()`
- **Exception unwrapping** — walking `e.getCause()` to surface `ORA-00001` as a user-friendly message
- **Servlet + Session + Cookies** — auth flow with remember-me
- **JavaMail** — registration confirmation
- **Maven-less build** — a manual `build.bat` that compiles, packages, and inspects the WAR

---

## 🚀 Future Enhancements

- [ ] Migrate to **Maven or Gradle** (currently manual `build.bat`)
- [ ] Auto-DDL via `eclipselink.ddl-generation=create-tables` in dev mode
- [ ] Add **BCrypt password hashing** — currently stored plaintext
- [ ] Replace `PrintWriter` HTML generation with **JSP views**
- [ ] Add **JUnit tests** for `GradeUtil` and JPA queries
- [ ] Containerize with **Docker** (WebLogic + Oracle)
- [ ] Migrate to **Spring Boot** for a modern stack

---

## ⚠️ Known Limitations

- **Password hashing missing** — `User.password` stored plaintext
- **No connection pooling config** — relies on EclipseLink defaults
- **Schema must be created manually** — auto-DDL disabled
- **JDK 1.6 target** — modern environments should target JDK 8+

Noted honestly as learning-stage design choices.

---

## 👩‍💻 Author

**Yashdeep Kaur**
- 🎓 B.Tech CSE, Punjabi University, Patiala (2026)
- 💼 Java Full Stack Trainee @ CodeSquadz
- 📧 ykdeep2453@gmail.com
- 🔗 [LinkedIn](https://linkedin.com/in/yashdeep-kaur-16aa083b1)
- 🐙 [@YashdeepKaur28](https://github.com/YashdeepKaur28)

---

⭐ If you found this useful, consider giving it a star!
