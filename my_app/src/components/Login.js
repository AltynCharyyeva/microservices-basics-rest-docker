import React, { useState } from "react";
import axios from "axios";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage(""); // Reset message on new attempt

    try {
      const response = await axios.post("http://localhost:8080/person/login", {
        name: username,
        password,
      });

      // Assuming your backend returns a user object with role and id
      const { personId, role } = response.data;

      // Set cookies based on response
      Cookies.set("personId", personId);
      Cookies.set("role", role);

      // Store username in localStorage after successful login
      localStorage.setItem("role", role);

      // Redirect based on role
      if (role === "ADMIN") {
        navigate("/admin");
      } else if (role === "CLIENT") {
        navigate(`/person/${personId}/devices`);
      } else {
        setMessage("Invalid role.");
      }
    } catch (error) {
      setMessage("Login failed. Please check your credentials.");
      console.error("Login error:", error);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Login</h2>
      {message && <div className="alert alert-warning">{message}</div>}
      <form onSubmit={handleLogin}>
        <div className="form-group mb-3">
          <label htmlFor="username">Username</label>
          <input
            type="text"
            className="form-control"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="form-group mb-3">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            className="form-control"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Login
        </button>
      </form>
    </div>
  );
}
