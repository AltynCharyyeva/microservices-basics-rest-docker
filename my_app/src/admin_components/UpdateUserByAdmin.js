import React, { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

export default function UpdateUserByAdmin() {
  const { userId } = useParams(); // Get userId from URL params
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    address: "",
    age: "",
    password: "",
  });
  const [message, setMessage] = useState("");

  useEffect(() => {
    // If userId is missing, set error message
    if (!userId) {
      setMessage(
        "User ID not found. Please go back and select a user to update."
      );
      return;
    }

    // Fetch user details if userId is available
    const fetchUserDetails = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/person/${userId}`
        );
        console.log("Person password: ", response.data.password);
        console.log("Person name: ", response.data.name);
        setFormData({
          name: response.data.name || "Person name", // Ensure default values
          address: response.data.address || "Person address",
          age: response.data.age || "Person age",
          password: response.data.password || "Person password", // Password as plain text
        });
      } catch (error) {
        setMessage("Error fetching user details. Please try again.");
      }
    };

    fetchUserDetails();
  }, [userId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!userId) {
      setMessage("User ID is missing. Cannot update user.");
      return;
    }

    try {
      await axios.post(
        `http://localhost:8080/person/update/${userId}`,
        formData
      );
      setMessage("User updated successfully!");
    } catch (error) {
      setMessage("Error updating user. Please try again.");
      console.error("There was an error updating the user!", error);
    }
  };

  // Function to navigate to the AssociateDevice page
  const handleNavigateToAssociateDevice = () => {
    navigate(`/associate-device/${userId}`);
  };

  return (
    <div className="container mt-5">
      <h2>Update User</h2>
      {message && <div className="alert alert-warning mt-3">{message}</div>}
      {userId && (
        <form onSubmit={handleSubmit}>
          <div className="form-group mb-3">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              className="form-control"
              id="name"
              name="name"
              value={formData.name} // Controlled input
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group mb-3">
            <label htmlFor="address">Address</label>
            <input
              type="text"
              className="form-control"
              id="address"
              name="address"
              value={formData.address} // Controlled input
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group mb-3">
            <label htmlFor="age">Age</label>
            <input
              type="number"
              className="form-control"
              id="age"
              name="age"
              value={formData.age} // Controlled input
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group mb-3">
            <label htmlFor="password">Password</label>
            <input
              type="text" // Display as plain text
              className="form-control"
              id="password"
              name="password"
              value={formData.password} // Controlled input
              onChange={handleChange}
            />
          </div>

          <button type="submit" className="btn btn-primary">
            Update User
          </button>
          <button
            type="button"
            className="btn btn-secondary mt-3"
            onClick={handleNavigateToAssociateDevice}
          >
            Associate Device to Person
          </button>
        </form>
      )}
    </div>
  );
}
