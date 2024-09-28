import React from 'react';
import './components-style/Header.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';

const Header: React.FC = () => {
    return (
        <header className="header">
            <div className="logo">
                <Link to="/home">TradeHeaven</Link>
            </div>
            <nav className="nav">
                <ul>
                    <li><Link to="/real-time-news">Real-Time news</Link></li>
                    <li><Link to="/tutorials">Tutorials</Link></li>
                    <li><Link to="/trading-tools">Trading tools</Link></li>
                    <li><Link to="/portfolio-management">Portfolio management</Link></li>
                    <li><Link to="/price-data">Price data</Link></li>
                </ul>
            </nav>
            <div className="client-area">
                <Link to="/login" className="user-container">
                    <FontAwesomeIcon icon={faUser} className="user-icon" />
                </Link>
            </div>
        </header>
    );
};

export default Header;
