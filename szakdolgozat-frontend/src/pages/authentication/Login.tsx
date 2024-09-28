import React from 'react';
import { Link } from 'react-router-dom';
import './authentication-style/Auth.css';

const Login: React.FC = () => {
    return (
        <div className="auth-page">
            <h1>Login</h1>
            <form>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" placeholder="Enter your email" />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" placeholder="Enter your password" />
                </div>
                <button type="submit" className="btn">Login</button>
            </form>
            <p>
                Don't have an account? <Link to="/register" className="register-link">Register here</Link>
            </p>
        </div>
    );
};

export default Login;
