import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './pages-style/Pages.css';

const PriceData: React.FC = () => {
    const [category, setCategory] = useState('CRYPTO');
    const [data, setData] = useState<any[]>([]);
    const [page, setPage] = useState(1); // Oldalszám változó

    const fetchData = async (type: string, page: number) => {
        let url = '';
        if (type === 'CRYPTO') {
            url = `http://localhost:8080/api/crypto?page=${page}`;
        } else if (type === 'STOCK') {
            url = `http://localhost:8080/api/stocks?page=${page}`;
        } else if (type === 'ETF') {
            url = `http://localhost:8080/api/etf?page=${page}`;
        }

        try {
            const response = await axios.get(url);
            setData(response.data);
        } catch (error) {
            console.error("Error fetching data", error);
        }
    };

    useEffect(() => {
        fetchData(category, page);
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
                <button onClick={() => { setCategory('CRYPTO'); setPage(1); }} className={category === 'CRYPTO' ? 'active-button' : ''}>CRYPTO</button>
                <button onClick={() => { setCategory('STOCK'); setPage(1); }} className={category === 'STOCK' ? 'active-button' : ''}>STOCK</button>
                <button onClick={() => { setCategory('ETF'); setPage(1); }} className={category === 'ETF' ? 'active-button' : ''}>ETF</button>
            </div>
            <table className="price-table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>24h %</th>
                    <th>Market Cap</th>
                    <th>Volume (24h)</th>
                    <th>Circulating Supply</th>
                </tr>
                </thead>
                <tbody>
                {data.map((item: any, index: number) => (
                    <tr key={index}>
                        <td>{(page - 1) * 10 + index + 1}</td> {/* Az indexet az oldalszámhoz igazítjuk */}
                        <td>
                            <img src={item.image} alt={item.name} style={{ width: '30px', height: '30px', marginRight: '10px', verticalAlign: 'middle' }} />
                            {item.name} ({item.symbol.toUpperCase()})
                        </td>
                        <td>${item.current_price.toFixed(2)}</td>
                        <td style={{ color: item.price_change_percentage_24h >= 0 ? 'green' : 'red' }}>
                            {item.price_change_percentage_24h.toFixed(2)}%
                        </td>
                        <td>${item.market_cap.toLocaleString()}</td>
                        <td>${item.total_volume.toLocaleString()}</td>
                        <td>{item.circulating_supply.toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={prevPage} disabled={page === 1}>Previous</button>
                <span>Page {page}</span>
                <button onClick={nextPage}>Next</button>
            </div>
        </div>
    );
};

export default PriceData;
