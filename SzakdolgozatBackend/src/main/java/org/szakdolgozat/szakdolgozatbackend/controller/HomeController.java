package org.szakdolgozat.szakdolgozatbackend.controller;

import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HomeController {
    private static final String apiKey = "API_KEY";
    private static final String url = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&apikey=" + apiKey;

    @GetMapping("/api/news/trending")
    public List<Map<String, String>> getHomeNews() throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray articles = jsonResponse.getJSONArray("feed");

        List<Map<String, String>> homeNews = new ArrayList<>();
        for (int i = 0; i < Math.min(2, articles.length()); i++) {
            JSONObject article = articles.getJSONObject(i);
            Map<String, String> newsItem = new HashMap<>();
            newsItem.put("title", article.getString("title"));
            newsItem.put("summary", article.getString("summary"));
            newsItem.put("url", article.getString("url"));
            newsItem.put("source", article.getString("source"));
            homeNews.add(newsItem);
        }

        return homeNews;
    }

    @GetMapping("/api/data/trending")
    public List<Map<String, Object>> getTrendingData() throws JSONException {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=4&page=1";
        RestTemplate restTemplate = new RestTemplate();
        JSONArray response = new JSONArray(restTemplate.getForObject(url, String.class));

        List<Map<String, Object>> trendingData = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject coin = response.getJSONObject(i);
            Map<String, Object> dataItem = new HashMap<>();
            dataItem.put("symbol", coin.getString("symbol").toUpperCase());
            dataItem.put("price", coin.getDouble("current_price"));
            dataItem.put("change", coin.getDouble("price_change_percentage_24h"));
            trendingData.add(dataItem);
        }

        return trendingData;
    }
}
