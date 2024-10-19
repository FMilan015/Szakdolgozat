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
public class NewsController {

    @GetMapping("/api/news")
    public ResponseEntity<?> getNews(@RequestParam int page) {
        String apiKey = "YOUR_API_KEY";
        String url = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&apikey=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(result);
            JsonNode newsItems = rootNode.get("feed");

            List<Map<String, Object>> newsList = new ArrayList<>();
            for (JsonNode item : newsItems) {
                Map<String, Object> newsData = new HashMap<>();
                newsData.put("title", item.get("title").asText());
                newsData.put("url", item.get("url").asText());
                newsData.put("time_published", item.get("time_published").asText());
                newsData.put("summary", item.get("summary").asText());
                newsData.put("source", item.get("source").asText());

                newsList.add(newsData);
            }

            int pageSize = 4;
            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, newsList.size());

            List<Map<String, Object>> paginatedNews = newsList.subList(start, end);
            return ResponseEntity.ok(paginatedNews);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching news");
        }
    }
}
