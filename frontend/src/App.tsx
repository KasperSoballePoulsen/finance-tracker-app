import React from "react";
import Header from "./components/Header";
import CreateTransaction from "./components/CreateTransaction";
import ViewTransactions from "./components/ViewTransactions";
import { Transaction } from "./types/Transaction";
import ViewCategories from "./components/ViewCategories";
import SummaryStatistics from "./components/SummaryStatistics";
import CategoryTrendChart from "./components/CategoryTrendChart";

function App() {
  const [transactions, setTransactions] = React.useState<Transaction[]>([]);
  const [editingTransaction, setEditingTransaction] = React.useState<Transaction | null>(null);

  const fetchTransactions = async () => {
    const res = await fetch("/transactions");
    const data = await res.json();
    setTransactions(data);
  };

  React.useEffect(() => {
    fetchTransactions();
  }, []);

  const handleEdit = (transaction: Transaction) => {
    setEditingTransaction(transaction);
  };

  const handleDelete = async (id: number) => {
    try {
      const response = await fetch(`/transactions/${id}`, {
        method: "DELETE",
      });
  
      if (response.ok) {
        setTransactions((prev) => prev.filter((t) => t.id !== id));
      } else {
        console.error("Failed to delete transaction. Status:", response.status);
      }
    } catch (err) {
      console.error("Error deleting transaction:", err);
    }
  };

  return (
    <>
      <Header />
      <div className="MainLayout">
        <div className="LeftColumn">
        <CreateTransaction
          onSaved={() => {
            fetchTransactions();
            setEditingTransaction(null);
          }}
          transaction={editingTransaction}
        />
        <ViewCategories />
        </div>

        <ViewTransactions
        transactions={transactions}
        onEditClick={handleEdit}
        onDeleteClick={handleDelete}
        />
        
      </div>
      <SummaryStatistics/>
      <CategoryTrendChart/>
    </>
  );
}

export default App;
