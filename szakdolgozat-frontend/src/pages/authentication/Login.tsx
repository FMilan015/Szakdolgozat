import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import './authentication-style/Auth.css';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setErrorMessage(null);

        try {
            const response = await axios.post('http://localhost:8080/api/auth/authenticate', {
                username,
                password
            });

            if (response.status === 200) {
                window.localStorage.setItem('token', response.data.token);
                navigate('/home');
                window.location.reload();
            }
        } catch (error) {
            console.error('Login failed', error);
            setErrorMessage("Login failed. Please check your username and password.");
        }
    };

    return (
        <div className="auth-page">
            <h1>Login</h1>
            <form onSubmit={handleLogin}>
                <div className="form-group">
                    <label>Username</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Enter your username" required />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Enter your password" required />
                </div>

                {}
                {errorMessage && (
                    <div className="error-message">
                        {errorMessage}
                    </div>
                )}

                <button type="submit" className="btn">Login</button>
            </form>
            <p>
                Don't have an account? <Link to="/register" className="register-link">Register here</Link>
            </p>
        </div>
    );
};

export default Login;
