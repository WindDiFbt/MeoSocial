import axios from '../utils/ApiUtil'

export const login = (identifier, password) => {
    return axios.post(`/auth/login`, { identifier, password }, { skipAuthRefresh: true });
}

export const getPost = () => {
    return axios.get(`/post`)
}

export const logout = () => {
    return axios.post(`/auth/logout`)
}

export const register = (username, password) => {
    return axios.post(`/auth/register`, { username, password });
}

export const getUserInfo = () => {
    return axios.get(`/user/profile`);
}

export const getUserPostMedia = (userId) => {
    return axios.get(`/post-media/user/${userId}`);
}