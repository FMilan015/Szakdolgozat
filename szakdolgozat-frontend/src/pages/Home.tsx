import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './pages-style/Home.css';

const Home: React.FC = () => {
    const [trendingNews, setTrendingNews] = useState<any[]>([]);
    const [trendingData, setTrendingData] = useState<any[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTrendingNews = async () => {
            try {
                const newsResponse = await axios.get('http://localhost:8080/api/news/trending');
                setTrendingNews(newsResponse.data.slice(0, 2));
            } catch (error) {
                console.error('Error fetching trending news on frontend', error);
            }
        };

        const fetchTrendingData = async () => {
            try {
                const dataResponse = await axios.get('http://localhost:8080/api/data/trending');
                setTrendingData(dataResponse.data.slice(0, 4));
            } catch (error) {
                console.error('Error fetching trending data on frontend', error);
            }
        };

        fetchTrendingNews();
        fetchTrendingData();
    }, []);

    return (
        <div className="home">
            <div className="hero-section">
                <h1>Welcome to TradeHeaven</h1>
                <p>Access real-time stock news, tutorials, trading tools and price data.</p>
            </div>

            <div className="trending-section">
                <div className="trending-news">
                    <h2>Trending news</h2>
                    {trendingNews.length > 0 ? (
                        trendingNews.map((newsItem, index) => (
                            <div className="news-item" key={index}>
                                <h3>{newsItem.title}</h3>
                                <p>{newsItem.summary}</p>
                                <a href={newsItem.url} target="_blank" rel="noopener noreferrer" className="news-link">Read more</a>
                            </div>
                        ))
                    ) : (
                        <p>Loading...</p>
                    )}
                    <p onClick={() => navigate('/news')} className="go-to-news">Go to News</p>
                </div>

                <div className="trending-data">
                    <h2>Trending data</h2>
                    {trendingData.length > 0 ? (
                        <table className="trending-data-table">
                            <thead>
                            <tr>
                                <th>Symbol</th>
                                <th>Price</th>
                                <th>24h %</th>
                            </tr>
                            </thead>
                            <tbody>
                            {trendingData.map((data, index) => (
                                <tr key={index} className="data-row">
                                    <td>{data.symbol}</td>
                                    <td>${data.price}</td>
                                    <td>{data.change}%</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>Loading...</p>
                    )}
                    <p onClick={() => navigate('/price-data')} className="go-to-price-data">Go to Price data</p>
                </div>
            </div>
        </div>
    );
};

export default Home;
