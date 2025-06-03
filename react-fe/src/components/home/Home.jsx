import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchHomePostsStart, fetchHomePostsSuccess, fetchPostsFailure, updatePostLike } from "../../redux/slices/PostSlice";
import Header from "../shared/Header";
import Home_SidebarL from "./Home_SidebarL";
import { getPost, likePost, unlikePost } from "../../services/APIService";
import formatDate from "../../utils/DateUtil";
import {
    Globe, Footprints, UserRound, Lock, Heart, MessageCircle, Share
} from 'lucide-react';

export default function Home() {
    const dispatch = useDispatch();
    const { homePosts } = useSelector((state) => state.posts);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                dispatch(fetchHomePostsStart());
                const response = await getPost();
                if (response?.data?.response) {
                    dispatch(fetchHomePostsSuccess(response.data.response));
                } else {
                    dispatch(fetchPostsFailure("Invalid API data!"));
                }
            } catch (error) {
                dispatch(fetchPostsFailure(error.message || "Unknown error"));
            }
        };
        fetchPosts();
    }, [dispatch]);

    const handleLikeToggle = async (postId, isLiked) => {
        try {
            if (isLiked) {
                await unlikePost(postId);
                dispatch(updatePostLike({ postId, isLiked: false }));
            } else {
                await likePost(postId);
                dispatch(updatePostLike({ postId, isLiked: true }));
            }
        } catch (error) {
            console.error("Error toggling like:", error);
            toast.error("Failed to update like status. Please try again.");
        }
    };

    const getVisibilityLabel = (level) => {
        switch (level) {
            case 1:
                return { label: "Public", icon: <Globe size={17} /> };
            case 2:
                return { label: "Followers", icon: <Footprints size={17} /> };
            case 3:
                return { label: "Friends", icon: <UserRound size={17} /> };
            case 4:
                return { label: "Private", icon: <Lock size={17} /> };
            default:
                return { label: "Unknown", icon: null };
        }
    };

    return (
        <div>
            <div className="bg-gray-100 min-h-screen">
                <Header />
                <Home_SidebarL />
                <div className="pt-20 max-w-2xl mx-auto">
                    {(!homePosts || homePosts.length === 0) ? (
                        <p className="text-center text-gray-500">No post yet.</p>
                    ) : (
                        homePosts.map((post) => (
                            <div key={post.id} className="bg-white rounded-lg shadow-md p-4 my-4">
                                <div className="flex items-start space-x-3">
                                    <img
                                        src={post?.postOwnerAvatarUrl || "../../../default-avatar.jpg"}
                                        alt="Avatar"
                                        className="w-10 h-10 rounded-full"
                                    />
                                    <div>
                                        <span className="font-semibold block">{post.fullName}</span>
                                        <div className="text-sm text-gray-500 flex items-center space-x-2">
                                            <span>@{post.userName}</span>
                                            <span>•</span>
                                            <span>{formatDate(post.createdAt)}</span>
                                            <label className="items-center gap-2" title={getVisibilityLabel(post.visibilityLevel).label}>
                                                {getVisibilityLabel(post.visibilityLevel).icon}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <p className="mt-3">{post.content}</p>
                                {post.media && post.media.length > 0 && (
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
                                <div className="text-sm mt-2 font-medium flex justify-between">
                                    <p>{post.postLikeCount} Likes</p>
                                    <p>{post.postCommentCount} Comments</p>
                                </div>
                                <div className="flex justify-between items-center mt-4 text-gray-600">
                                    <button
                                        className={`flex cursor-pointer items-center space-x-1 ${post.isLiked ? "text-red-500" : "hover:text-red-500"
                                            }`}
                                        onClick={() => handleLikeToggle(post.id, post.isLiked)}
                                    >
                                        {post.isLiked ? (
                                            <Heart fill="red" size={20} />
                                        ) : (
                                            <Heart size={20} />
                                        )}
                                        <span className="text-sm">Like</span>
                                    </button>
                                    <button className="flex cursor-pointer items-center space-x-1 hover:text-blue-500">
                                        <MessageCircle size={20} />
                                        <span className="text-sm">Comments</span>
                                    </button>
                                    <button className="flex cursor-pointer items-center space-x-1 hover:text-green-500">
                                        <Share size={20} />
                                        <span className="text-sm">Share</span>
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