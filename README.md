# 💰 Personal Finance Manager

A **console-based Personal Finance Management System** built using **Core Java (JDK 17)** and **Object-Oriented Programming** principles. This project allows users to register/login, track their income and expenses, set monthly budgets, and generate detailed financial reports — all through a clean, menu-driven command-line interface.

Built as a placement-preparation project to demonstrate strong fundamentals in Java, OOP, Collections, File Handling, and Exception Handling — without relying on any external frameworks or libraries.

---

## 📌 Project Overview

Personal Finance Manager helps users take control of their money by recording income and expenses, categorizing spending, setting monthly budgets, and generating insightful reports (savings, category-wise spending, highest/lowest expense, etc.). All data is automatically saved to disk in human-readable CSV files and reloaded the next time the application starts — no database required.

---

## ✨ Features

### 👤 User Management
- Register a new account with username, password, full name & email
- Login with credential verification
- Logout

### 💵 Income Management
- Add / Update / Delete income entries
- View all income (sorted by date)
- Search income by keyword

### 💸 Expense Management
- Add / Update / Delete expense entries
- View all expenses
- Search expenses by keyword
- Categorize expenses into: `FOOD`, `TRAVEL`, `SHOPPING`, `RENT`, `MEDICAL`, `ENTERTAINMENT`, `EDUCATION`, `OTHERS`

### 📊 Budget Module
- Set a monthly budget limit
- View remaining budget for any month
- Automatic **warning** when 80%+ of budget is used
- **Budget exceeded** alert when spending crosses the limit

### 📈 Reports
- Total Income
- Total Expense
- Net Savings
- Category-wise Spending breakdown
- Monthly Report (income vs expense vs savings)
- Highest Expense
- Lowest Expense
- Export full report to a `.txt` file

### 🔍 Search Module
Search income/expenses by:
- Date
- Category
- Amount
- Keyword

### ↕️ Sorting
Sort expenses by:
- Amount
- Date
- Category

### 🗂️ File Handling
- Data is auto-saved to CSV files inside the `/data` folder
- `/data` folder and files are created automatically on first run
- Data automatically loads back in on every startup

### 🛡️ Exception Handling
- Every user input is validated
- Invalid input never crashes the application — the user is simply re-prompted

---

## 🗃️ Folder Structure

```
PersonalFinanceManager/
│
├── src/
│   ├── Main.java                       # Application entry point & menu driver
│   │
│   ├── models/
│   │   ├── User.java                   # User account entity
│   │   ├── Transaction.java            # Abstract base class (Income/Expense)
│   │   ├── Income.java                 # Income entity (extends Transaction)
│   │   ├── Expense.java                # Expense entity (extends Transaction)
│   │   ├── ExpenseCategory.java        # Enum of expense categories
│   │   └── Budget.java                 # Monthly budget entity
│   │
│   ├── services/
│   │   ├── AuthenticationService.java  # Register / Login / Logout logic
│   │   ├── IncomeService.java          # Income CRUD + search + sort
│   │   ├── ExpenseService.java         # Expense CRUD + search + sort
│   │   ├── BudgetService.java          # Budget setting & tracking
│   │   ├── ReportService.java          # Report generation & export
│   │   └── SearchService.java          # Unified search across income & expense
│   │
│   ├── storage/
│   │   └── FileManager.java            # CSV file read/write (BufferedReader/Writer)
│   │
│   └── utils/
│       ├── InputValidator.java         # Console input validation helpers
│       ├── DateUtil.java               # Date parsing/formatting helpers
│       └── CurrencyFormatter.java      # Currency display formatting
│
├── data/                                # Auto-generated CSV data files (created at runtime)
├── README.md
```

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| **Java 17** | Core language |
| **OOP** (Encapsulation, Inheritance, Abstraction, Polymorphism) | Application design |
| **Collections Framework** (`ArrayList`, `HashMap`, `LinkedHashMap`, `Comparator`, `Comparable`) | In-memory data management & sorting |
| **java.time** (`LocalDate`, `LocalDateTime`) | Date & time handling |
| **File I/O** (`BufferedReader`, `BufferedWriter`, `File`) | Persistent CSV storage |
| **Exception Handling** | Robust, crash-free input validation |
| **Scanner** | Console input |

No Spring Boot. No Maven/Gradle. No external libraries — **pure Core Java**.

---

## ▶️ How to Run

### Option 1: IntelliJ IDEA
1. Open IntelliJ IDEA → `Open` → select the `PersonalFinanceManager` folder
2. Mark `src` as **Sources Root** (right-click `src` → *Mark Directory as* → *Sources Root*)
3. Right-click `Main.java` → **Run 'Main.main()'**

### Option 2: VS Code
1. Open the `PersonalFinanceManager` folder in VS Code
2. Install the **Extension Pack for Java** (if not already installed)
3. Open `src/Main.java` and click **Run**

### Option 3: Command Line
```bash
# Navigate to the project root
cd PersonalFinanceManager

# Compile all source files into an "out" directory
javac -d out $(find src -name "*.java")

# Run the application
java -cp out Main
```

> On first run, the app automatically creates a `/data` folder with CSV files to store your users, incomes, expenses and budgets.

---

## 🖼️ Screenshots

> _Add screenshots of the console menu, reports, and sample output here before uploading to GitHub._

```
=========================================
      PERSONAL FINANCE MANAGER (v1.0)
=========================================
1. Register
2. Login
3. Exit
```

---

## 🚀 Future Improvements

- Migrate storage to a relational database (MySQL/PostgreSQL) using JDBC
- Add password hashing (currently stored as plain text for simplicity)
- Build a GUI version using JavaFX or Swing
- Add multi-currency support
- Export reports as PDF using a PDF generation library
- Add recurring/scheduled transactions (e.g. monthly rent, subscriptions)
- Add unit tests using JUnit

---

## 📄 License

This project is licensed under the **MIT License** — feel free to use, modify, and distribute it for learning or portfolio purposes.

---

### 👨‍💻 Author

Built as a resume/portfolio project to demonstrate Core Java, OOP design, and file-handling skills for software engineering placements.
