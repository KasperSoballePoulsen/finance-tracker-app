import React from "react";
import Header from "./components/Header"

import CreateTransaction from "./components/CreateTransaction"
import ViewTransactions from "./components/ViewTransactions"
import { Transaction } from "./types/Transaction";




function App() {
  const [transactions, setTransactions] = React.useState<Transaction[]>([]);

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
      <Header/>
      <CreateTransaction onSaved={fetchTransactions} />
      <ViewTransactions transactions={transactions} />
    </>
  );
}

export default App
