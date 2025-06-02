import { useState, useEffect, useRef } from "react";
import { useNavigate, Link, useLocation } from "react-router-dom";
import { login } from "../../services/APIService"
import { toast } from "react-toastify";
import { useDispatch } from "react-redux";
import { clearEmailForVerification } from "../../redux/slices/AuthSlice";
import { login as loginAction } from "../../redux/slices/AuthSlice";
import { Eye, EyeClosed } from 'lucide-react';

const LoginPage = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [remember, setRemember] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);
    const [showPassword, setShowPassword] = useState(false);
    const location = useLocation();
    const hasToastedRef = useRef(false);

    useEffect(() => {
        const savedIdentifier = localStorage.getItem("savedIdentifier");
        const savedPassword = localStorage.getItem("savedPassword");
        if (savedIdentifier && savedPassword) {
            setIdentifier(savedIdentifier);
            setPassword(savedPassword);
            setRemember(true);
        }
        if (location.state?.fromVerify && !hasToastedRef.current) {
            toast.success("Account verified successfully. Please log in.");
            dispatch(clearEmailForVerification());
            hasToastedRef.current = true;
        }
    }, [location, dispatch]);

    const handleLogin = async (e) => {
        e.preventDefault();
        setErrors([]);

        if (identifier === '' || password === '') {
            toast.error('Username or password blank');
            return;
        }
        try {
            const response = await login(identifier.trim(), password.trim());
            if (response && response.data.status === "200 OK") {
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
            } else {
                toast.error(response?.data?.message || "Login failed!");
            }
        } catch (error) {
            setErrors(['Wrong Username or password!']);
        }
    };

    return (
        <div className="flex min-h-full items-center flex-col justify-center px-6 py-12 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img className="mx-auto h-25 w-auto" src="/meosocial_logo.png" alt="MeoSocial" />
                <h2 className="mt-5 text-center text-2xl/9 font-bold tracking-tight text-gray-900">Sign in to your account</h2>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form className="space-y-6" onSubmit={handleLogin}>
                    <div>
                        <label htmlFor="identifier" className="block font-medium text-gray-900">Email / Phone number / Username</label>
                        <div className="mt-2">
                            <input type="text" required
                                id="identifier"
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                                value={identifier}
                                autoComplete="username"
                                onChange={(e) => setIdentifier(e.target.value)}
                            />
                        </div>
                    </div>
                    <div>
                        <div className="flex items-center justify-between">
                            <label htmlFor="password" className="block font-medium text-gray-900">Password</label>
                            <div className="text-sm">
                                <a href="#" className="font-semibold text-indigo-600 hover:text-indigo-500">Forgot password?</a>
                            </div>
                        </div>
                        <div className="mt-2 relative">
                            <input
                                type={showPassword ? "text" : "password"}
                                id="password"
                                required
                                className="block w-full rounded-md bg-white px-3 py-2 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                                value={password}
                                autoComplete="current-password"
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
                    <div className="mb-4 flex justify-end">
                        <span className="mr-2 text-gray-600 font-medium">Remember Me</span>
                        <label className="inline-flex items-center">
                            <input
                                type="checkbox"
                                className="form-checkbox"
                                checked={remember}
                                onChange={(e) => setRemember(e.target.checked)}
                            />
                        </label>
                    </div>
                    <div>
                        <button type="submit" className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Sign in</button>
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
                    Not a member?
                    <Link to="/register" className="ml-1 font-semibold text-indigo-600 hover:text-indigo-500">Sign up</Link>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
