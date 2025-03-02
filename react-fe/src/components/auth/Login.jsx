import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/APIService"
import { toast } from "react-toastify";
const LoginPage = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        if (identifier === '' || password === '') {
            toast.error('Username or password blank');
        }
        try {
            const response = await login(identifier.trim(), password.trim());
            if (!response || !response.data.response) {
                toast.error("Your account does not exist.");
                return;
            }
            const { accessToken, refreshToken, id, username, roles } = response.data.response;
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
            localStorage.setItem("id", id);
            localStorage.setItem("username", username);
            localStorage.setItem("roles", JSON.stringify(roles));
            if (roles.includes("ROLE_USER")) {
                toast.success("User login successfully!");
                navigate('/');
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
                        <label className="block text-gray-600">Tên đăng nhập</label>
                        <input
                            type="text"
                            className="w-full px-4 py-2 border rounded-md"
                            value={identifier}
                            onChange={(e) => setIdentifier(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block text-gray-600">Mật khẩu</label>
                        <input
                            type="password"
                            className="w-full px-4 py-2 border rounded-md"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-md">
                        Đăng nhập
                    </button>
                </form>
            </div>
        </div>
    );
};

export default LoginPage;
