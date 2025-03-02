import axios from "axios";
import nProgress from "nprogress";

const API = axios.create({
    baseURL: "http://localhost:8080",
    withCredentials: true,
});

API.interceptors.request.use(
    (config) => {
        nProgress.start();
        const token = localStorage.getItem("accessToken");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

API.interceptors.response.use(
    (response) => {
        nProgress.done();
        return response;
    },
    (error) => {
        nProgress.done();
        if (error.response) {
            toast.error(error.response.data.message || "Lỗi không xác định!");
        }
        return Promise.reject(error);
    }
);

export default API;