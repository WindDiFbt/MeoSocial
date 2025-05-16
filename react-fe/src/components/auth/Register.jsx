import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../services/APIService";
import { toast } from "react-toastify";

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState([]);
    const navigate = useNavigate();
    
    const handleRegister = async (e) => {
        e.preventDefault();
        setErrors([]);
        if (password !== confirmPassword) {
            setErrors(["Passwords do not match!"]);
            return;
        }
        try {
            const response = await register(username.trim(), password.trim());
            if (response && response.data.status === "200 OK") {
                toast.success("Registration successful! Please log in.");
                navigate('/login');
            } else {
                setErrors([response?.data?.message || "Registration failed!"]);
            }
        } catch (error) {
            const backendErrors = error?.response?.data?.message;
            if (Array.isArray(backendErrors)) {
                setErrors(backendErrors);
            } else if (typeof backendErrors === "string") {
                setErrors([backendErrors]);
            } else {
                setErrors(["An unexpected error occurred."]);
            }
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
                {errors.length > 0 && (
                    <div className="mt-4 bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded text-sm">
                        <ul className="list-disc pl-5">
                            {errors.map((error, index) => (
                                <li key={index}>{error}</li>
                            ))}
                        </ul>
                    </div>
                )}
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