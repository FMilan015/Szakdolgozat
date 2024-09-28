import React from 'react';
import './authentication-style/Auth.css'; // Ugyanaz a CSS fájl a közös stílusokhoz

const Registration: React.FC = () => {
    return (
        <div className="auth-page">
            <h1>Register</h1>
            <form>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" placeholder="Enter your email" />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" placeholder="Create a password" />
                </div>
                <div className="form-group">
                    <label>Confirm password</label>
                    <input type="password" placeholder="Confirm your password" />
                </div>
                <button type="submit" className="btn">Register</button>
            </form>
        </div>
    );
};

export default Registration;
