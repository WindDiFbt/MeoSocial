import { useState, useEffect } from "react";
import { getUserInfo } from "../../services/APIService";
import { toast } from "react-toastify";
import Header from "../shared/Header";
import Home_SidebarL from "../home/Home_SidebarL";
import { Link, Outlet, useLocation } from "react-router-dom";

export default function Profile() {
    const [user, setUser] = useState(null);
    const [username, setUsername] = useState("");
    const location = useLocation();

    useEffect(() => {
        const storedUsername = localStorage.getItem("username");
        if (storedUsername) {
            setUsername(storedUsername);
        }

        const fetchUser = async () => {
            try {
                const response = await getUserInfo();
                if (response?.data?.response) {
                    setUser(response.data.response);
                } else {
                    toast.error("Failed to fetch user information.");
                }
            } catch (error) {
                console.error("Error fetching user info:", error);
                toast.error("An error occurred while fetching user information.");
            }
        };
        fetchUser();
    }, []);

    return (
        <div>
            <div className="bg-gray-100 min-h-screen">
                <Header />
                <Home_SidebarL />
                <div className="pt-22 max-w-2xl mx-auto">
                    {/* Profile */}
                    <div className="flex items-center space-x-8 mb-8">
                        <img
                            src={user?.avatarUrl || "../../../default-avatar.jpg"}
                            alt="User Avatar"
                            className="w-32 h-32 rounded-full object-cover"
                        />
                        <div>
                            <h1 className="text-2xl font-bold">{user?.fullName}</h1>
                            <p className="text-gray-500">@{username}</p>
                            <div className="flex space-x-4 mt-4">
                                <span>
                                    <strong>0</strong> Posts
                                </span>
                                <span>
                                    <strong>0</strong> Followers
                                </span>
                                <span>
                                    <strong>0</strong> Following
                                </span>
                            </div>
                        </div>
                    </div>

                    {/* Tabs */}
                    <div className="flex justify-center space-x-4 mb-6 border-b border-gray-300 pb-2">
                        <Link
                            to="/profile/posts"
                            className={`px-4 py-2 text-sm font-medium ${location.pathname === "/profile/posts"
                                ? "text-blue-600 border-b-2 border-blue-600"
                                : "text-gray-500 hover:text-blue-600"
                                }`}
                        >
                            Posts
                        </Link>
                        <Link
                            to="/profile/media"
                            className={`px-4 py-2 text-sm font-medium ${location.pathname === "/profile/media"
                                ? "text-blue-600 border-b-2 border-blue-600"
                                : "text-gray-500 hover:text-blue-600"
                                }`}
                        >
                            Media
                        </Link>
                        <Link
                            to="/profile/friends"
                            className={`px-4 py-2 text-sm font-medium ${location.pathname === "/profile/friends"
                                ? "text-blue-600 border-b-2 border-blue-600"
                                : "text-gray-500 hover:text-blue-600"
                                }`}
                        >
                            Friends
                        </Link>
                    </div>

                    {/* Nested Routes Content */}
                    <Outlet />
                </div>
            </div>
        </div>
    );
}