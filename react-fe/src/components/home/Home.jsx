import { useState, useEffect } from "react";
import Header from "../shared/Header";
import Home_SidebarL from "./Home_SidebarL";
import { Heart, MessageCircle, Share } from "lucide-react";
import { getPost } from "../../services/APIService";
import formatDate from "../../utils/DateUtil";

const Post = () => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await getPost();
                if (response?.data?.response) {
                    setPosts(response.data.response);
                } else {
                    setError("Invalid API data!");
                }
            } catch (error) {
                console.error("Error when calling API: ", error);
                setError(error.message || "Unknown error");
            } finally {
                setLoading(false);
            }
        };
        fetchPosts();
    }, []);
    return { posts, loading, error };
}

export default function Home() {
    const { posts, loading, error } = Post();
    return (
        <div>
            <div className="bg-gray-100 min-h-screen">
                <Header />
                <Home_SidebarL />
                <div className="pt-20 max-w-2xl mx-auto">
                    {posts.length === 0 ? (
                        <p className="text-center text-gray-500">Chưa có bài viết nào.</p>
                    ) : (
                        posts.map((post) => (
                            <div key={post.id} className="bg-white rounded-lg shadow-md p-4 my-4">
                                <div className="flex items-start space-x-3">
                                    <img
                                        src={post.userAvatar || "../../../default-avatar.jpg"}
                                        alt="Avatar"
                                        className="w-10 h-10 rounded-full"
                                    />
                                    <div>
                                        <span className="font-semibold block">{post.fullName}</span>
                                        <div className="text-sm text-gray-500 flex items-center space-x-2">
                                            <span>@{post.userName}</span>
                                            <span>•</span>
                                            <span>{formatDate(post.createdAt)}</span>
                                        </div>
                                    </div>
                                </div>
                                <p className="mt-3">{post.content}</p>
                                {post.media.length > 0 && (
                                    <div className="mt-3 grid grid-cols-2 gap-2">
                                        {post.media.map((media) => (
                                            <div key={media.id} className="media">
                                                {media.mediaType === 1 ? (
                                                    <img
                                                        src={media.mediaUrl}
                                                        alt="Post Media"
                                                        className="rounded-lg w-full"
                                                    />
                                                ) : (
                                                    <video controls className="rounded-lg w-full">
                                                        <source src={media.mediaUrl} type="video/mp4" />
                                                    </video>
                                                )}
                                            </div>
                                        ))}
                                    </div>
                                )}

                                <div className="flex justify-between items-center mt-4 text-gray-600">
                                    <button className="flex items-center space-x-1 hover:text-red-500">
                                        <Heart size={20} />
                                        <span>Thích</span>
                                    </button>
                                    <button className="flex items-center space-x-1 hover:text-blue-500">
                                        <MessageCircle size={20} />
                                        <span>Bình luận</span>
                                    </button>
                                    <button className="flex items-center space-x-1 hover:text-green-500">
                                        <Share size={20} />
                                        <span>Chia sẻ</span>
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
}
