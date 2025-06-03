import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, Link } from "react-router-dom";
import Modal from "react-modal";
import { logout as logoutAction } from "../../redux/slices/AuthSlice";
import { getUserInfo } from "../../services/APIService";
import { toast } from 'react-toastify';
import CreateNewPost from './CreateNewPost';
import {
  Home, Calendar, Ticket, Megaphone, Settings, BadgePlus, HelpCircle, Sparkles, ChevronUp, ChevronDown,
  User, LogOut, Video
} from 'lucide-react';

Modal.setAppElement("#root");

export default function Home_SidebarL() {
  const [open, setOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [user, setUser] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");

  useEffect(() => {
    const storedUsername = localStorage.getItem("username");
    if (storedUsername) {
      setUsername(storedUsername);
    }

    const fetchUserInfo = async () => {
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
    fetchUserInfo();
  }, []);

  const handleLogout = () => {
    sessionStorage.removeItem("accessToken");
    localStorage.removeItem("id");
    localStorage.removeItem("username");
    localStorage.removeItem("roles");
    dispatch(logoutAction());
    toast.success("Logout successfully!");
    navigate('/');
  };

  const handleProfileClick = () => {
    navigate(`/profile`);
  };

  const openModal = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  return (
    <>
      <aside className="flex h-full w-64 flex-col justify-between border-r border-gray-200 p-4 fixed pt-20">
        <div>
          <div className="space-y-1 pt-2">
            <SidebarItem icon={<Home size={18} />} label="Home" to="/home" />
            <SidebarItem icon={<Calendar size={18} />} label="Events" to="#" />
            <SidebarItem icon={<Ticket size={18} />} label="Orders" to="#" />
            <SidebarItem icon={<Megaphone size={18} />} label="Broadcasts" to="#" />
            <hr className="border-gray-200 my-4" />
            <SidebarItem
              icon={<BadgePlus size={18} />}
              label="New Post"
              onClick={openModal}
            />
            <SidebarItem icon={<Video size={18} />} label="Livestream" to="#" />
            <hr className="border-gray-200 my-4" />
            <SidebarItem icon={<HelpCircle size={18} />} label="Support" to="#" />
            <SidebarItem icon={<Sparkles size={18} />} label="Changelog" to="#" />
          </div>
        </div>

        <div>
          <div className="relative transition-all duration-300">
            <div
              className={`absolute bottom-full left-0 mb-2 w-full overflow-hidden transition-all duration-300 ease-in-out z-10 
              ${open ? 'max-h-40 opacity-100' : 'max-h-0 opacity-0'}`}
            >
              <div className="flex flex-col rounded border border-gray-200 shadow">
                <DropdownItem icon={<User size={18} />} label="Profile" onClick={handleProfileClick} />
                <DropdownItem icon={<Settings size={18} />} label="Settings" />
                <DropdownItem icon={<LogOut size={18} />} label="Logout" onClick={handleLogout} />
              </div>
            </div>

            <div
              className="flex items-center justify-between rounded border border-gray-200 p-3 cursor-pointer"
              onClick={() => setOpen(!open)}
            >
              <div className="flex items-center gap-3">
                <img
                  src={user?.avatarUrl || "/default-avatar.jpg"}
                  alt={user?.fullName || "User Avatar"}
                  className="h-6 w-6 rounded-full"
                />
                <div>
                  <p className="text-sm font-medium">{user?.fullName}</p>
                  <p className="text-xs text-gray-500">@{username}</p>
                </div>
              </div>
              {open ? (
                <ChevronDown className="text-gray-500" size={18} />
              ) : (
                <ChevronUp className="text-gray-500" size={18} />
              )}
            </div>
          </div>
        </div>
      </aside>
      <CreateNewPost isOpen={isModalOpen} onClose={closeModal} user={user} />
    </>
  );
}

function SidebarItem({ icon, label, to, onClick }) {
  return to ? (
    <Link
      to={to}
      className="flex w-full items-center gap-3 rounded px-3 py-2 text-base font-medium hover:bg-gray-200 hover:shadow-lg transition cursor-pointer"
    >
      <span className="text-gray-500">{icon}</span>
      <span>{label}</span>
    </Link>
  ) : (
    <button
      onClick={onClick}
      className="flex w-full items-center gap-3 rounded px-3 py-2 text-base font-medium hover:bg-gray-200 hover:shadow-lg transition cursor-pointer"
    >
      <span className="text-gray-500">{icon}</span>
      <span>{label}</span>
    </button>
  );
}

function DropdownItem({ icon, label, onClick }) {
  return (
    <button
      onClick={onClick}
      className="flex items-center gap-3 rounded px-3 py-2 text-base font-medium hover:bg-gray-200 hover:shadow-lg transition cursor-pointer"
    >
      {icon}
      <span>{label}</span>
    </button>
  );
}