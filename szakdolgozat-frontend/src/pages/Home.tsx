import React from 'react';
import './pages-style/Home.css';

const Home: React.FC = () => {
    return (
        <div className="home">
            <div className="hero-section">
                <h1>Welcome to TradeHeaven</h1>
                <p>Access real-time stock news, tutorials, trading tools, portfolio management features, and price data.</p>
                <button className="cta-button">Start trading now</button>
            </div>
        </div>
    );
};

export default Home;
