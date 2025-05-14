import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../services/APIService";
import { toast } from "react-toastify";

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    // const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            toast.error("Passwords do not match!");
            return;
        }
        try {
            const response = await register(username, password);
            if (response && response.data.success) {
                toast.success("Registration successful! Please log in.");
                navigate('/login');
            } else {
                toast.error(response?.data?.message || "Registration failed!");
            }
        } catch (error) {
            toast.error("An error occurred during registration.");
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <div className="bg-white p-8 shadow-lg rounded-lg w-96">
                <h2 className="text-2xl font-bold text-center text-gray-700">Đăng ký</h2>
                <form onSubmit={handleRegister} className="mt-4">
                    <div className="mb-4">
                        <label className="block text-gray-600">Tên đăng nhập</label>
                        <input
                            type="text"
                            className="w-full px-4 py-2 border rounded-md"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    {/* <div className="mb-4">
                        <label className="block text-gray-600">Email</label>
                        <input
                            type="email"
                            className="w-full px-4 py-2 border rounded-md"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div> */}
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
                    <div className="mb-4">
                        <label className="block text-gray-600">Xác nhận mật khẩu</label>
                        <input
                            type="password"
                            className="w-full px-4 py-2 border rounded-md"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-md">
                        Đăng ký
                    </button>
                </form>
                <p className="mt-4 text-center text-gray-600">
                    Đã có tài khoản?{" "}
                    <a href="/login" className="text-blue-500 hover:underline">
                        Đăng nhập
                    </a>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;