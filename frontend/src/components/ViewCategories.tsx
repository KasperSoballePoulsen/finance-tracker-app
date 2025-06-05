import React from "react";
import { Category } from "../types/Category";
import { TransactionType } from "../types/TransactionType";

function ViewCategories() {
  const [categories, setCategories] = React.useState<Category[]>([]);
  const [newCategoryName, setNewCategoryName] = React.useState<string>("");
  const [newCategoryType, setNewCategoryType] = React.useState<TransactionType>("EXPENSE");

  React.useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const res = await fetch("/categories");
      const data = await res.json();
      setCategories(data);
    } catch (err) {
      console.error("Failed to fetch categories:", err);
    }
  };

  const handleAddCategory = async () => {
    if (!newCategoryName.trim()) {
      alert("Category name is required.");
      return;
    }

    try {
      const res = await fetch("/categories", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: newCategoryName.trim(),
          type: newCategoryType,
        }),
      });

      if (!res.ok) {
        throw new Error("Failed to add category");
      }

      setNewCategoryName("");
      setNewCategoryType("EXPENSE");
      fetchCategories(); // refresh the list
    } catch (err) {
      alert(err);
    }
  };

  return (
    <div className="Box ViewCategories">
      <h2>Categories</h2>

      <div className="NewCategoryForm">
        <div className="CategoryName">
          <label htmlFor="category-name-input">Category name:</label>
          <input
            type="text"
            value={newCategoryName}
            onChange={(e) => setNewCategoryName(e.target.value)}
          />
        </div>
        

        <div className="CategoryTypeRadios">
          <label>
            <input
              type="radio"
              name="categoryType"
              value="EXPENSE"
              checked={newCategoryType === "EXPENSE"}
              onChange={() => setNewCategoryType("EXPENSE")}
            />
            Expense
          </label>
          <label>
            <input
              type="radio"
              name="categoryType"
              value="EARNING"
              checked={newCategoryType === "EARNING"}
              onChange={() => setNewCategoryType("EARNING")}
            />
            Earning
          </label>
        </div>

        <button onClick={handleAddCategory}>Add Category</button>
      </div>

      <ul className="CategoryList">
        {categories.map((cat) => (
          <li key={cat.id}>
            <strong>{cat.name}</strong> – {cat.type}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ViewCategories;
