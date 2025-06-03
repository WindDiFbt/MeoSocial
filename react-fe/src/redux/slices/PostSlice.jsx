import { createSlice } from "@reduxjs/toolkit";

const updatePostInList = (list, postId, updater) => {
    return list.map((post) =>
        post.id === postId ? updater(post) : post
    );
};

const initialState = {
    homePosts: [],
    profilePosts: [],
    isLoading: false,
    error: null,
};

const postSlice = createSlice({
    name: "post",
    initialState,
    reducers: {
        fetchHomePostsStart: (state) => {
            state.isLoading = true;
        },
        fetchHomePostsSuccess: (state, action) => {
            state.homePosts = action.payload;
            state.isLoading = false;
        },
        fetchProfilePostsStart: (state) => {
            state.isLoading = true;
        },
        fetchProfilePostsSuccess: (state, action) => {
            state.profilePosts = action.payload;
            state.isLoading = false;
        },
        fetchPostsFailure: (state, action) => {
            state.error = action.payload;
            state.isLoading = false;
        },
        setPosts: (state, action) => {
            state.posts = action.payload;
        },
        addPost: (state, action) => {
            state.posts.push(action.payload);
        },
        updatePostLike(state, action) {
            const { postId, isLiked } = action.payload;
            const applyUpdate = (post) => ({
                ...post,
                isLiked,
                postLikeCount: post.postLikeCount + (isLiked ? 1 : -1),
            });
            state.homePosts = updatePostInList(state.homePosts, postId, applyUpdate);
            state.profilePosts = updatePostInList(state.profilePosts, postId, applyUpdate);
        },
    },
});

export const {
    fetchHomePostsStart,
    fetchHomePostsSuccess,
    fetchProfilePostsStart,
    fetchProfilePostsSuccess,
    fetchPostsFailure,
    setPosts,
    addPost,
    updatePostLike,
} = postSlice.actions;
export default postSlice.reducer;