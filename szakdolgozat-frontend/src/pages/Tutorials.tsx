import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './pages-style/Pages.css';

interface Tutorial {
    fileName: string;
    youtubeUrl: string;
}

const Tutorials: React.FC = () => {
    const [tutorials, setTutorials] = useState<Tutorial[]>([]);
    const [error, setError] = useState<string | null>(null);

    const youtubeLinks: { [key: string]: string } = {
        'tutorial1.jpg': 'https://www.youtube.com/watch?v=Dggu6VBQQyM',
        'tutorial2.jpg': 'https://www.youtube.com/watch?v=mRuohN6NzBA',
        'tutorial3.jpg': 'https://www.youtube.com/watch?v=duxYgnPPJsI',
        'tutorial4.jpg': 'https://www.youtube.com/watch?v=W9uVuHggqbs',
        'tutorial5.jpg': 'https://www.youtube.com/watch?v=Bk42ns1rwaI',
        'tutorial6.jpg': 'https://www.youtube.com/watch?v=X9bz--vwhvo',
        'tutorial7.jpg': 'https://www.youtube.com/watch?v=4yObyRbFjBw',
        'tutorial8.jpg': 'https://www.youtube.com/watch?v=BIxs5q18Frw',
        'tutorial9.jpg': 'https://www.youtube.com/watch?v=rWrqz7YtV4M',
        'tutorial10.jpg': 'https://www.youtube.com/watch?v=KhnxSbgjyeo',
    };

    const titles = [
        "Top Down Analysis",
        "Smart Money Concepts (SMC)",
        "TradingView Tutorial",
        "Technical Analysis",
        "Market Structure Trading",
        "Liquidity Grab Trading",
        "Moving Averages",
        "Momentum Trading",
        "ATR Indicator",
        "MACD Indicator"
    ];

    useEffect(() => {
        axios.get<string[]>('http://localhost:8080/api/tutorials')
            .then((response) => {
                const imagesWithLinks = response.data
                    .filter((fileName) => youtubeLinks[fileName])
                    .map((fileName) => ({
                        fileName,
                        youtubeUrl: youtubeLinks[fileName],
                    }));
                setTutorials(imagesWithLinks);
            })
            .catch((error) => {
                console.error("Error fetching images:", error);
                setError("Loading...");
            });
    }, []);

    if (error) {
        return <div className="page">{error}</div>;
    }

    return (
        <div className="page">
            <div className="tutorials-gallery">
                {tutorials.length === 0 ? (
                    <p>No images available</p>
                ) : (
                    tutorials.map((tutorial, index) => (
                        <div key={tutorial.fileName} className="tutorial-item">
                            <a href={tutorial.youtubeUrl} target="_blank" rel="noopener noreferrer">
                                <img
                                    src={`http://localhost:8080/api/tutorials/${tutorial.fileName}`}
                                    alt={`Tutorial ${tutorial.fileName}`}
                                    className="tutorial-image"
                                />
                            </a>
                            <p className="tutorial-title">{titles[index]}</p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default Tutorials;
