import { useEffect, useRef, useState } from "react";
import { AssetTable } from "./AssetTable";
import { Button } from "@/components/ui/button";
import StockChart from "../StockDetails/StockChart";
import { Cross1Icon, DotIcon, ChevronLeftIcon } from "@radix-ui/react-icons";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchCoinDetails,
  fetchCoinList,
  getTop50CoinList,
  fetchTreadingCoinList,
} from "@/Redux/Coin/Action";
import { MessageCircle } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { sendMessage } from "@/Redux/Chat/Action";
import SpinnerBackdrop from "@/components/custome/SpinnerBackdrop";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationEllipsis,
} from "@/components/ui/pagination";

const Home = () => {
  const dispatch = useDispatch();
  const [page, setPage] = useState(1);
  const [category, setCategory] = useState("all");
  const { coin, chatBot, auth } = useSelector((store) => store);
  const [isBotRelease, setIsBotRelease] = useState(false);
  const [inputValue, setInputValue] = useState("");
  const chatContainerRef = useRef(null);

  useEffect(() => {
    dispatch(fetchCoinList(page));
  }, [page]);

  useEffect(() => {
    dispatch(
      fetchCoinDetails({
        coinId: "bitcoin",
        jwt: auth.jwt || localStorage.getItem("jwt"),
      })
    );
  }, []);

  useEffect(() => {
    if (category === "top50") dispatch(getTop50CoinList());
    else if (category === "trading") dispatch(fetchTreadingCoinList());
  }, [category]);

  useEffect(() => {
    if (chatContainerRef.current)
      chatContainerRef.current.scrollIntoView({ behavior: "smooth" });
  }, [chatBot.messages]);

  const handlePageChange = (page) => setPage(page);
  const handleBotRelease = () => setIsBotRelease(!isBotRelease);
  const handleChange = (e) => setInputValue(e.target.value);
  const handleKeyPress = (e) => {
    if (e.key === "Enter" && inputValue.trim() !== "") {
      dispatch(
        sendMessage({ prompt: inputValue, jwt: auth.jwt || localStorage.getItem("jwt") })
      );
      setInputValue("");
    }
  };

  if (coin.loading) return <SpinnerBackdrop />;

  return (
    <div className="relative min-h-screen bg-[#0a0a0a] text-white">
      <div className="lg:flex">
        {/* Left Panel */}
        <div className="lg:w-[50%] border-r border-[#1f1f1f] p-3">
          <div className="flex gap-3 mb-3">
            <Button
              variant={category === "all" ? "default" : "outline"}
              onClick={() => setCategory("all")}
              className="rounded-full bg-[#1f1f1f] hover:bg-[#2a2a2a] shadow-sm text-white"
            >
              All
            </Button>
            <Button
              variant={category === "top50" ? "default" : "outline"}
              onClick={() => setCategory("top50")}
              className="rounded-full bg-[#1f1f1f] hover:bg-[#2a2a2a] shadow-sm text-white"
            >
              Top 50
            </Button>
          </div>

          <AssetTable
            category={category}
            coins={category === "all" ? coin.coinList : coin.top50}
          />

          {category === "all" && (
            <Pagination className="flex justify-center mt-2">
              <PaginationContent>
                <PaginationItem>
                  <Button
                    variant="ghost"
                    disabled={page === 1}
                    onClick={() => handlePageChange(page - 1)}
                  >
                    <ChevronLeftIcon className="mr-1 h-4 w-4" />
                    Prev
                  </Button>
                </PaginationItem>
                {[1, 2, 3].map((p) => (
                  <PaginationItem key={p}>
                    <PaginationLink onClick={() => handlePageChange(p)} isActive={page === p}>
                      {p}
                    </PaginationLink>
                  </PaginationItem>
                ))}
                {page > 3 && (
                  <PaginationItem>
                    <PaginationLink isActive>{page}</PaginationLink>
                  </PaginationItem>
                )}
                <PaginationItem>
                  <PaginationEllipsis />
                </PaginationItem>
                <PaginationItem>
                  <PaginationNext onClick={() => handlePageChange(page + 1)} />
                </PaginationItem>
              </PaginationContent>
            </Pagination>
          )}
        </div>

        {/* Right Panel */}
        <div className="hidden lg:block lg:w-[50%] p-5">
          <StockChart coinId={"bitcoin"} />
          <div className="flex gap-5 items-center mt-4">
            <Avatar>
              <AvatarImage src={coin.coinDetails?.image?.large} />
            </Avatar>
            <div>
              <div className="flex items-center gap-2 text-gray-400">
                <p>{coin.coinDetails?.symbol?.toUpperCase()}</p>
                <DotIcon className="text-gray-500" />
                <p>{coin.coinDetails?.name}</p>
              </div>
              <div className="flex items-end gap-2">
                <p className="text-xl font-bold">
                  {coin.coinDetails?.market_data.current_price.usd}
                </p>
                <p
                  className={`${
                    coin.coinDetails?.market_data.market_cap_change_24h < 0
                      ? "text-red-600"
                      : "text-green-400"
                  }`}
                >
                  {coin.coinDetails?.market_data.market_cap_change_24h} (
                  {coin.coinDetails?.market_data.market_cap_change_percentage_24h}%)
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

  {/* Floating Chat Bot */}
<div className="fixed bottom-10 right-10 z-50 flex flex-col items-end gap-2">
  {isBotRelease && (
    <div className="w-[22rem] h-[60vh] max-w-full bg-[#111111] rounded-xl shadow-2xl flex flex-col overflow-hidden">
      {/* Header */}
      <div className="flex justify-between items-center px-4 py-2 border-b border-gray-700">
        <p className="font-semibold text-white">Chat Bot</p>
        <Button size="icon" variant="ghost" onClick={handleBotRelease}>
          <Cross1Icon className="text-white" />
        </Button>
      </div>

      {/* Chat Messages */}
      <div className="flex-1 overflow-y-auto overflow-x-hidden px-4 py-2 space-y-3 scrollbar-thin scrollbar-thumb-blue-600 scrollbar-track-gray-900">
        {/* Welcome Message */}
        <div className="self-start bg-[#1a1a1a] px-3 py-2 rounded-md shadow-inner border border-gray-700">
          <p className="text-white">{`Hi, ${auth.user?.fullName}`}</p>
          <p className="text-gray-400 text-sm">Ask any crypto-related question!</p>
        </div>

        {/* Chat Messages */}
        {chatBot.messages.map((item, idx) => (
          <div
            ref={chatContainerRef}
            key={idx}
            className={`flex w-full ${
              item.role === "user" ? "justify-end" : "justify-start"
            }`}
          >
            <div
              className={`px-3 py-2 rounded-md max-w-[18rem] break-words ${
                item.role === "user"
                  ? "bg-gradient-to-r from-gray-800 to-gray-900 text-right text-white shadow-md"
                  : "bg-gradient-to-r from-gray-900 to-gray-800 text-left text-white shadow-sm"
              }`}
            >
              <p>{item.role === "user" ? item.prompt : item.ans}</p>
            </div>
          </div>
        ))}
        {chatBot.loading && (
          <p className="text-gray-400 text-sm">Fetching data...</p>
        )}
      </div>

      {/* Input */}
      <div className="px-3 py-2 border-t border-gray-700">
        <Input
          placeholder="Type a message..."
          className="bg-[#1a1a1a] border-none text-white focus:ring-0 placeholder-gray-500 w-full"
          value={inputValue}
          onChange={handleChange}
          onKeyPress={handleKeyPress}
        />
      </div>
    </div>
  )}

  {/* Floating Button */}
  <Button
    className="bg-[#1f1f1f] hover:bg-[#2a2a2a] text-white rounded-full px-4 py-2 flex items-center gap-2 shadow-lg hover:shadow-blue-500/50 transition-all duration-200"
    onClick={handleBotRelease}
  >
    <MessageCircle className="text-blue-400" size={24} />
    Chat Bot
  </Button>
</div>

    </div>
  );
};

export default Home;
