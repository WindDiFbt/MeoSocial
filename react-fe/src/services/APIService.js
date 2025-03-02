import axios from '../utils/Api'

export const login = (identifier, password) => {
    return axios.post("/api/v1/auth/login", { identifier, password }, {withCredentials: true});
}