package org.szakdolgozat.szakdolgozatbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class PriceDataController {

    //Crypto adatok lekérése CoinGeckoról (nagyon minimális a request limit)
    @GetMapping("/crypto")
    public ResponseEntity<?> getCryptoData(@RequestParam int page) {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(result);
    }


    // Stock adatok lekérése az Alpha Vantage API-ból
    @GetMapping("/stocks")
    public ResponseEntity<?> getStockData() {
        String apiKey = "YOUR_ALPHA_VANTAGE_API_KEY";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBM&apikey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(result);
    }

    // ETF adatok lekérése az Alpha Vantage API-ból
    @GetMapping("/etf")
    public ResponseEntity<?> getEtfData() {
        String apiKey = "YOUR_ALPHA_VANTAGE_API_KEY";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=VOO&apikey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(result);
    }
}

