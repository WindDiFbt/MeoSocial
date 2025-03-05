import axios from '../utils/ApiUtil'

export const login = (identifier, password) => {
    return axios.post("/api/v1/auth/login", { identifier, password });
}

export const getPost = () =>{
    return axios.get("/post")
}