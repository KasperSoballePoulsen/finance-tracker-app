# Finance Tracker App

A full-stack personal finance management application featuring a Java Spring Boot backend and a React + TypeScript frontend. This app enables users to track incomes and expenses, manage budgets, and gain insights into their financial health.

## Features
- **Add Transactions**: Record new income or expense entries with amount, category, and date.
- **Edit Transactions**: Update existing financial entries to keep your records accurate.
- **Delete Transactions**: Remove outdated or incorrect transactions from your history.
- **Filter by Type**: Easily switch between viewing all transactions, only expenses, or only earnings.
- **Category Management**: Create your own custom income and expense categories.
- **Summary Statistics**: Get a quick overview of your total earnings and expenses.
- **Trend Charts**: Visualize earnings trends over time with dynamic charts using Recharts.
- **Responsive UI**: Clean and intuitive interface that adapts to different screen sizes.


## Technologies Used

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database (for development and testing)
- JUnit 5 (for unit testing)
- Spring Boot Test

### Frontend
- React
- TypeScript
- Vite
- ESLint
- Recharts

## Getting Started

### Prerequisites
- Node.js (v16 or later)
- Java Development Kit (JDK) 21
- Gradle

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/KasperSoballePoulsen/finance-tracker-app.git
   cd finance-tracker-app

2. **Backend Setup**:

   ```bash
   cd backend
   ./gradlew bootRun

The backend server will start at http://localhost:8080.

3. **Frontend Setup**:

   ```bash
   cd ../frontend
   npm install
   npm run dev

The frontend application will be available at http://localhost:5173.

## API Documentation (Swagger UI)

This project uses **Swagger** (via Springdoc OpenAPI) to automatically generate interactive API documentation for the backend.

#### Accessing Swagger UI

1. Make sure the backend server is running:

   ```bash
   cd backend
   ./gradlew bootRun

2. Open your browser and navigate to:
   
  http://localhost:8080/swagger-ui.html

From here, you can explore and test all available API endpoints directly in the browser.

## Testing

The project includes automated unit tests written using **JUnit 5** and **Spring Boot Test**.

- `TransactionServiceTest`: Covers all transaction-related functionality.
- `CategoryServiceTest`: Covers functionality related to category management.

### How to run the tests

Run the following command from the `backend` folder:

   ```bash
   ./gradlew test
   
```
If all tests pass, you should see:
   ```bash
   BUILD SUCCESSFUL
```

## H2 Database Console

This application uses a **file-based H2 database** for development and testing. You can inspect the database via the H2 web console.

### Accessing the H2 Console

1. Make sure the backend is running:

   ```bash
   cd backend
   ./gradlew bootRun
   
2. Open your browser and go to:
http://localhost:8080/h2-console

3. Use the following settings:
- Driver Class: org.h2.Driver
- JDBC URL: jdbc:h2:file:./data/backend
- User Name: sa
- Password: (leave blank)


## Project Structure
```bash
finance-tracker-app/
├── backend/
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/example/backend/
│       │   │       ├── api/
│       │   │       │   ├── dto/
│       │   │       │   │   └── SummaryDTO 
│       │   │       │   ├── CategoryController.java
│       │   │       │   └── TransactionController.java
│       │   │       ├── bll/
│       │   │       │   ├── CategoryService.java
│       │   │       │   └── TransactionService.java
│       │   │       ├── dal/
│       │   │       │   ├── initializer/
│       │   │       │   │   └── DatabaseInitializer.java
│       │   │       │   ├── model/
│       │   │       │   │   ├── Category.java
│       │   │       │   │   ├── Transaction.java
│       │   │       │   │   └── TransactionType.java
│       │   │       │   └── repository/
│       │   │       │       ├── CategoryRepository.java
│       │   │       │       └── TransactionRepository.java
│       │   │       └── BackendApplication.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/
│               └── com/example/backend/
│                   ├── CategoryServiceTest.java
│                   └── TransactionServiceTest.java
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── CategoryTrendChart.tsx
│   │   │   ├── CreateTransaction.tsx
│   │   │   ├── Header.tsx
│   │   │   ├── SummaryStatistics.tsx
│   │   │   ├── ViewCategories.tsx
│   │   │   └── ViewTransactions.tsx
│   │   ├── styles/
│   │   │   └── index.css
│   │   ├── types/
│   │   │   ├── Category.ts
│   │   │   ├── Transaction.ts
│   │   │   └── TransactionType.ts
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── vite-env.d.ts
│   ├── eslint.config.js
│   ├── index.html
│   ├── package.json
│   ├── package-lock.json
│   ├── tsconfig.json
│   └── vite.config.ts
├── .gitignore
└── README.md


