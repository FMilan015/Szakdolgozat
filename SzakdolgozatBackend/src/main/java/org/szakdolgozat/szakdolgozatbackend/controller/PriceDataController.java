package org.szakdolgozat.szakdolgozatbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@RestController
public class PriceDataController {

    @GetMapping("/api/crypto")
    public ResponseEntity<?> getCryptoData(@RequestParam int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> cryptoDataList = new ArrayList<>();
            JsonNode rootNode = mapper.readTree(result);

            for (JsonNode node : rootNode) {
                Map<String, Object> cryptoData = new HashMap<>();
                cryptoData.put("symbol", node.get("symbol").asText());
                cryptoData.put("current_price", node.get("current_price").asDouble());
                cryptoData.put("price_change_percentage_24h", node.get("price_change_percentage_24h").asDouble());
                cryptoData.put("market_cap", node.get("market_cap").asLong());
                cryptoData.put("total_volume", node.get("total_volume").asLong());
                cryptoData.put("circulating_supply", node.get("circulating_supply").asLong());
                cryptoData.put("image", node.get("image").asText());

                cryptoDataList.add(cryptoData);
            }
            return ResponseEntity.ok(cryptoDataList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching crypto data on backend");
        }
    }

    @GetMapping("/api/stocks")
    public ResponseEntity<?> getStockData(@RequestParam int page) {
        String[] top100Stocks = {
                "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "FB", "NVDA", "BRK.B", "JPM", "JNJ",
                "V", "PG", "UNH", "DIS", "HD", "MA", "PYPL", "NFLX", "VZ", "PFE",
                "NKE", "XOM", "KO", "MRK", "WMT", "PEP", "ADBE", "CRM", "CSCO", "INTC",
                "T", "CMCSA", "ABBV", "MDT", "CVX", "HON", "BA", "ABT", "MCD", "IBM",
                "QCOM", "LLY", "AMGN", "TMO", "DHR", "MMM", "GE", "ORCL", "C", "LOW",
                "BMY", "WFC", "SBUX", "UPS", "CAT", "AXP", "GS", "MS", "RTX", "USB",
                "BKNG", "ISRG", "MDLZ", "AMT", "SPGI", "ADP", "PLD", "BLK", "CB", "CL",
                "DUK", "NEE", "TJX", "TGT", "ECL", "SYK", "ZTS", "CI", "DE", "LIN",
                "BDX", "ADI", "COST", "MMM", "FIS", "ITW", "STZ", "GILD", "SCHW", "F",
                "EL", "PSA", "APD", "MO", "NSC", "ETN", "FDX", "AON", "D", "SO",
                "AEP", "VRTX", "WM", "MAR", "ADI"
        };
        List<Map<String, Object>> stockDataList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        try {
            int startIndex = (page - 1) * 10;
            int endIndex = Math.min(startIndex + 10, top100Stocks.length);

            for (int i = startIndex; i < endIndex; i++) {
                String symbol = top100Stocks[i];
                String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + symbol + "&apikey=YOUR_API_KEY";
                String result = restTemplate.getForObject(url, String.class);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(result);
                JsonNode timeSeries = rootNode.get("Time Series (Daily)");

                if (timeSeries == null) {
                    System.out.println("No time series data available for " + symbol);
                    continue;
                }

                Iterator<Map.Entry<String, JsonNode>> fields = timeSeries.fields();
                Map.Entry<String, JsonNode> latestData = fields.next();

                Map<String, Object> stockData = new HashMap<>();
                stockData.put("symbol", symbol);
                stockData.put("close", latestData.getValue().get("4. close").asDouble());

                if (fields.hasNext()) {
                    Map.Entry<String, JsonNode> previousData = fields.next();
                    double todayClose = latestData.getValue().get("4. close").asDouble();
                    double yesterdayClose = previousData.getValue().get("4. close").asDouble();
                    double priceChangePercentage = ((todayClose - yesterdayClose) / yesterdayClose) * 100;
                    stockData.put("price_change_percentage_1d", priceChangePercentage);
                } else {
                    stockData.put("price_change_percentage_1d", 0.0);
                }

                stockData.put("total_volume", latestData.getValue().get("5. volume").asLong());

                stockDataList.add(stockData);
            }
            return ResponseEntity.ok(stockDataList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching stock data on backend");
        }
    }


}
