package com.example.essaychecker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * AI作文批改服务
 * 负责与DeepSeek AI API交互，对作文进行分析和批改
 */
@Service
public class AIService {

    // 从配置文件中注入DeepSeek API密钥
    @Value("${ai.deepseek.api-key}")
    private String deepseekApiKey;

    // 从配置文件中注入DeepSeek API URL
    @Value("${ai.deepseek.api-url}")
    private String deepseekApiUrl;

    // 创建一个ObjectMapper对象，解析JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初始化验证方法
     * 确保API密钥和URL已正确配置
     */
    @PostConstruct
    public void validateConfig() {
        if (deepseekApiKey == null || deepseekApiKey.isEmpty()) {
            throw new IllegalStateException("DeepSeek API key 没有配置");
        }
        if (deepseekApiUrl == null || deepseekApiUrl.isEmpty()) {
            throw new IllegalStateException("DeepSeek API URL 没有配置");
        }
        System.out.println("AI服务配置验证通过");
    }

    /**
     * 作文分析入口方法
     * @param content 待分析的作文内容
     * @return JSON格式的分析结果
     */
    public String analyzeEssay(String content) {
        try {
            // 调用DeepSeek API
            String response = callDeepSeekAPI(content);
            if (isValidJson(response)) {
                return response;
            } else {
                return String.format("{\"error\":\"INVALID_RESPONSE\",\"message\":\"%s\"}",
                        response.replace("\"", "\\\""));
            }
        } catch (Exception e) {
            return String.format("{\"error\":\"%s\",\"message\":\"%s\"}",
                    e.getClass().getSimpleName(),
                    e.getMessage().replace("\"", "\\\""));
        }
    }

    // 检查响应是否为有效的JSON
    private boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 调用DeepSeek API
    private String callDeepSeekAPI(String content) throws Exception {
        // 创建HTTP客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HTTP POST请求
        HttpPost httpPost = new HttpPost(deepseekApiUrl);

        try {
            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + deepseekApiKey);

            // 设置请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek-chat");

            // 设置消息
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", buildPrompt(content));

            requestBody.put("messages", Arrays.asList(message));
            requestBody.put("max_tokens", 2000);
            requestBody.put("temperature", 0.7);

            // 将请求体转换为JSON字符串
            String jsonRequest = objectMapper.writeValueAsString(requestBody);
            httpPost.setEntity(new StringEntity(jsonRequest, "UTF-8"));

            // 执行HTTP POST请求，处理响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String responseBody = EntityUtils.toString(entity);
                    JsonNode responseJson = objectMapper.readTree(responseBody);
                    JsonNode choices = responseJson.get("choices");

                    if (choices != null && choices.isArray() && choices.size() > 0) {
                        JsonNode messageResponse = choices.get(0).get("message");
                        if (messageResponse != null) {
                            return messageResponse.get("content").asText();
                        }
                    }
                }
            }
        } finally {
            httpClient.close();
        }

        return "{\"error\":\"AI_SERVICE_UNAVAILABLE\",\"message\":\"AI分析服务暂不可用\"}";
    }

    // 构建提示
    private String buildPrompt(String content) {
        return
                "请作为一名中文写作老师，批改下面这篇中文作文。\n" +
                        "请严格按照以下格式输出标准 JSON，不得包含任何多余解释或文本。\n\n" +
                        "### 要求：\n" +
                        "1. 语法错误分析（grammarErrors）\n" +
                        "2. 流畅度分析（fluencyAnalysis）\n" +
                        "3. 逻辑结构评估（logicEvaluation）\n" +
                        "4. 写作建议（writingSuggestions）\n" +
                        "5. 词汇推荐（vocabularyRecommendations）\n" +
                        "6. 综合评分（overallScore，0~100）\n\n" +
                        "### 返回格式：\n" +
                        "{\n" +
                        "  \"grammarErrors\": [\"错误描述1\", \"错误描述2\"],\n" +
                        "  \"fluencyAnalysis\": \"对句子、段落流畅性的评价\",\n" +
                        "  \"logicEvaluation\": \"文章结构与论证逻辑评价\",\n" +
                        "  \"writingSuggestions\": [\"建议1\", \"建议2\"],\n" +
                        "  \"vocabularyRecommendations\": [\"词汇建议1\", \"词汇建议2\"],\n" +
                        "  \"overallScore\": 80\n" +
                        "}\n\n" +
                        "### 作文内容：\n" + content;
    }
}
