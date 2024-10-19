import React, { useState, useEffect } from 'react';
import api from '../config/axiosConfig';
import './pages-style/Pages.css';

const PriceData: React.FC = () => {
    const [category, setCategory] = useState<string | null>(null);
    const [data, setData] = useState<any[]>([]);
    const [page, setPage] = useState(1);

    const fetchData = async (type: string, page: number) => {
        let url = '';
        if (type === 'CRYPTO') {
            url = `/api/crypto?page=${page}`;
        } else if (type === 'STOCK') {
            url = `/api/stocks?page=${page}`;
        }

        try {
            const response = await api.get(url);
            setData(response.data);
        } catch (error) {
            console.error("Error fetching data", error);
            setData([]);
        }
    };

    useEffect(() => {
        if (category) {
            fetchData(category, page);
        }
    }, [category, page]);

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
            <div className="category-buttons">
                <button onClick={() => {
                    setCategory('CRYPTO');
                    setPage(1);
                }} className={category === 'CRYPTO' ? 'active-button' : ''}>CRYPTO
                </button>
                <button onClick={() => {
                    setCategory('STOCK');
                    setPage(1);
                }} className={category === 'STOCK' ? 'active-button' : ''}>STOCK
                </button>
            </div>

            {!category ? (
                <p></p>
            ) : (
                <table className="price-table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Symbol</th>
                        <th>Price</th>
                        <th>24h %</th>
                        {category === 'CRYPTO' && <th>Market Cap</th>}
                        <th>Volume (24h)</th>
                    </tr>
                    </thead>
                    <tbody>
                    {data.length > 0 ? (
                        data.map((item: any, index: number) => (
                            <tr key={index}>
                                <td>{(page - 1) * 10 + index + 1}</td>
                                <td>
                                    {category === 'CRYPTO' && item.image ? (
                                        <img src={item.image} alt={item.symbol} style={{ width: '30px', height: '30px', marginRight: '10px' }} />
                                    ) : null}
                                    {item.symbol?.toUpperCase() || 'N/A'}
                                </td>
                                <td>${category === 'CRYPTO' ? item.current_price?.toFixed(2) : item.close?.toFixed(2) || 'N/A'}</td>
                                <td style={{ color: category === 'CRYPTO' ? (item.price_change_percentage_24h >= 0 ? 'green' : 'red') : (item.price_change_percentage_1d >= 0 ? 'green' : 'red') }}>
                                    {category === 'CRYPTO' ? (item.price_change_percentage_24h?.toFixed(2) || 'N/A') : (item.price_change_percentage_1d?.toFixed(2) || 'N/A')}%
                                </td>
                                {category === 'CRYPTO' && <td>${item.market_cap?.toLocaleString() || 'N/A'}</td>}
                                <td>${item.total_volume?.toLocaleString() || 'N/A'}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={category === 'CRYPTO' ? 6 : 5} style={{ textAlign: 'center' }}>
                                No data available
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            )}

            {category && data.length > 0 && (
                <div className="pagination">
                    <button onClick={prevPage} disabled={page === 1}>Previous</button>
                    <span>Page {page}</span>
                    <button onClick={nextPage}>Next</button>
                </div>
            )}
        </div>
    );
};

export default PriceData;
