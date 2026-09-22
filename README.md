# I-Wish

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue.svg)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**I-Wish** is a JavaFX-based wishlist application that allows users to create and manage wish lists, connect with friends, view friends' wish lists, contribute to their wishes, and receive notifications through a custom client-server architecture.

---

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Application Flow](#application-flow)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Demo Flow](#demo-flow)
- [Team](#team)
- [License](#license)

---

## Features

- User Registration & Login
- Friend Management & Friend Requests
- Create and Manage Wish Lists
- Add, Update, and Delete Wish List Items
- View Friends' Wish Lists
- Contribute to Friends' Wishes
- Notifications
- Client-Server Communication using Socket Programming
- MySQL Database Integration
- JavaFX Graphical User Interface with Custom CSS Styling

---

## Technologies

| Category | Technology |
|---|---|
| Language | Java |
| UI Framework | JavaFX |
| Build Tool | Maven |
| Database | MySQL |
| Networking | Socket Programming |
| Data Transfer | Object Serialization |
| Styling | CSS |

---

## Project Structure

```text
I-Wish/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/example/
│       │       ├── client/       # Client-side networking logic
│       │       ├── database/     # Database connection and DAO layer
│       │       ├── gui/          # JavaFX screens and UI
│       │       ├── model/        # Application models/entities
│       │       ├── network/      # Shared request/response classes
│       │       ├── server/       # Server-side request handling
│       │       └── service/      # Business logic
│       │
│       └── resources/
│           └── AppStyle.css
│
├── database/
│   ├── schema.sql
│   └── backup.sql
│
├── pom.xml
└── README.md
```

---

## Application Flow

The application follows a client-server architecture where the JavaFX client communicates with the server through socket connections.

```text
User
  │
  ▼
JavaFX GUI
  │
  ▼
Network Client
  │
  ▼
Socket Connection
  │
  ▼
Server
  │
  ├── Authentication
  ├── Friend Management
  ├── Wish List Management
  └── Notifications
  │
  ▼
Services / DAOs
  │
  ▼
MySQL Database
```

---

## Prerequisites

Before running the project, make sure you have the following installed:

- **JDK 17+**
- **Maven 3.8+**
- **MySQL Server 8.0+**
- An IDE such as **IntelliJ IDEA** or **NetBeans**

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/I-Wish.git
cd I-Wish
```

### 2. Set Up the Database

Create the required MySQL database and import the provided schema:

```bash
mysql -u <username> -p <database_name> < database/schema.sql
```

If a database backup is provided, it can be imported using:

```bash
mysql -u <username> -p <database_name> < database/backup.sql
```

### 3. Configure the Database Connection

Update the database connection settings in the database configuration class located under:

```text
src/main/java/org/example/database/
```

Configure the required:

- Database URL
- Database name
- Username
- Password
- Port

### 4. Build the Project

Run:

```bash
mvn clean install
```

### 5. Start the Server

Start the server before running the client:

```bash
mvn exec:java -Dexec.mainClass="org.example.server.Server"
```

### 6. Run the Client

Run the JavaFX application:

```bash
mvn javafx:run
```

### 7. Start Using I-Wish

Register a new account, log in, add friends, create a wish list, and start sharing and contributing to wishes.

---

## Demo Flow

The following flow demonstrates the main functionality of the application:

```text
Register
   ↓
Login
   ↓
Add Friend
   ↓
Accept Friend Request
   ↓
View Friend
   ↓
View Friend's Wish List
   ↓
Select a Wish
   ↓
Contribute
   ↓
Receive Notification
```

---

## Team

| Name | Role | Responsibilities |
|---|---|---|
| **Ahmed Mohammed** | Networking | Client-server architecture, socket programming |
| **Salah Sader** | Networking | Request/response communication, server-side request handling |
| **Sara Ashraf** | GUI | JavaFX screens, user interaction, navigation |
| **Rana Kamal** | GUI | Application screens, UI styling |
| **Ahmed Elsayed** | Design & Database | System design, UML design, database schema |
| **Nouran Read** | Design & Database | MySQL database, data management |

---

## License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for more information.

---

**I-Wish — Make wishes. Share them. Make them happen.**
