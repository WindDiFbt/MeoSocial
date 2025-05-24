import { useState, useRef } from 'react';
import Modal from "react-modal";
import { toast } from 'react-toastify';
import './Home_SidebarL.css';
import { createPost, createPostMedia } from "../../services/APIService";
import {
    ChevronDown, X, Image, Tag, ImagePlay, SmilePlus, Ellipsis, Globe, Footprints, UserRound, Lock,
    ChevronLeft, ChevronRight
} from 'lucide-react';

export default function CreateNewPost({ isOpen, onClose, user }) {
    const [postContent, setPostContent] = useState("");
    const [uploadedFiles, setUploadedFiles] = useState([]);
    const [filePreviews, setFilePreviews] = useState([]);
    const [showDropdown, setShowDropdown] = useState(false);
    const carouselRef = useRef(null);
    const isPostButtonEnabled = postContent.trim() !== "" || uploadedFiles.length > 0;

    const handlePostContentChange = (e) => {
        setPostContent(e.target.value);
    };

    const handleCloseModal = () => {
        setFilePreviews([]);
        setUploadedFiles([]);
        onClose();
    };

    const scrollCarousel = (direction) => {
        if (carouselRef.current) {
            const scrollAmount = 300;
            carouselRef.current.scrollBy({
                left: direction === "next" ? scrollAmount : -scrollAmount,
                behavior: "smooth",
            });
        }
    };

    const handleRemoveFile = (index) => {
        setFilePreviews((prevPreviews) => prevPreviews.filter((_, i) => i !== index));
        setUploadedFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
    };

    const handleFileUpload = (e) => {
        const files = Array.from(e.target.files);
        const validExtensions = [".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".heic", ".mp4", ".mov", ".avi", ".webm", ".mkv"];
        const isValidFile = (file) => {
            const mimeValid = file.type.startsWith("image/") || file.type.startsWith("video/");
            const extensionValid = validExtensions.some((ext) =>
                file.name.toLowerCase().endsWith(ext)
            );
            return mimeValid || extensionValid;
        };
        const validFiles = files.filter(isValidFile);
        if (validFiles.length !== files.length) {
            toast.error("Some files were not uploaded because they are not valid image or video files.");
        }
        const previews = validFiles.map((file) => ({
            url: URL.createObjectURL(file),
            type: file.type.startsWith("image/") || file.name.match(/\.(jpg|jpeg|png|gif|bmp|webp|heic)$/i)
                ? "image"
                : "video",
        }));
        setUploadedFiles((prevFiles) => [...prevFiles, ...validFiles]);
        setFilePreviews((prevPreviews) => [...prevPreviews, ...previews]);
    };

    const handleCreateNewPost = async () => {
        try {
            const response = await createPost(postContent);
            const postId = response?.data?.response.id;
            if (!postId) {
                throw new Error('Failed to create post. No post ID returned.');
            }
            if (uploadedFiles && uploadedFiles.length > 0) {
                const uploadPromises = uploadedFiles.map((file) => createPostMedia(postId, file));
                await Promise.all(uploadPromises);
            }
            toast.success('Post created successfully!');
            handleCloseModal();
        } catch (error) {
            console.error('Error creating post:', error);
            toast.error('Failed to create post. Please try again.');
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={handleCloseModal}
            contentLabel="Create Post"
            className="fixed inset-0 flex items-center justify-center z-50"
            overlayClassName="fixed inset-0 backdrop-blur-xs"
        >
            <div className="bg-white rounded-lg w-full max-w-xl shadow-xl">
                <div className="flex justify-between items-center px-4 py-3 border-b border-gray-200">
                    <h2 className="text-lg font-semibold text-gray-800">New post</h2>
                    <button
                        onClick={handleCloseModal}
                        className="text-gray-500 hover:text-gray-700 cursor-pointer"
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

                    {filePreviews.length > 0 && (
                        <div className="relative mt-4">
                            <div
                                ref={carouselRef}
                                className="flex overflow-x-auto gap-2 border-t border-gray-200 pt-2 scrollbar-hide"
                            >
                                {filePreviews.map((preview, index) => (
                                    <div key={index} className="relative flex-shrink-0 w-32 h-32">
                                        <div className="flex justify-end">
                                            <X
                                                onClick={() => handleRemoveFile(index)}
                                                className="absolute text-gray-500 w-5 h-5 bg-gray-100 rounded-full items-center justify-center text-xs hover:text-gray-700 cursor-pointer"
                                            ></X>
                                        </div>
                                        {preview.type === "image" ? (
                                            <div>
                                                <img
                                                    src={preview.url}
                                                    alt={`Preview ${index}`}
                                                    className="w-full h-full object-cover rounded border-2 border-gray-200"
                                                />
                                            </div>
                                        ) : (
                                            <video
                                                src={preview.url}
                                                controls
                                                className="w-full h-full object-cover rounded"
                                            />
                                        )}
                                    </div>
                                ))}
                            </div>
                            {filePreviews.length > 4 && (
                                <>
                                    <button
                                        onClick={() => scrollCarousel("prev")}
                                        className="absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-200 p-2 rounded-full shadow hover:bg-gray-300 cursor-pointer"
                                    >
                                        <ChevronLeft size={24} />
                                    </button>
                                    <button
                                        onClick={() => scrollCarousel("next")}
                                        className="absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-200 p-2 rounded-full shadow hover:bg-gray-300 cursor-pointer"
                                    >
                                        <ChevronRight size={24} />
                                    </button>
                                </>
                            )}
                        </div>
                    )}
                </div>

                <div className="border-t border-gray-200 px-4 py-3">
                    <button
                        onClick={handleCreateNewPost}
                        className={`w-full bg-blue-500 text-white font-semibold py-2 rounded-lg hover:bg-blue-600 ${!isPostButtonEnabled ? "opacity-50 cursor-not-allowed" : ""
                            }`}
                        disabled={!isPostButtonEnabled}
                    >
                        Post
                    </button>
                </div>
            </div>
        </Modal>
    );
}