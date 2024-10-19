import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import News from './pages/News';
import Tutorials from './pages/Tutorials';
import TradingTools from './pages/TradingTools';
import PriceData from './pages/PriceData';
import Profile from './pages/Profile';
import Login from './pages/authentication/Login';
import Register from './pages/authentication/Registration';
import './src-style/App.css';
import ProtectedRoute from "./components/ProtectedRoute";

const App: React.FC = () => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const checkAuthentication = () => {
            const storedToken = localStorage.getItem('token');
            setIsAuthenticated(!!storedToken);
        };

        checkAuthentication();
        window.addEventListener('storage', checkAuthentication);

        return () => {
            window.removeEventListener('storage', checkAuthentication);
        };
    }, []);

    return (
        <Router>
            <div className="App">
                <Header isAuthenticated={isAuthenticated} />

                <Routes>
                    <Route path="/" element={<Navigate to="/home" />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/news" element={
                        <ProtectedRoute isAuthenticated={isAuthenticated}>
                            <News />
                        </ProtectedRoute>
                    } />
                    <Route path="/tutorials" element={
                        <ProtectedRoute isAuthenticated={isAuthenticated}>
                            <Tutorials />
                        </ProtectedRoute>
                    } />
                    <Route path="/trading-tools" element={
                        <ProtectedRoute isAuthenticated={isAuthenticated}>
                            <TradingTools />
                        </ProtectedRoute>
                    } />
                    <Route path="/price-data" element={
                        <ProtectedRoute isAuthenticated={isAuthenticated}>
                            <PriceData />
                        </ProtectedRoute>
                    } />
                    <Route path="/profile" element={
                        <ProtectedRoute isAuthenticated={isAuthenticated}>
                            <Profile />
                        </ProtectedRoute>
                    } />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
