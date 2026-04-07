import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Users() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const result = await axios.get("http://localhost:8080/person");
      console.log("Users fetched:", result.data); // Log the fetched data
      setUsers(result.data);
    } catch (error) {
      console.error("Error loading users:", error);
    }
  };

  const handleDeleteUser = async (id) => {
    // Confirm deletion with the user
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this user?"
    );
    if (!confirmDelete) return;

    try {
      await axios.delete(`http://localhost:8080/person/delete/${id}`);
      // Reload the users list after deletion
      loadUsers();
      alert("User deleted successfully!");
    } catch (error) {
      console.error("Error deleting user:", error);
      alert("There was an error deleting the user. Please try again.");
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>User List</h1>
        <Link to="/create-user">
          <button className="btn btn-primary">Create User</button>
        </Link>
      </div>
      {users.length === 0 ? (
        <p className="text-center">No users found.</p>
      ) : (
        <div className="row">
          {users.map((user, index) => (
            <div className="col-md-6" key={user.id}>
              <div className="card mb-3 shadow-sm">
                <div className="card-body">
                  <h5 className="card-title">
                    <strong>Username:</strong> {user.name}
                  </h5>
                  <p className="card-text">
                    <strong>Age:</strong> {user.age}
                  </p>
                  <div className="d-flex justify-content-between">
                    <Link to={`/update-user/${user.id}`}>
                      <button className="btn btn-warning">Update</button>
                    </Link>
                    <button
                      className="btn btn-danger"
                      onClick={() => handleDeleteUser(user.id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
