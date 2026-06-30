# Expense Splitter

A Java-based command-line application that helps groups track shared expenses and settle debts efficiently using a **Minimum Cash Flow algorithm**.

## Overview

Splitting expenses among friends, roommates, or trip groups often results in a tangled web of who-owes-whom. This project models that as a graph settlement problem and computes the **minimum number of transactions** needed to settle all debts within a group, rather than naively having every debtor pay every creditor individually.

## Features

- Create and manage users and groups
- Add and split expenses among group members
- Automatic calculation of net balances per user
- Debt settlement using a Minimum Cash Flow algorithm (greedy approach with a max-heap / PriorityQueue of creditors and debtors)
- Persistent storage using Gson (JSON-based) — data survives across sessions

## Architecture

The project follows a layered, Single Responsibility Principle (SRP) design:

src/
├── Main.java                    Entry point, CLI interaction
├── model/
│   ├── User.java                 User entity
│   ├── Group.java                Group entity
│   ├── Expense.java              Expense entity
│   └── Split.java                Represents a user's share in an expense
└── service/
    ├── UserService.java          User-related operations
    ├── GroupService.java         Group-related operations
    ├── ExpenseService.java       Expense creation & tracking
    ├── SettlementService.java    Core settlement logic (Min Cash Flow)
    └── DataStore.java            Gson-based JSON persistence layer

Each service handles a single concern, keeping business logic decoupled from data persistence and CLI interaction.

## Algorithm: Minimum Cash Flow

Instead of settling every individual expense between every pair of users, the app:

1. Computes each user's net balance (total paid minus total owed) within a group.
2. Separates users into creditors (positive balance) and debtors (negative balance).
3. Uses a greedy approach with PriorityQueues to repeatedly match the largest creditor with the largest debtor, settling the maximum possible amount each time.
4. Repeats until all balances are zero, minimizing the total number of transactions required.

This reduces an O(n squared) "everyone pays everyone" scenario down to at most n-1 transactions for n users.

## Tech Stack

- Java — core language
- Gson — JSON serialization for persistence
- PriorityQueue (Java Collections) — used in the settlement algorithm

## How to Run

Compile:

    javac -cp lib/gson-2.10.1.jar -d out src/Main.java src/model/*.java src/service/*.java

Run (Windows):

    java -cp "out;lib/gson-2.10.1.jar" Main

Run (macOS/Linux):

    java -cp "out:lib/gson-2.10.1.jar" Main

## Sample Usage

1. Create users: Harikha, Jhansi, Gayatri
2. Create group: "Trip to Goa"
3. Add expense: Harikha paid 3000, split equally among 3
4. View settlement: Jhansi owes Harikha 1000, Gayatri owes Harikha 1000

## Roadmap

- REST API conversion using Spring Boot (in progress)
- Replace file-based storage with a relational database
- Add expense categories and reporting

## Author

Harikha Bammidi
