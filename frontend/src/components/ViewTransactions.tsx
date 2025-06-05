import React from "react";
import { Transaction } from "../types/Transaction";



function ViewTransactions({transactions, onEditClick}: {transactions: Transaction[]; onEditClick: (t: Transaction) => void;}) {
  const [filter, setFilter] = React.useState<"ALL" | "EXPENSE" | "EARNING">("ALL");

  const filtered = transactions.filter((t) =>
    filter === "ALL" ? true : t.type === filter
  );

  const filterOptions: ("ALL" | "EXPENSE" | "EARNING")[] = ["ALL", "EXPENSE", "EARNING"];
  const labels = {
    ALL: "Show All",
    EXPENSE: "Show Expenses",
    EARNING: "Show Earnings",
  };

  return (
    <div className="Box ViewTransactions">
      <h2>Transactions</h2>

      <div className="Filters">
        {filterOptions.map((option) => {
          return (
            <label key={option}>
            <input
              type="radio"
              name="filter"
              checked={filter === option}
              onChange={() => setFilter(option)}
            />
            {labels[option]}
            </label>
          );
        })}
      </div>
      <ul className="TransactionList">
        {filtered.map((t) => (
          <li key={t.id} className={t.type === "EARNING" ? "earning" : "expense"}>
            <strong>{t.amount} DKK</strong> – {t.date.slice(0, 10)} – {t.category.name} – {t.description}
            <button onClick={() => onEditClick(t)}>Edit</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ViewTransactions;
