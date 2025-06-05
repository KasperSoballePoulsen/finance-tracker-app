import React from "react";
import Header from "./components/Header";
import CreateTransaction from "./components/CreateTransaction";
import ViewTransactions from "./components/ViewTransactions";
import { Transaction } from "./types/Transaction";
import ViewCategories from "./components/ViewCategories";

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
    onEditClick={(t) => setEditingTransaction(t)}
  />
</div>
    </>
  );
}

export default App;
