import { createSlice } from "@reduxjs/toolkit";

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
} = postSlice.actions;
export default postSlice.reducer;