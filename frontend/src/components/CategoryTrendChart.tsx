import React, { useEffect, useState } from "react";
import {Category} from "../types/Category"
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";


type SummaryItem = {
  month: string;
  category: string;
  type: "EXPENSE" | "EARNING" | "BALANCE";
  total: number;
};



function CategoryTrendChart() {
  const currentYear = new Date().getFullYear();
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>("");
  const [yearInput, setYearInput] = useState<string>(currentYear.toString());
  const [data, setData] = useState<SummaryItem[]>([]);

  const fetchCategories = async () => {
    const res = await fetch("/categories?type=EARNING");
    const json = await res.json();
    setCategories(json);
    if (json.length > 0) setSelectedCategory(json[0].name);
  };

  const fetchSummary = async (year: number) => {
    const res = await fetch(`/transactions/summary?year=${year}`);
    const json = await res.json();
    setData(json);
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleLoad = () => {
    const parsed = parseInt(yearInput, 10);
    if (!isNaN(parsed)) fetchSummary(parsed);
  };

  const months = Array.from(
    new Set(data.map((d) => d.month).filter((m) => m !== "YEARLY"))
  ).sort();

  const chartData = months.map((month) => {
    const entry: { month: string; total: number } = {
      month,
      total:
        data.find(
          (d) =>
            d.month === month &&
            d.category === selectedCategory &&
            d.type === "EARNING"
        )?.total || 0,
    };
    return entry;
  });

  return (
    <div className="Box">
      <h2>Earning Trend by Category</h2>

      <div >
        <label id="selectChartYear">
          Year:
          <input
            type="number"
            value={yearInput}
            onChange={(e) => setYearInput(e.target.value)}
            
          />
        </label>

        <label>
          Category:
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            id="selectChartCategory"
          >
            {categories.map((cat) => (
              <option key={cat.id} value={cat.name}>
                {cat.name}
              </option>
            ))}
          </select>
        </label>

        <button id="loadChartBtn" onClick={handleLoad}>
          Load
        </button>
      </div>

      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={chartData}>
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line
            type="monotone"
            dataKey="total"
            strokeWidth={2}
            name={selectedCategory}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

export default CategoryTrendChart;
