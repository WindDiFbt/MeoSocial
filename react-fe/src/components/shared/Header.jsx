import { useState, useRef, useEffect } from "react";
import { Search, Inbox, Bell } from "lucide-react";

export default function Header() {
    const [searchOpen, setSearchOpen] = useState(false);
    const [searchText, setSearchText] = useState("");
    const searchRef = useRef(null);
    const inputRef = useRef(null);

    useEffect(() => {
        if (searchOpen && inputRef.current) {
            inputRef.current.focus();
        }
    }, [searchOpen]);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (searchRef.current && !searchRef.current.contains(e.target)) {
                setSearchOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    return (
        <header className="flex items-center justify-between bg-white p-4 shadow-md fixed w-full top-0 z-50">
            {/* Logo */}
            <div className="pl-2 flex items-center gap-2">
                <img
                    src="/meosocial_logo.png"
                    alt="MeoSocial Logo"
                    className="w-10 h-10 object-contain"
                />
                <span className="text-xl font-bold">MeoSocial</span>
            </div>

            {/* Search area */}
            <div className="relative pl-9 flex-1 flex">
                <div className="w-100 h-11">
                    <div
                        ref={searchRef}
                        onClick={() => !searchOpen && setSearchOpen(true)}
                        className={`flex items-center border border-gray-300 transition-all duration-300 rounded-full cursor-pointer
                            ${searchOpen ? "px-4 py-1 w-full" : "w-11 h-11 justify-center"}`}
                    >
                        <Search className="text-gray-500 shrink-0" size={17} />
                        {searchOpen && (
                            <input
                                ref={inputRef}
                                type="text"
                                placeholder="Tìm kiếm..."
                                value={searchText}
                                onChange={(e) => setSearchText(e.target.value)}
                                className="ml-2 w-full py-[5px] bg-transparent focus:outline-none"
                            />
                        )}
                    </div>
                </div>
            </div>

            {/* Icons */}
            <div className="flex space-x-6">
                <button className="p-2 hover:bg-gray-100 rounded-full">
                    <Bell className="text-gray-600" size={24} />
                </button>
                <button className="p-2 hover:bg-gray-100 rounded-full">
                    <Inbox className="text-gray-600" size={24} />
                </button>
            </div>
        </header>
    );
}