import React from "react";
import { TransactionType } from "../types/TransactionType";
import { Category } from "../types/Category";
import { Transaction } from "../types/Transaction";

function CreateTransaction({
  onSaved,
  transaction,
}: {
  onSaved: () => void;
  transaction: Transaction | null;
}) {

  const [selectedCategory, setSelectedCategory] = React.useState<string>("");
  const [type, setType] = React.useState<TransactionType>("EXPENSE");
  const [amount, setAmount] = React.useState<number>(0);
  const [date, setDate] = React.useState<Date>(new Date())
  const [description, setDescription] = React.useState<string>("")

  React.useEffect(() => {
    if (transaction) {
      setAmount(transaction.amount);
      setDate(new Date(transaction.date));
      setDescription(transaction.description);
      setType(transaction.type);
      setSelectedCategory(transaction.category.id.toString());
    }
  }, [transaction]);

  return (
    <div className="Box CreateTransactionBox">
      <h2>{transaction ? "Update Transaction" : "Create Transaction"}</h2>
      <AddAmount amount={amount} setAmount={setAmount} />
      <SelectType type={type} setType={setType} />
      <SelectDate date={date} setDate={setDate}/>
      <SelectCategory selected={selectedCategory} setSelected={setSelectedCategory} type={type} />
      <AddDescription description={description} setDescription={setDescription}/>
      <SaveTransactionButton
        amount={amount}
        date={date}
        description={description}
        type={type}
        selectedCategory={selectedCategory}
        onSaved={onSaved}
        transaction={transaction}
      />
    </div>
  );
}


function AddAmount({ amount, setAmount }: { amount: number; setAmount: (value: number) => void }) {
  return (
    <div className="AddAmount">
      <label>Amount:</label>
      <input
        type="number"
        value={amount}
        onChange={(e) => setAmount(parseFloat(e.target.value))}
        min="0"
        step="0.01"
      />
    </div>
  );
}

type TypeProps = {
  type: TransactionType;
  setType: (t: TransactionType) => void;
};

function SelectType({ type, setType }: TypeProps) {
  return (
    <div className="SelectType">
      <label>Type:</label>
      <select value={type} onChange={(e) => setType(e.target.value as TransactionType)}>
        <option value="">-- Choose Type --</option>
        <option value="EXPENSE">Expense</option>
        <option value="EARNING">Earning</option>
      </select>
    </div>
  );
}




function SelectDate({ date, setDate }: { date: Date; setDate: (value: Date) => void }) {
  return (
    <div className="SelectDate">
      <label>Date:</label>
      <input
        type="date"
        value={date.toISOString().split("T")[0]}
        onChange={(e) => setDate(new Date(e.target.value))}
      />
    </div>
  );
}

type CategoryProps = {
  selected: string;
  setSelected: (value: string) => void;
  type: TransactionType;
};

function SelectCategory({ selected, setSelected, type}: CategoryProps) {

  
  const [categories, setCategories] = React.useState<Category[]>([]);

  React.useEffect(() => {
    async function fetchCategories() {
      try {
        const res = await fetch(`/categories?type=${type}`, {
          method: "GET",
          headers: { accept: "application/json" },
        });
        const data = await res.json();
        setCategories(data);
      } catch (err) {
        console.error("Could not load categories", err);
      }
    }
  
    fetchCategories();
  }, [type]); 
  

  return (
    <div className="SelectCategory">
      <label>Category:</label>
      <select value={selected} onChange={(e) => setSelected(e.target.value)}>
        <option value="">-- Choose Category --</option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.id.toString()}>
            {cat.name}
          </option>
        ))}
      </select>
    </div>
  );
}



function AddDescription({description, setDescription}: { description: string; setDescription: (value: string) => void }) {
  return (
    <div className="AddDescription">
      <label>Description:</label>
      <input
        type="text"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        
      />
    </div>
  );
}


type SaveTransactionProps = {
  amount: number;
  date: Date;
  description: string;
  type: TransactionType;
  selectedCategory: string;
  onSaved: () => void;
  transaction?: Transaction | null;
};

function SaveTransactionButton({amount, date, description, type, selectedCategory, onSaved, transaction}: SaveTransactionProps) {
  const saveTransaction = async () => {
    const transactionData = {
      amount,
      date: date.toISOString(),
      description,
      type,
      category: {
        id: parseInt(selectedCategory),
      },
    };
  
    try {
      if (!selectedCategory || isNaN(parseInt(selectedCategory))) {
        throw new Error("You have to choose a category");
      }
  
      const url = transaction?.id
        ? `/transactions/${transaction.id}`
        : "/transactions";
  
      const method = transaction?.id ? "PUT" : "POST";
  
      const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(transactionData),
      });
  
      if (!res.ok) {
        throw new Error("Failed to save transaction");
      }
  
      alert("Transaction saved!");
      onSaved();
    } catch (err) {
      alert(err);
    }
  };

  return (
    <div className="SaveTransactionButton">
      <button onClick={saveTransaction}>
        {transaction ? "Update Transaction" : "Save Transaction"}
      </button>
    </div>
  );
}


export default CreateTransaction;