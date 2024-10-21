import React, { useState } from 'react';
import './pages-style/Pages.css';

const TradingTools: React.FC = () => {
    const [selectedTool, setSelectedTool] = useState<string | null>(null);
    const [inputs, setInputs] = useState<any>({});
    const [result, setResult] = useState<number | string | null>(null);
    const [profit, setProfit] = useState<number | string | null>(null);
    const [totalContributions, setTotalContributions] = useState<number | string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const handleToolSelection = (tool: string) => {
        setSelectedTool(tool);
        setInputs({});
        setResult(null);
        setProfit(null);
        setTotalContributions(null);
        setError(null);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInputs({
            ...inputs,
            [e.target.name]: parseFloat(e.target.value)
        });
    };

    const validateInputs = () => {
        if (selectedTool === 'Profit & Loss') {
            return inputs.entryPrice && inputs.exitPrice && inputs.positionSize;
        } else if (selectedTool === 'Risk/Reward ratio') {
            return inputs.entryPrice && inputs.stopLoss && inputs.profitTarget;
        } else if (selectedTool === 'Return on investment (ROI)') {
            return inputs.initialInvestment && inputs.finalValue;
        } else if (selectedTool === 'Compound interest') {
            return inputs.monthlyContribution && inputs.rate && inputs.time;
        }
        return false;
    };

    const calculateResult = () => {
        if (!validateInputs()) {
            setError('Please fill in all fields');
            setResult(null); // Clear the result if validation fails
            return;
        }

        setError(null);

        if (selectedTool === 'Profit & Loss') {
            const profitLoss = ((inputs.exitPrice - inputs.entryPrice) * inputs.positionSize).toFixed(2);
            setResult(profitLoss);
        } else if (selectedTool === 'Return on investment (ROI)') {
            const roi = (((inputs.finalValue - inputs.initialInvestment) / inputs.initialInvestment) * 100).toFixed(2);
            setResult(roi);
        } else if (selectedTool === 'Risk/Reward ratio') {
            const riskReward = Math.abs(((inputs.profitTarget - inputs.entryPrice) / (inputs.entryPrice - inputs.stopLoss))).toFixed(2);
            setResult(riskReward);
        } else if (selectedTool === 'Compound interest') {
            const monthlyContribution = inputs.monthlyContribution || 0;
            const annualRate = inputs.rate / 100;
            const years = inputs.time;
            const compoundsPerYear = 12;

            const futureValueContributions = monthlyContribution * ((Math.pow((1 + annualRate / compoundsPerYear), compoundsPerYear * years) - 1) / (annualRate / compoundsPerYear));
            const totalFutureValue = futureValueContributions;
            const totalInvested = monthlyContribution * (years * 12);
            const investmentProfit = totalFutureValue - totalInvested;

            setResult(totalFutureValue.toFixed(2));
            setTotalContributions(totalInvested.toFixed(2));
            setProfit(investmentProfit.toFixed(2));
        }
    };

    return (
        <div className="page">
            <div className="tool-buttons">
                <button onClick={() => handleToolSelection('Profit & Loss')}>Profit & Loss</button>
                <button onClick={() => handleToolSelection('Risk/Reward ratio')}>Risk/Reward ratio</button>
                <button onClick={() => handleToolSelection('Return on investment (ROI)')}>Return on investment (ROI)</button>
                <button onClick={() => handleToolSelection('Compound interest')}>Compound interest</button>
            </div>

            <p className="tool-description">Select a tool and enter the required data</p>

            {selectedTool && (
                <div className="tool-inputs">
                    <h2>{selectedTool}</h2>

                    {error && <p style={{ color: 'white' }}>{error}</p>}

                    {selectedTool === 'Profit & Loss' && (
                        <div>
                            <input type="number" name="entryPrice" placeholder="Entry price" onChange={handleInputChange} />
                            <input type="number" name="exitPrice" placeholder="Exit price" onChange={handleInputChange} />
                            <input type="number" name="positionSize" placeholder="Position size" onChange={handleInputChange} />
                        </div>
                    )}

                    {selectedTool === 'Risk/Reward ratio' && (
                        <div>
                            <input type="number" name="entryPrice" placeholder="Entry price" onChange={handleInputChange} />
                            <input type="number" name="stopLoss" placeholder="Stop loss" onChange={handleInputChange} />
                            <input type="number" name="profitTarget" placeholder="Profit target" onChange={handleInputChange} />
                        </div>
                    )}

                    {selectedTool === 'Return on investment (ROI)' && (
                        <div>
                            <input type="number" name="initialInvestment" placeholder="Initial investment" onChange={handleInputChange} />
                            <input type="number" name="finalValue" placeholder="Final value of your investment" onChange={handleInputChange} />
                        </div>
                    )}

                    {selectedTool === 'Compound interest' && (
                        <div>
                            <input type="number" name="monthlyContribution" placeholder="Monthly contribution" onChange={handleInputChange} />
                            <input type="number" name="rate" placeholder="Annual interest rate (%)" onChange={handleInputChange} />
                            <input type="number" name="time" placeholder="Years" onChange={handleInputChange} />
                        </div>
                    )}

                    <button className="calculate-btn" onClick={calculateResult}>Calculate</button>

                    {result !== null && selectedTool === 'Compound interest' && (
                        <div className="result-box">
                            <table className="centered-table">
                                <thead>
                                <tr>
                                    <th>Description</th>
                                    <th>Value</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Total future value (with investment):</td>
                                    <td>{Number(result).toLocaleString()} Ft</td>
                                </tr>
                                <tr>
                                    <td>Total invested amount:</td>
                                    <td>{Number(totalContributions).toLocaleString()} Ft</td>
                                </tr>
                                <tr>
                                    <td>Profit earned from investment:</td>
                                    <td>{Number(profit).toLocaleString()} Ft</td>
                                </tr>
                                </tbody>
                            </table>
                            <p>This is the total value you will accumulate, based on your monthly contributions and the interest rate.</p>
                        </div>
                    )}

                    {result !== null && selectedTool === 'Profit & Loss' && (
                        <div className="result-box">
                            <h3>Result: {Number(result).toLocaleString()} Ft</h3>
                            <p>This is your profit or loss based on the entry and exit prices and the position size.</p>
                        </div>
                    )}

                    {result !== null && selectedTool === 'Return on investment (ROI)' && (
                        <div className="result-box">
                            <h3>Result: {Number(result).toLocaleString()}%</h3>
                            <p>This is the return on investment (ROI) based on your initial investment and final value.</p>
                        </div>
                    )}

                    {result !== null && selectedTool === 'Risk/Reward ratio' && (
                        <div className="result-box">
                            <h3>Result: {Number(result).toLocaleString()} RR</h3>
                            <p>This is the risk/reward ratio based on your entry price, stop loss, and profit target.</p>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default TradingTools;
