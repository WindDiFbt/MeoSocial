import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, Link } from "react-router-dom";
import Modal from "react-modal";
import { logout as logoutAction } from "../../redux/slices/AuthSlice";
import { getUserInfo } from "../../services/APIService";
import { toast } from 'react-toastify';

import {
  Home, Calendar, Ticket, Megaphone, Settings, BadgePlus, HelpCircle, Sparkles, ChevronUp, ChevronDown,
  User, LogOut, Video, X, Image, Tag, ImagePlay, SmilePlus, Ellipsis, Globe, Footprints, UserRound, Lock,
} from 'lucide-react';

Modal.setAppElement("#root");

export default function Home_SidebarL() {
  const [open, setOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [user, setUser] = useState(null);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [postContent, setPostContent] = useState("");
  const [uploadedFiles, setUploadedFiles] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const isPostButtonEnabled = postContent.trim() !== "" || uploadedFiles.length > 0;

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
  const closeModal = () => {
    setIsModalOpen(false);
    setPostContent("");
    setUploadedFiles([]);
  };

  const handlePostContentChange = (e) => {
    setPostContent(e.target.value);
  };

  const handleFileUpload = (e) => {
    const files = Array.from(e.target.files);
    setUploadedFiles((prevFiles) => [...prevFiles, ...files]);
  };

  return (
    <>
      <aside className="flex h-full w-64 flex-col justify-between border-r border-gray-200 p-4 fixed pt-20">
        <div>
          <div className="space-y-1">
            <SidebarItem icon={<Home className="h-4 w-4" />} label="Home" to="/home" />
            <SidebarItem icon={<Calendar className="h-4 w-4" />} label="Events" to="#" />
            <SidebarItem icon={<Ticket className="h-4 w-4" />} label="Orders" />
            <SidebarItem icon={<Megaphone className="h-4 w-4" />} label="Broadcasts" />
            <hr className="border-gray-200 my-4" />
            <SidebarItem
              icon={<BadgePlus className="h-4 w-4" />}
              label="New Post"
              onClick={openModal}
            />
            <SidebarItem icon={<Video className="h-4 w-4" />} label="Livestream" />
            <hr className="border-gray-200 my-4" />
            <SidebarItem icon={<HelpCircle className="h-4 w-4" />} label="Support" />
            <SidebarItem icon={<Sparkles className="h-4 w-4" />} label="Changelog" />
          </div>
        </div>

        <div>
          <div className="relative transition-all duration-300">
            <div
              className={`absolute bottom-full left-0 mb-2 w-full overflow-hidden transition-all duration-300 ease-in-out z-10 
              ${open ? 'max-h-40 opacity-100' : 'max-h-0 opacity-0'}`}
            >
              <div className="flex flex-col rounded border border-gray-200 shadow">
                <DropdownItem icon={<User className="h-4 w-4" />} label="Profile" onClick={handleProfileClick} />
                <DropdownItem icon={<Settings className="h-4 w-4" />} label="Settings" />
                <DropdownItem icon={<LogOut className="h-4 w-4" />} label="Logout" onClick={handleLogout} />
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
                <ChevronDown className="h-4 w-4 text-gray-500" />
              ) : (
                <ChevronUp className="h-4 w-4 text-gray-500" />
              )}
            </div>
          </div>
        </div>
      </aside>

      <Modal
        isOpen={isModalOpen}
        onRequestClose={closeModal}
        contentLabel="Tạo bài viết"
        className="fixed inset-0 flex items-center justify-center z-50"
        overlayClassName="fixed inset-0 backdrop-blur-xs"
      >
        <div className="bg-white rounded-lg w-full max-w-xl shadow-xl">
          <div className="flex justify-between items-center px-4 py-3 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-gray-800">New post</h2>
            <button
              onClick={closeModal}
              className="text-gray-500 hover:text-gray-700"
            >
              <X size={24} />
            </button>
          </div>
          <div className="px-4 py-3">
            <div className="flex items-center gap-3 mb-4">
              <img
                src={user?.avatarUrl || "/default-avatar.jpg"}
                alt={user?.fullName || "User Avatar"}
                className="w-10 h-10 rounded-full object-cover"
              />
              <div>
                <p className="font-semibold text-gray-800">{user?.fullName}</p>
                <div className="relative inline-block text-left pt-1">
                  <button
                    onClick={() => setShowDropdown(!showDropdown)}
                    className="text-xs text-gray-500 border rounded px-2 py-0.5 hover:bg-gray-100 flex items-center gap-1"
                  >
                    <div className='flex items-center gap-1'>
                      <Globe className='h-4 w-4'></Globe>Public
                      <ChevronDown className="h-4 w-4 text-gray-500" />
                    </div>
                  </button>

                  {showDropdown && (
                    <div className="absolute right-0 mt-2 w-40 bg-white border border-gray-200 rounded shadow-lg z-10">
                      <ul className="text-sm text-gray-700">
                        <li className="px-4 flex py-2 gap-2 hover:bg-gray-100 cursor-pointer"><Globe className='h-5 w-5'></Globe>Public</li>
                        <li className="px-4 flex py-2 gap-2 hover:bg-gray-100 cursor-pointer"><Footprints className='h-5 w-5'></Footprints>Followers</li>
                        <li className="px-4 flex py-2 gap-2 hover:bg-gray-100 cursor-pointer"><UserRound className='h-5 w-5'></UserRound>Friends</li>
                        <li className="px-4 flex py-2 gap-2 hover:bg-gray-100 cursor-pointer"><Lock className='h-5 w-5'></Lock>Private</li>
                      </ul>
                    </div>
                  )}
                </div>

              </div>
            </div>

            <textarea
              className="w-full text-lg text-gray-800 placeholder-gray-500 focus:outline-none resize-none"
              placeholder="What's on your mind?"
              rows={5}
              value={postContent}
              onChange={handlePostContentChange}
            />

            <div>
              <div className="flex justify-end gap-4 mt-4">
                <label className="items-center gap cursor-pointer" title='Upload images/videos'>
                  <input
                    type="file"
                    multiple
                    className="hidden"
                    onChange={handleFileUpload}
                  />
                  <Image className="h-6 w-6"></Image>
                </label>
                <label className="items-center gap-2 cursor-pointer" title='Tag others'>
                  <input className="hidden" />
                  <Tag className="h-6 w-6 text-blue-400"></Tag>
                </label>
                <label className="items-center gap-2 cursor-pointer" title='Feelings/Activities'>
                  <input className="hidden" />
                  <SmilePlus className="h-6 w-6"></SmilePlus>
                </label>
                <label className="items-center gap-2 cursor-pointer" title='GIF'>
                  <input className="hidden" />
                  <ImagePlay className="h-6 w-6"></ImagePlay>
                </label>
                <label className="items-center gap-2 cursor-pointer" title='GIF'>
                  <input className="hidden" />
                  <Ellipsis className="h-6 w-6"></Ellipsis>
                </label>
              </div>
            </div>
            {uploadedFiles.length > 0 && (
              <div className="mt-2 text-sm text-gray-600">
                {uploadedFiles.map((file, index) => (
                  <p key={index}>{file.name}</p>
                ))}
              </div>
            )}
          </div>

          <div className="border-t border-gray-200 px-4 py-3">
            <button
              className={`w-full bg-blue-500 text-white font-semibold py-2 rounded-lg hover:bg-blue-600 ${!isPostButtonEnabled ? "opacity-50 cursor-not-allowed" : ""
                }`}
              disabled={!isPostButtonEnabled}
            >
              Post
            </button>
          </div>
        </div>
      </Modal>
    </>
  );
}

function SidebarItem({ icon, label, to, onClick }) {
  return to ? (
    <Link
      to={to}
      className="flex items-center gap-3 rounded px-3 py-2 text-base font-medium hover:bg-gray-200 hover:shadow-lg transition cursor-pointer"
    >
      <span className="text-gray-500">{icon}</span>
      <span>{label}</span>
    </Link>
  ) : (
    <button
      onClick={onClick}
      className="flex items-center gap-3 rounded px-3 py-2 text-base font-medium hover:bg-gray-200 hover:shadow-lg transition cursor-pointer"
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