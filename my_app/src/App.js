import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Cookies from "js-cookie";
import Users from "./Users"; // Make sure this path is correct
import AdminHome from "./components/AdminHome";
import Devices from "./Devices";
import CreateUser from "./components/CreateUser";
import CreateDevice from "./components/CreateDevice";
import UpdateUser from "./components/UpdateUser";
import Login from "./components/Login";
import Navbar from "./components/Navbar";
import UpdateUserByAdmin from "./admin_components/UpdateUserByAdmin";
import UpdateDeviceByAdmin from "./admin_components/UpdateDeviceByAdmin";
import Home from "./Home";
import UserDevices from "./components/UserDevices";
import AssociateDevice from "./admin_components/AssociateDevice";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    // Check if user is already logged in
    const personId = Cookies.get("personId");
    if (personId) {
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogout = () => {
    // Remove personId cookie on logout
    Cookies.remove("personId");
    setIsLoggedIn(false);
    console.log("User logged out.");
  };

  return (
    <Router>
      <Navbar isLoggedIn={isLoggedIn} onLogout={handleLogout} />
      <div className="App">
        <header className="App-header">
          {/* Your existing header content */}
        </header>
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/users" element={<Users />} />
            <Route path="/devices" element={<Devices />} />
            <Route path="/admin" element={<AdminHome />} />
            <Route path="/create-user" element={<CreateUser />} />
            <Route path="/create-device" element={<CreateDevice />} />
            <Route path="/update-user" element={<UpdateUser />} />
            <Route path="/login" element={<Login />} />
            <Route
              path="/update-user/:userId"
              element={<UpdateUserByAdmin />}
            />
            <Route
              path="/update-device/:deviceId"
              element={<UpdateDeviceByAdmin />}
            />
            <Route path="/person/:personId/devices" element={<UserDevices />} />
            <Route
              path="/associate-device/:userId"
              element={<AssociateDevice />}
            />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
