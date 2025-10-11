import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getUserAssets } from "@/Redux/Assets/Action";
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { getAllOrdersForUser } from "@/Redux/Order/Action";
import { calculateProfite } from "@/Util/calculateProfite";
import { readableDate } from "@/Util/readableDate";

const TreadingHistory = () => {
  const dispatch = useDispatch();
  const { order } = useSelector((store) => store);

  useEffect(() => {
    dispatch(getUserAssets(localStorage.getItem("jwt")));
    dispatch(getAllOrdersForUser({ jwt: localStorage.getItem("jwt") }));
  }, []);

  return (
    <div className="relative">
      <ScrollArea className="rounded-xl border border-[#222222]">
        <Table className="min-w-full text-white">
          <TableHeader>
            <TableRow className="sticky top-0 bg-gradient-to-r from-[#0a0a0a] to-[#111111] z-10 border-b-2 border-blue-500">
              <TableHead className="py-3">Date & Time</TableHead>
              <TableHead>Treading Pair</TableHead>
              <TableHead>Buy Price</TableHead>
              <TableHead>Selling Price</TableHead>
              <TableHead>Order Type</TableHead>
              <TableHead>Profite/Loss</TableHead>
              <TableHead className="text-right">VALUE</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {order.orders?.map((item, idx) => (
              <TableRow
                key={item.id}
                className={`transition-all duration-300 cursor-pointer border border-transparent rounded-lg
                  hover:border hover:border-blue-500 hover:shadow-[0_0_15px_rgba(0,150,255,0.5)]
                  hover:scale-101
                  ${idx % 2 === 0 ? "bg-[#0a0a0a]" : "bg-[#0c0c0c]"} neon-hover`}
              >
                <TableCell>
                  <p>{readableDate(item.timestamp).date}</p>
                  <p className="text-gray-400">{readableDate(item.timestamp).time}</p>
                </TableCell>
                <TableCell className="font-medium flex items-center gap-2">
                  <Avatar className="-z-50">
                    <AvatarImage
                      src={item.orderItem.coin.image}
                      alt={item.orderItem.coin.symbol}
                    />
                  </Avatar>
                  <span>{item.orderItem.coin.name}</span>
                </TableCell>
                <TableCell>${item.orderItem.buyPrice}</TableCell>
                <TableCell>{"$" + item.orderItem.sellPrice || "-"}</TableCell>
                <TableCell>{item.orderType}</TableCell>
                <TableCell
                  className={`${
                    calculateProfite(item) < 0
                      ? "text-red-400 font-semibold"
                      : "text-green-400 font-semibold"
                  }`}
                >
                  {item.orderType === "SELL" ? calculateProfite(item) : "-"}
                </TableCell>
                <TableCell className="text-right">${item.price}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </ScrollArea>
    </div>
  );
};

export default TreadingHistory;
