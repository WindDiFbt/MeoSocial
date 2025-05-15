import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { login } from "../../services/APIService"
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";
import { login as loginAction } from "../../redux/slices/AuthSlice";

const LoginPage = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [remember, setRemember] = useState(false);
    const dispatch = useDispatch();
    useEffect(() => {
        const savedIdentifier = localStorage.getItem("savedIdentifier");
        const savedPassword = localStorage.getItem("savedPassword");
        if (savedIdentifier && savedPassword) {
            setIdentifier(savedIdentifier);
            setPassword(savedPassword);
            setRemember(true);
        }
    }, []);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        if (identifier === '' || password === '') {
            toast.error('Username or password blank');
            return;
        }
        try {
            const response = await login(identifier.trim(), password.trim());
            if (response.data.status === "401 UNAUTHORIZED") {
                toast.error("Your account does not exist.");
                return;
            }
            const { accessToken, id, username, roles } = response.data.response;
            sessionStorage.setItem("accessToken", accessToken);
            localStorage.setItem("id", id);
            localStorage.setItem("username", username);
            localStorage.setItem("roles", JSON.stringify(roles));
            dispatch(loginAction({ id, username, roles }));
            if (remember) {
                localStorage.setItem("savedIdentifier", identifier);
                localStorage.setItem("savedPassword", password);
            } else {
                localStorage.removeItem("savedIdentifier");
                localStorage.removeItem("savedPassword");
            }
            if (roles.includes("ROLE_USER")) {
                toast.success("User login successfully!");
                navigate('/home');
            } else if (roles.includes("ROLE_ADMIN")) {
                toast.success("Admin login successfully!");
                navigate('/');
            }
        } catch (error) {
            toast.error('Wrong Username or password!');
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <div className="bg-white p-8 shadow-lg rounded-lg w-96">
                <h2 className="text-2xl font-bold text-center text-gray-700">Đăng nhập</h2>
                <form onSubmit={handleLogin} className="mt-4">
                    <div className="mb-4">
                        <label className="block text-gray-600">Tên đăng nhập hoặc email</label>
                        <input
                            type="text"
                            className="w-full px-4 py-2 border rounded-md"
                            value={identifier}
                            onChange={(e) => setIdentifier(e.target.value)}
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block text-gray-600">Mật khẩu</label>
                        <input
                            type="password"
                            className="w-full px-4 py-2 border rounded-md"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div className="mb-4">
                        <label className="inline-flex items-center">
                            <input
                                type="checkbox"
                                className="form-checkbox"
                                checked={remember}
                                onChange={(e) => setRemember(e.target.checked)}
                            />
                            <span className="ml-2 text-gray-600">Remember Me</span>
                        </label>
                    </div>
                    <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-md">
                        Đăng nhập
                    </button>
                </form>
                <p className="mt-4 text-center text-gray-600">
                    Chưa có tài khoản?{" "}
                    <Link to="/register" className="text-green-500 hover:underline">
                        Đăng ký
                    </Link>
                </p>
                <p className="mt-2 text-center text-gray-600 text-sm">
                    <Link to="/" className="text-blue-500 hover:underline">
                        Quên mật khẩu?
                    </Link>
                </p>
                <p className="mt-2 text-center text-gray-600">
                    <Link to="/" className="text-blue-500 hover:underline">
                        Trang chủ
                    </Link>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
