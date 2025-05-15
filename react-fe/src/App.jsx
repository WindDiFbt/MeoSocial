import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import LandingPage from "./components/home/LandingPage";
import LoginPage from "./components/auth/Login";
import HomePage from "./components/home/Home";
import RegisterPage from "./components/auth/Register";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
      <ToastContainer
        position="bottom-left"
        autoClose={3000} />
    </Router>
  );
}

export default App;
