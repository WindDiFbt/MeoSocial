import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import LandingPage from "./components/home/LandingPage";
import LoginPage from "./components/auth/Login";
import HomePage from "./components/home/Home";
import RegisterPage from "./components/auth/Register";
import Profile from "./components/profile/Profile";
import Posts from "./components/profile/Posts";
import Media from "./components/profile/Media";
import Friends from "./components/profile/Friends";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/profile" element={<Profile />}>
          <Route index element={<Navigate to="posts" replace />} />
          <Route path="posts" element={<Posts />} />
          <Route path="media" element={<Media />} />
          <Route path="friends" element={<Friends />} />
        </Route>
      </Routes>
      <ToastContainer
        position="bottom-left"
        autoClose={2000} />
    </Router>
  );
}

export default App;
