import { useState, useEffect } from "react";
import { getPost } from "../../services/APIService";
import { Heart, MessageCircle, Share } from "lucide-react";
import formatDate from "../../utils/DateUtil";

export default function Posts() {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await getPost();
                if (response?.data?.response) {
                    setPosts(response.data.response);
                }
            } catch (error) {
                console.error("Error fetching posts:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchPosts();
    }, []);

    if (loading) {
        return <p className="text-center">Loading posts...</p>;
    }
    return (
        <div>
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
    );
}