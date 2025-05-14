import { useState } from "react";
import { Menu, MenuButton, MenuItem, MenuItems, Transition } from "@headlessui/react";
import { Fragment } from "react";
import { Search, Home, Users, Bell, User } from "lucide-react";
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";
import { logout as logoutAction} from "../../redux/slices/AuthSlice";

export default function Header() {
    const [search, setSearch] = useState("");
    const dispatch = useDispatch();
    
    const handleLogout = () => {
        // Handle logout logic here
        sessionStorage.removeItem("accessToken");
        localStorage.removeItem("id");
        localStorage.removeItem("username");
        localStorage.removeItem("roles");
        dispatch(logoutAction());
        toast.success("Logout successfully!");
        window.location.href = "/login";
    };
    return (
        <header className="flex items-center justify-between bg-white p-4 shadow-md fixed w-full top-0 z-50">
            <div className="text-xl font-bold text-blue-600">MeoSocial</div>

            <div className="relative w-1/3">
                <Search className="absolute left-3 top-2.5 text-gray-400" size={20} />
                <input
                    type="text"
                    placeholder="Tìm kiếm..."
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className="pl-10 w-full py-2 rounded-full border border-gray-300 focus:ring-2 focus:ring-blue-500 outline-none"
                />
            </div>

            <div className="flex space-x-6">
                <button className="p-2 hover:bg-gray-100 rounded-full">
                    <Home className="text-gray-600" size={24} />
                </button>
                <button className="p-2 hover:bg-gray-100 rounded-full">
                    <Users className="text-gray-600" size={24} />
                </button>
                <button className="p-2 hover:bg-gray-100 rounded-full">
                    <Bell className="text-gray-600" size={24} />
                </button>
            </div>

            <Menu as="div" className="relative">
                <MenuButton className="p-2 hover:bg-gray-100 rounded-full">
                    <User className="text-gray-600" size={24} />
                </MenuButton>

                <Transition
                    as={Fragment}
                    enter="transition ease-out duration-100"
                    enterFrom="transform opacity-0 scale-95"
                    enterTo="transform opacity-100 scale-100"
                    leave="transition ease-in duration-75"
                    leaveFrom="transform opacity-100 scale-100"
                    leaveTo="transform opacity-0 scale-95"
                >
                    <MenuItems static className="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-lg shadow-lg">
                        <MenuItem>
                            {({ active }) => (
                                <a
                                    href="/profile"
                                    className={`block px-4 py-2 text-sm ${active ? "bg-gray-100" : ""}`}
                                >
                                    Hồ sơ
                                </a>
                            )}
                        </MenuItem>
                        <MenuItem>
                            {({ active }) => (
                                <a
                                    href="/settings"
                                    className={`block px-4 py-2 text-sm ${active ? "bg-gray-100" : ""}`}
                                >
                                    Cài đặt
                                </a>
                            )}
                        </MenuItem>
                        <MenuItem>
                            {({ active }) => (
                                <button onClick={handleLogout}
                                    className={`block w-full text-left px-4 py-2 text-sm text-red-600 ${active ? "bg-gray-100" : ""
                                        }`}
                                >
                                    Đăng xuất
                                </button>
                            )}
                        </MenuItem>
                    </MenuItems>
                </Transition>
            </Menu>
        </header>
    );
}
