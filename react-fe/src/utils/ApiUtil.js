import axios from "axios";
import nProgress from "nprogress";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

const API_URL = import.meta.env.VITE_API_URL;
const REFRESH_URL = `${API_URL}/refresh`;

const API = axios.create({
    baseURL: API_URL,
    withCredentials: true,
});

let isRefreshing = false;
let refreshSubscribers = [];

const onRrefreshed = (token) => {
    refreshSubscribers.map((callback) => callback(token));
};

const addRefreshSubscriber = (callback) => {
    refreshSubscribers.push(callback);
};

API.interceptors.request.use(
    (config) => {
        nProgress.start();
        const token = sessionStorage.getItem("accessToken");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        nProgress.done();
        Promise.reject(error);
    }
);

API.interceptors.response.use(
    (response) => {
        nProgress.done();
        return response;
    },
    async (error) => {
        nProgress.done();
        const { config, response } = error;
        const originalRequest = config;     
        if (config.skipAuthRefresh) {
            return Promise.reject(error);
        }
        if (response && response.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise((resolve) => {
                    addRefreshSubscriber((token) => {
                        originalRequest.headers["Authorization"] = `Bearer ${token}`;
                        resolve(axios(originalRequest));
                    });
                });
            }
            originalRequest._retry = true;
            isRefreshing = true;
            try {
                const res = await axios.post(REFRESH_URL, {}, { withCredentials: true });
                const { accessToken } = res.data;
                sessionStorage.setItem("accessToken", accessToken);
                isRefreshing = false;
                onRrefreshed(accessToken);
                refreshSubscribers = [];
                originalRequest.headers["Authorization"] = `Bearer ${accessToken}`;
                return axios(originalRequest);
            } catch (refreshError) {
                isRefreshing = false;
                toast.error("Your session has expired. Please login again.");
                sessionStorage.removeItem("accessToken");
                useNavigate()("/login");
            }
        }
        return Promise.reject(error);
    }
);

export default API;