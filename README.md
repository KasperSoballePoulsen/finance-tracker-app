# Finance Tracker App

A full-stack personal finance management application featuring a Java Spring Boot backend and a React + TypeScript frontend. This app enables users to track incomes and expenses, manage budgets, and gain insights into their financial health.

## Features
- **Add Transactions**: Record new income or expense entries with amount, category, and date.
- **Edit Transactions**: Update existing financial entries to keep your records accurate.
- **Delete Transactions**: Remove outdated or incorrect transactions from your history.
- **Filter by Type**: Easily switch between viewing all transactions, only expenses, or only earnings.
- **Category Management**: Create your own custom income and expense categories.
- **Summary Statistics**: Get a quick overview of your total earnings and expenses.
- **Responsive UI**: Clean and intuitive interface that adapts to different screen sizes.



## Technologies Used

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database (for development and testing)

### Frontend
- React
- TypeScript
- Vite
- ESLint

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

### Project Structure
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
│                   └── BackendApplicationTests.java
├── frontend/
│   ├── src/
│   │   ├── components/
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


