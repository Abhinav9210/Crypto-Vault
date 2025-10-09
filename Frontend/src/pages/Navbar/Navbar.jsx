import { Button } from "@/components/ui/button";
import {
  AvatarIcon,
  DragHandleHorizontalIcon,
  MagnifyingGlassIcon,
} from "@radix-ui/react-icons";
import SideBar from "../SideBar/SideBar";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { useNavigate } from "react-router-dom";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useSelector } from "react-redux";

const Navbar = () => {
  const navigate = useNavigate();
  const { auth } = useSelector((store) => store);

  const handleNavigate = () => {
    if (auth.user) {
      auth.user.role === "ROLE_ADMIN"
        ? navigate("/admin/withdrawal")
        : navigate("/profile");
    } else {
      navigate("/login");
    }
  };

  return (
    <nav className="sticky top-0 left-0 right-0 z-50 bg-[#0a0a0a]/90 backdrop-blur-md border-b border-[#1f1f1f] flex justify-between items-center px-4 py-3 shadow-sm">
      {/* LEFT SIDE */}
      <div className="flex items-center gap-3">
        {/* Sidebar Drawer */}
        <Sheet>
          <SheetTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="rounded-full hover:bg-[#1a1a1a] text-gray-300"
            >
              <DragHandleHorizontalIcon className="h-6 w-6" />
            </Button>
          </SheetTrigger>
          <SheetContent
            className="w-72 bg-[#0a0a0a] border-r border-[#1f1f1f] text-white"
            side="left"
          >
           
<SheetHeader>
  <SheetTitle>
    <div className="text-2xl flex justify-center items-center gap-2 mb-4">
      <Avatar className="h-10 w-10 border border-emerald-400">
        <AvatarImage src="https://cdn.pixabay.com/photo/2021/04/30/16/47/binance-logo-6219389_1280.png" />
        <AvatarFallback className="bg-[#0a0a0a] text-emerald-400">CV</AvatarFallback>
      </Avatar>
      <div>
        <span className="font-bold text-emerald-400">Crypto</span>
        <span className="text-gray-300">Vault</span>
      </div>
    </div>
  </SheetTitle>
</SheetHeader>

<SideBar /> {/* Sidebar updated with green neon theme */}

          </SheetContent>
        </Sheet>

        {/* Logo */}
        <div
          onClick={() => navigate("/")}
          className="flex items-center gap-2 cursor-pointer select-none"
        >
          <span className="font-bold text-xl text-white tracking-wide">
            Crypto<span className="text-blue-400">Vault</span>
          </span>
        </div>

        {/* Search Button */}
        <div className="hidden sm:flex ml-6">
          <Button
            variant="outline"
            onClick={() => navigate("/search")}
            className="flex items-center gap-2 border-[#2a2a2a] bg-[#0f0f0f] text-gray-300 hover:bg-[#1a1a1a]"
          >
            <MagnifyingGlassIcon className="h-5 w-5 text-blue-400" />
            <span>Search</span>
          </Button>
        </div>
      </div>

      {/* RIGHT SIDE */}
      <div>
        <Avatar
          className="cursor-pointer hover:shadow-md hover:scale-105 transition-all duration-200 border border-[#2a2a2a]"
          onClick={handleNavigate}
        >
          {!auth.user ? (
            <AvatarIcon className="h-6 w-6 text-gray-400" />
          ) : auth.user.profilePic ? (
            <AvatarImage src={auth.user.profilePic} />
          ) : (
            <AvatarFallback className="bg-[#1a1a1a] text-blue-400">
              {auth.user?.fullName?.[0]?.toUpperCase()}
            </AvatarFallback>
          )}
        </Avatar>
      </div>
    </nav>
  );
};

export default Navbar;
