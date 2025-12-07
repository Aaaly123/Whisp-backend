package com.example.demo.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SentimentAnalysisService {

    @Value("${huggingface.api.token}")
    private String huggingFaceToken;

    // Use the three-label model
    private static final String MODEL_URL =
            "https://router.huggingface.co/models/cardiffnlp/twitter-roberta-base-sentiment-latest";

    public SentimentResult analyzeSentiment(String text) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + huggingFaceToken);

            JSONObject body = new JSONObject();
            body.put("inputs", text);

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
            ResponseEntity<String> response =
                    restTemplate.postForEntity(MODEL_URL, entity, String.class);

            String respBody = response.getBody();
            if (respBody == null || respBody.isEmpty()) {
                System.out.println("Empty response from Hugging Face");
                return new SentimentResult("neutral", 0.0f);
            }

            System.out.println("🔍 Raw HF response: " + respBody);

            // Example structure: [[{"label":"negative","score":0.02},{"label":"neutral","score":0.15},{"label":"positive","score":0.83}]]
            JSONArray rootArr = new JSONArray(respBody);
            JSONArray scoresArr = rootArr.getJSONArray(0);

            String bestLabel = null;
            double bestScore = -1.0;

            for (int i = 0; i < scoresArr.length(); i++) {
                JSONObject obj = scoresArr.getJSONObject(i);
                String label = obj.getString("label").toLowerCase();
                double score = obj.getDouble("score");
                if (score > bestScore) {
                    bestScore = score;
                    bestLabel = label;
                }
            }

            if (bestLabel == null) {
                bestLabel = "neutral";
                bestScore = 0.0;
            }

            return new SentimentResult(bestLabel, (float) bestScore);

        } catch (Exception e) {
            System.out.println("Error analyzing sentiment: " + e.getMessage());
            return new SentimentResult("neutral", 0.0f);
        }
    }

    public static class SentimentResult {
        private final String label;
        private final float score;

        public SentimentResult(String label, float score) {
            this.label = label;
            this.score = score;
        }

        public String getLabel() { return label; }
        public float getScore() { return score; }
    }
}

