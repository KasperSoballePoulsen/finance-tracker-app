import React from "react";

function CreateTransaction() {

  const [selectedCategory, setSelectedCategory] = React.useState<string>("");
  const [type, setType] = React.useState<"EXPENSE" | "EARNING">("EXPENSE");
  const [amount, setAmount] = React.useState<number>(0);
  const [date, setDate] = React.useState<Date>(new Date())
  const [description, setDescription] = React.useState<string>("")

  

  return (
    <div className="CreateTransactionBox">
      <h2>Create Transaction</h2>
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
  type: "EXPENSE" | "EARNING";
  setType: (t: "EXPENSE" | "EARNING") => void;
};

function SelectType({ type, setType }: TypeProps) {
  return (
    <div className="SelectType">
      <label>Type:</label>
      <select value={type} onChange={(e) => setType(e.target.value as "EXPENSE" | "EARNING")}>
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
  type: "EXPENSE" | "EARNING";
};

function SelectCategory({ selected, setSelected, type}: CategoryProps) {

  type Category = {
    id: number;
    name: string;
  };
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
  type: "EXPENSE" | "EARNING";
  selectedCategory: string;
};

function SaveTransactionButton({amount, date, description, type, selectedCategory}: SaveTransactionProps) {
  const saveTransaction = async () => {
    const transaction = {
      amount,
      date: date.toISOString(),
      description,
      type,
      category: {
        id: parseInt(selectedCategory),
      },
    };

    

    try {
      if (!selectedCategory) {
        throw new Error("You have to choose a category");
      }

      const res = await fetch("/transactions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(transaction),
      });

      if (!res.ok) {
        throw new Error("Failed to save transaction");
      }

      
      
      alert("Transaction saved!");
    } catch (err) {
      alert(err);

    }
  };

  return (
    <div className="SaveTransactionButton">
      <button onClick={saveTransaction}>Save Transaction</button>
    </div>
  );
}

export default CreateTransaction;