package com.example.travelplanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Kimi API客户端
 * 使用单例模式确保全局只有一个实例
 */
@Component
public class KimiApiClient {

    @Value("${kimi.api.url}")
    private String apiUrl;

    @Value("${kimi.api.key}")
    private String apiKey;

    @Value("${kimi.api.model}")
    private String model;

    private final RestTemplate restTemplate;

    // 简单的缓存机制，减少重复调用
    private final Map<String, String> responseCache = new HashMap<>();

    public KimiApiClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 发送请求到Kimi API
     * @param prompt 提示词
     * @return AI生成的回复
     */
    public String sendRequest(String prompt) {
        // 检查缓存中是否有相同prompt的结果
        if (responseCache.containsKey(prompt)) {
            return responseCache.get(prompt);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 实际项目中应该处理更复杂的响应结构和错误情况
            Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);
            if (response != null && response.containsKey("choices")) {
                // 假设响应结构包含choices数组，第一个元素有text字段
                @SuppressWarnings("unchecked")
                Map<String, Object> choice = ((java.util.List<Map<String, Object>>) response.get("choices")).get(0);
                String result = (String) choice.get("text");
                
                // 缓存结果
                responseCache.put(prompt, result);
                
                return result;
            }
            return "无法解析API响应";
        } catch (Exception e) {
            return "API调用失败: " + e.getMessage();
        }
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        responseCache.clear();
    }
}