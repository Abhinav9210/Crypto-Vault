
import React, { useEffect, useState } from "react";
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
} from "recharts";
import fetchData from "@/utils/fetchData";

const StockChart = ({ coinId }) => {
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchChart = async () => {
      const data = await fetchData("TIME_SERIES_DAILY", coinId.toUpperCase());
      if (data && data["Time Series (Daily)"]) {
        const times = Object.entries(data["Time Series (Daily)"]);
        const formatted = times
          .map(([date, values]) => ({
            date,
            close: parseFloat(values["4. close"]),
          }))
          .reverse();
        setChartData(formatted);
      }
    };
    fetchChart();
  }, [coinId]);

  if (!chartData.length)
    return (
      <div className="h-96 flex justify-center items-center text-gray-400">
        Loading chart...
      </div>
    );

  return (
    <div className="w-full h-96 bg-[#111111] rounded-lg p-3 shadow-lg">
      <ResponsiveContainer width="100%" height="100%">
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#222222" />
          <XAxis dataKey="date" axisLine={false} tick={{ fill: "#94a3b8" }} />
          <YAxis axisLine={false} tick={{ fill: "#94a3b8" }} />
          <Tooltip
            contentStyle={{
              backgroundColor: "#111111",
              border: "1px solid #333",
              color: "#fff",
            }}
            labelStyle={{ color: "#cbd5e1" }}
            itemStyle={{ color: "#00bfff" }}
          />
          <Line
            type="monotone"
            dataKey="close"
            stroke="#00bfff"
            strokeWidth={2}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default StockChart;
