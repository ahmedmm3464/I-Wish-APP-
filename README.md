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

-  User Registration & Login
-  Friend Management & Friend Requests
-  Create and Manage Wish Lists
-  Add, Update, and Delete Wish List Items
   View Friends' Wish Lists
-  Contribute to Friends' Wishes
-  Notifications
-  Client-Server Communication using Socket Programming
-  MySQL Database Integration
-  JavaFX Graphical User Interface with Custom CSS Styling

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
