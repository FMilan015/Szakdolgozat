import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import './components-style/Header.css';

interface HeaderProps {
    isAuthenticated: boolean;
}

const Header: React.FC<HeaderProps> = ({ isAuthenticated }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/home');
        window.location.reload();
    };

    return (
        <header className="header">
            <div className="logo">
                <Link to="/home">TradeHeaven</Link>
            </div>
            <nav className="nav">
                <ul>
                    <li><Link to="/news">News</Link></li>
                    <li><Link to="/tutorials">Tutorials</Link></li>
                    <li><Link to="/trading-tools">Trading tools</Link></li>
                    <li><Link to="/price-data">Price data</Link></li>
                </ul>
            </nav>
            <div className="client-area">
                {isAuthenticated ? (
                    <>
                        <button onClick={handleLogout} className="logout-btn" aria-label="Logout">
                            <FontAwesomeIcon icon={faSignOutAlt} className="logout-icon" />
                        </button>
                        <Link to="/profile" className="user-container">
                            <FontAwesomeIcon icon={faUser} className="user-icon" />
                        </Link>
                    </>
                ) : (
                    <Link to="/profile" className="user-container">
                        <FontAwesomeIcon icon={faUser} className="user-icon" />
                    </Link>
                )}
            </div>
        </header>
    );
};

export default Header;
