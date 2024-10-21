import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './pages-style/Pages.css';

const News: React.FC = () => {
    const [news, setNews] = useState<any[]>([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        const fetchNews = async () => {
            const token = localStorage.getItem('token');
            try {
                const response = await axios.get(`http://localhost:8080/api/news?page=${page}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setNews(response.data);
            } catch (error) {
                console.error("Error fetching news on frontend", error);
            }
        };
        fetchNews();
    }, [page]);

    const nextPage = () => {
        setPage(page + 1);
    };

    const prevPage = () => {
        if (page > 1) {
            setPage(page - 1);
        }
    };

    return (
        <div className="page">
            <div className="news-container">
                {news.length > 0 ? (
                    news.map((item, index) => (
                        <div key={index} className="news-item">
                            <h3><a href={item.url} target="_blank" rel="noopener noreferrer">{item.title}</a></h3>
                            <p>{item.summary}</p>
                            <small>{item.source} - {new Date(item.time_published).toLocaleString()}</small>
                        </div>
                    ))
                ) : (
                    <p>Loading...</p>
                )}
            </div>

            <div className="pagination">
                <button onClick={prevPage} disabled={page === 1}>Previous</button>
                <span>Page {page}</span>
                <button onClick={nextPage}>Next</button>
            </div>
        </div>
    );
};

export default News;
