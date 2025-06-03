import { useState, useEffect } from "react";

type TransactionType = "EXPENSE" | "EARNING";

interface Category {
  id: number;
  name: string;
}

function CreateTransaction() {
  const [amount, setAmount] = useState("");
  const [date, setDate] = useState("");
  const [description, setDescription] = useState("");
  const [type, setType] = useState<TransactionType>("EXPENSE");
  const [categoryId, setCategoryId] = useState<number>();
  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    // Hent kategorier fra backend
    fetch("/api/categories")
      .then((res) => res.json())
      .then((data) => setCategories(data))
      .catch((err) => console.error("Error loading categories", err));
  }, []);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const transaction = {
      amount: parseFloat(amount),
      date,
      description,
      type,
      category: {
        id: categoryId,
      },
    };

    fetch("/api/transactions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(transaction),
    })
      .then((res) => {
        if (res.ok) {
          alert("Transaction created!");
          // Reset form or redirect
        } else {
          throw new Error("Failed to create transaction");
        }
      })
      .catch((err) => {
        console.error(err);
        alert("Error creating transaction");
      });
  };

  return (
    <div>
      <h2>Create Transaction</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Amount:</label>
          <input
            type="number"
            step="0.01"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Date:</label>
          <input
            type="date"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Description:</label>
          <input
            type="text"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>

        <div>
          <label>Type:</label>
          <select value={type} onChange={(e) => setType(e.target.value as TransactionType)}>
            <option value="EXPENSE">Expense</option>
            <option value="EARNING">Earning</option>
          </select>
        </div>

        <div>
          <label>Category:</label>
          <select
            value={categoryId}
            onChange={(e) => setCategoryId(parseInt(e.target.value))}
            required
          >
            <option value="">Select a category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.name}
              </option>
            ))}
          </select>
        </div>

        <button type="submit">Save</button>
      </form>
    </div>
  );
}


export default CreateTransaction;