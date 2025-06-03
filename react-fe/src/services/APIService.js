import axios from '../utils/ApiUtil'

// Authentication
export const login = (identifier, password) => {
    return axios.post(`/auth/login`, { identifier, password }, { skipAuthRefresh: true });
}

export const logout = () => {
    return axios.post(`/auth/logout`)
}

export const register = (email, username, password) => {
    return axios.post(`/auth/register`, { email, username, password });
}

export const verifyEmail = (email, code) => {
    return axios.post(`/auth/verify`, { email, code });
}

export const resendVerifyCode = (email) => {
    return axios.post(`/auth/resend-verification`, { email });
}

// User
export const getUserInfo = () => {
    return axios.get(`/user/profile`);
}

// Post
export const getPost = () => {
    return axios.get(`/post`)
}

export const getUserPostMedia = (userId) => {
    return axios.get(`/post-media/user/${userId}`);
}

export const createPost = (content, visibilityLevel) => {
    return axios.post(`/post/new`, { content, visibilityLevel });
}

export const createPostMedia = (postId, file) => {
    const formData = new FormData();
    formData.append('file', file);
    return axios.post(`/post-media/add/${postId}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

export const likePost = (postId) => {
    return axios.post(`/post/like/${postId}`);
}

export const unlikePost = (postId) => {
    return axios.post(`/post/unlike/${postId}`);
}