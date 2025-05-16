import axios from '../utils/ApiUtil'

export const login = (identifier, password) => {
    return axios.post("/auth/login", { identifier, password }, {skipAuthRefresh: true});
}

export const getPost = () =>{
    return axios.get("/post")
}

export const logout = () => {
    return axios.post("/auth/logout")
}

export const register = (username, password) => {
    return axios.post("/auth/register", { username, password });
}
