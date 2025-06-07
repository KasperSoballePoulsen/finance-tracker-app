import React from "react";

type SummaryItem = {
  month: string;
  category: string;
  type: "EXPENSE" | "EARNING" | "BALANCE";
  total: number;
};

function SummaryStatistics() {
  const currentYear = new Date().getFullYear();
  const [yearInput, setYearInput] = React.useState<string>(currentYear.toString());
  const [data, setData] = React.useState<SummaryItem[]>([]);

  const fetchSummary = async (year: number) => {
    try {
      const res = await fetch(`/transactions/summary?year=${year}`);
      const json = await res.json();
      setData(json);
    } catch (err) {
      console.error("Failed to fetch summary statistics", err);
    }
  };

  const handleLoadClick = () => {
    const parsed = parseInt(yearInput, 10);
    if (!isNaN(parsed)) {
      fetchSummary(parsed);
    }
  };

  const months = Array.from(new Set(data.map((d) => d.month).filter((m) => m !== "YEARLY"))).sort();
  const earnings = Array.from(new Set(data.filter((d) => d.type === "EARNING").map((d) => d.category)));
  const expenses = Array.from(new Set(data.filter((d) => d.type === "EXPENSE").map((d) => d.category)));

  const getAmount = (category: string, month: string, type: "EXPENSE" | "EARNING") => {
    const match = data.find((d) => d.category === category && d.month === month && d.type === type);
    return match ? match.total.toFixed(2) : "0.00";
  };

  const getTotalBalance = (month: string) => {
    const match = data.find((d) => d.category === "TOTAL" && d.type === "BALANCE" && d.month === month);
    return match ? match.total.toFixed(2) : "0.00";
  };

  const getYearlyTotal = () => {
    const match = data.find((d) => d.month === "YEARLY" && d.category === "TOTAL" && d.type === "BALANCE");
    return match ? match.total.toFixed(2) : null;
  };

  const yearlyTotal = getYearlyTotal();

  const getMonthLabel = (monthStr: string) => {
    const [year, month] = monthStr.split("-");
    const date = new Date(Number(year), Number(month) - 1);
    return date.toLocaleString("default", { month: "short" }); 
  };

  return (
    <div className="Box SummaryStatistics">
      <h2>Summary Statistics</h2>

      <div className="loadYear">
        <label htmlFor="year-input">Year: </label>
        <input
          id="year-input"
          type="number"
          value={yearInput}
          onChange={(e) => setYearInput(e.target.value)}
        />
        <button onClick={handleLoadClick}>Load</button>
      </div>

      <table>
        <thead>
          <tr>
            <th>Category</th>
            {months.map((month) => (
              <th key={month}>{getMonthLabel(month)}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {earnings.map((category) => (
            <tr key={`earning-${category}`}>
              <td>{category} (Earning)</td>
              {months.map((month) => (
                <td key={month}>{getAmount(category, month, "EARNING")}</td>
              ))}
            </tr>
          ))}
          {expenses.map((category) => (
            <tr key={`expense-${category}`}>
              <td>{category} (Expense)</td>
              {months.map((month) => (
                <td key={month}>{getAmount(category, month, "EXPENSE")}</td>
              ))}
            </tr>
          ))}
          <tr style={{ fontWeight: "bold" }}>
            <td>Total</td>
            {months.map((month) => (
              <td key={month}>{getTotalBalance(month)}</td>
            ))}
          </tr>
        </tbody>
      </table>

      {yearlyTotal && (
        <p style={{ marginTop: "20px", fontWeight: "bold" }}>
          Total balance of the year: {yearlyTotal} DKK
        </p>
      )}
    </div>
  );
}

export default SummaryStatistics;
