import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { verifyEmail, resendVerifyCode } from "../../services/APIService";
import { useSelector } from "react-redux";

const VerifyPage = () => {
    const email = useSelector((state) => state.auth.emailForVerification);
    const navigate = useNavigate();

    const [code, setCode] = useState('');
    const [errors, setErrors] = useState([]);
    const [countdown, setCountdown] = useState(120);
    const hasToastedRef = useRef(false);

    useEffect(() => {
        if (!email && !hasToastedRef.current) {
            toast.error("No email provided for verification. Please register first.");
            navigate('/register');
            hasToastedRef.current = true;
        }
        let timer;
        if (countdown > 0) {
            timer = setInterval(() => {
                setCountdown((prev) => prev - 1);
            }, 1000);
        }
        return () => clearInterval(timer);
    }, [email, navigate, countdown]);

    const formatTime = (seconds) => {
        const minutes = Math.floor(seconds / 60);
        const remainingSeconds = seconds % 60;
        return `${minutes}:${remainingSeconds < 10 ? `0${remainingSeconds}` : remainingSeconds}`;
    }

    const handleVerify = async (e) => {
        e.preventDefault();
        setErrors([]);
        if (!code.trim()) {
            setErrors(["Verification code is required."]);
            return;
        }
        try {
            const response = await verifyEmail(email.trim(), code.trim());
            if (response && response.data.status === "200 OK") {
                navigate('/login', { state: { fromVerify: true } });
            } else {
                setErrors([response?.data?.message || "Verification failed!"]);
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

    const handleResendeVerifyCode = async (e) => {
        e.preventDefault();
        setErrors([]);
        if (!email) {
            setErrors(["No email provided for verification."]);
            return;
        }
        try {
            const response = await resendVerifyCode(email.trim());
            toast.success("Verification code resent!");
            setCountdown(120);
        } catch (error) {
            setErrors(["Failed to resend verification code. Please try again later."]);
        }
    };

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-100">
            <div className="bg-white p-8 shadow-lg rounded-lg w-96">
                <img className="mx-auto h-20 w-auto mb-4" src="/meosocial_logo.png" alt="MeoSocial" />
                <h2 className="text-2xl font-bold text-center text-gray-700">Verify email</h2>
                <p className="text-center text-gray-500 mt-2 text-sm">Please enter the verification code sent to your email: {email}.</p>

                <form onSubmit={handleVerify} className="mt-6 space-y-4">
                    <div>
                        <label className="block text-gray-600">Verification code</label>
                        <input
                            type="text"
                            className="w-full px-4 py-2 border rounded-md focus:outline-indigo-500"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full bg-indigo-600 text-white py-2 rounded-md font-semibold hover:bg-indigo-500 transition"
                    >
                        Confirm
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

                <p className="mt-4 text-center text-gray-600 text-sm">
                    Didn't receive code?
                    {countdown > 0 ? (
                        <span className="ml-1 text-gray-500">
                            Resend available in {formatTime(countdown)}
                        </span>
                    ) : (
                        <a
                            href="#"
                            onClick={handleResendeVerifyCode}
                            className="ml-1 text-indigo-600 font-semibold hover:underline"
                        >
                            Resend
                        </a>
                    )}
                </p>
            </div>
        </div>
    );
};

export default VerifyPage;
