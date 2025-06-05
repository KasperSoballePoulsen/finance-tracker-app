import React from "react";
import { Category } from "../types/Category";

function ViewCategories() {
  const [categories, setCategories] = React.useState<Category[]>([]);

  React.useEffect(() => {
    const fetchCategories = async () => {
      try {
        const res = await fetch("/categories");
        const data = await res.json();
        setCategories(data);
      } catch (err) {
        console.error("Failed to fetch categories:", err);
      }
    };
  
    fetchCategories();
  }, []);

  return (
    <div className="Box ViewCategories">
      <h2>Categories</h2>
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
