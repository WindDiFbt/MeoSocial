import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import LandingPage from "./components/home/LandingPage";
import LoginPage from "./components/auth/Login";
import Home from "./components/home/Home";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<Home />} />
      </Routes>
      <ToastContainer></ToastContainer>
    </Router>
  );
}

export default App;
