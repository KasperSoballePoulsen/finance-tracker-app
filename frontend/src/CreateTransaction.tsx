import React from "react";

function CreateTransaction() {

  const [selectedCategory, setSelectedCategory] = React.useState<string>("");
  const [type, setType] = React.useState<"EXPENSE" | "EARNING">("EXPENSE");

  const saveTransaction = () => {
    console.log("Saving transaction with category:", selectedCategory);
  };

  return (
    <div className="CreateTransactionBox">
      <h2>Create Transaction</h2>
      <SelectCategory selected={selectedCategory} setSelected={setSelectedCategory} />
      <SelectType type={type} setType={setType} />
      <button onClick={saveTransaction}>Save Transaction</button>
    </div>
  );
}

type Category = {
  id: number;
  name: string;
};

type CategoryProps = {
  selected: string;
  setSelected: (value: string) => void;
};



function SelectCategory({ selected, setSelected }: CategoryProps) {
  const [categories, setCategories] = React.useState<Category[]>([]);

  React.useEffect(() => {
    async function fetchCategories() {
      try {
        const res = await fetch("/categories", {
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
  }, []);

  return (
    <div className="SelectCategory">
      <label>Category:</label>
      <select value={selected} onChange={(e) => setSelected(e.target.value)}>
        <option value="">No Category</option>
        {categories.map((cat) => (
          <option key={cat.id} value={cat.id.toString()}>
            {cat.name}
          </option>
        ))}
      </select>
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
        <option value="EXPENSE">Expense</option>
        <option value="INCOME">Income</option>
      </select>
    </div>
  );
}
export default CreateTransaction;