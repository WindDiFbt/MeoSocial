import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../services/APIService";
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";
import { setEmailForVerification } from "../../redux/slices/AuthSlice";
import { Eye, EyeClosed, Check } from 'lucide-react';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errors, setErrors] = useState([]);
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const dispatch = useDispatch();

    const handleRegister = async (e) => {
        e.preventDefault();
        setErrors([]);
        if (password !== confirmPassword) {
            setErrors(["Passwords do not match!"]);
            return;
        }
        try {
            const response = await register(email.trim(), username.trim(), password.trim());
            if (response && response.data.status === "200 OK") {
                dispatch(setEmailForVerification(email.trim()));
                toast.success("Please verify your email.");
                navigate('/verify');
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
    const isPasswordMatch = password === confirmPassword && confirmPassword.length > 0;

    return (
        <div className="flex min-h-full items-center flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img className="mx-auto h-25 w-auto" src="/meosocial_logo.png" alt="MeoSocial" />
                <h2 className="mt-5 text-center text-2xl font-bold tracking-tight text-gray-900">
                    Create your account
                </h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form onSubmit={handleRegister} className="space-y-6">
                    <div>
                        <label className="block font-medium text-gray-900">Username</label>
                        <div className="mt-2">
                            <input
                                type="text"
                                required
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block font-medium text-gray-900">Email</label>
                        <div className="mt-2">
                            <input
                                type="email"
                                required
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm"
                                value={email}
                                autoComplete="email"
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block font-medium text-gray-900">Password</label>
                        <div className="mt-2 relative">
                            <input
                                type={showPassword ? "text" : "password"}
                                required
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm"
                                value={password}
                                autoComplete="new-password"
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute inset-y-0 right-3 flex items-center text-gray-500 hover:text-gray-700"
                            >
                                {showPassword ? <Eye size={20}></Eye> : <EyeClosed size={20}></EyeClosed>}
                            </button>
                        </div>
                    </div>

                    <div>
                        <label className="block font-medium text-gray-900">Confirm Password
                            <span className="absolute ml-2">{isPasswordMatch ? <Check color="green"></Check> : ""}</span>
                        </label>
                        <div className="mt-2 relative">
                            <input
                                type={showConfirmPassword ? "text" : "password"}
                                required
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm"
                                value={confirmPassword}
                                autoComplete="new-password"
                                onChange={(e) => setConfirmPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                className="absolute inset-y-0 right-3 flex items-center text-gray-500 hover:text-gray-700"
                            >
                                {showConfirmPassword ? <Eye size={20}></Eye> : <EyeClosed size={20}></EyeClosed>}
                            </button>
                        </div>
                    </div>

                    <div>
                        <button type="submit" className="flex w-full cursor-pointer justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                            Sign up
                        </button>
                    </div>
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

                <p className="mt-5 text-center text-gray-500">
                    Already have an account?
                    <a href="/login" className="ml-1 font-semibold text-indigo-600 hover:text-indigo-500">
                        Sign in
                    </a>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;