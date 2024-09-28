import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import News from './pages/News';
import Tutorials from './pages/Tutorials';
import TradingTools from './pages/TradingTools';
import PortfolioManagement from './pages/PortfolioManagement';
import PriceData from './pages/PriceData';
import Login from './pages/authentication/Login';
import Register from './pages/authentication/Registration';
import './src-style/App.css';

const App: React.FC = () => {
    return (
        <Router>
            <div className="App">
                <Header />
                <Routes>
                    <Route path="/" element={<Navigate to="/home" />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/real-time-news" element={<News />} />
                    <Route path="/tutorials" element={<Tutorials />} />
                    <Route path="/trading-tools" element={<TradingTools />} />
                    <Route path="/portfolio-management" element={<PortfolioManagement />} />
                    <Route path="/price-data" element={<PriceData />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
