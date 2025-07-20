package com.example.essaychecker.service;

import com.example.essaychecker.dto.CheckResultDto;
import com.example.essaychecker.entity.CheckResult;
import com.example.essaychecker.entity.Essay;
import com.example.essaychecker.repository.CheckResultRepository;
import com.example.essaychecker.repository.EssayRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 作文核心服务类
 * 提供作文的CRUD操作和批改功能
 */
@Service
public class EssayService {

    @Autowired
    private EssayRepository essayRepository;

    @Autowired
    private CheckResultRepository checkResultRepository;

    @Autowired
    private AIService aiService;

    // 解析AI返回的JSON数据
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 保存作文
    public Essay saveEssay(Long userId, String title, String content) {
        Essay essay = new Essay(userId, title, content);
        return essayRepository.save(essay);
    }

    // 获取用户所有作文
    public List<Essay> getUserEssays(Long userId) {
        return essayRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 获取作文详情
    public Essay getEssayById(Long id) {
        return essayRepository.findById(id).orElse(null);
    }

    // 批改作文
    public CheckResultDto checkEssay(Long essayId) {
        Optional<Essay> essayOpt = essayRepository.findById(essayId);
        if (!essayOpt.isPresent()) {
            throw new RuntimeException("作文不存在");
        }

        Essay essay = essayOpt.get();

        Optional<CheckResult> existingResult = checkResultRepository.findByEssayId(essayId);
        if (existingResult.isPresent()) {
            return convertToDto(existingResult.get(), essay);
        }

        String aiResponse = aiService.analyzeEssay(essay.getContent());
        CheckResult checkResult = parseAIResponse(aiResponse, essayId);

        checkResult = checkResultRepository.save(checkResult);
        return convertToDto(checkResult, essay);
    }

    // 解析AI返回的JSON数据
    private CheckResult parseAIResponse(String aiResponse, Long essayId) {
        CheckResult checkResult = new CheckResult();
        checkResult.setEssayId(essayId);

        // 错误处理
        try {
            if (aiResponse.contains("\"error\"")) {
                JsonNode errorNode = objectMapper.readTree(aiResponse);
                String errorMsg = errorNode.get("message").asText();
                throw new RuntimeException("AI服务错误: " + errorMsg);
            }

            JsonNode jsonNode = objectMapper.readTree(aiResponse);

            // 解析语法错误
            JsonNode grammarErrors = jsonNode.get("grammarErrors");
            if (grammarErrors != null && grammarErrors.isArray()) {
                List<String> errors = new ArrayList<>();
                for (JsonNode error : grammarErrors) {
                    errors.add(error.asText());
                }
                checkResult.setGrammarErrors(String.join("\u0001", errors));
            }

            // 解析流畅度分析
            if (jsonNode.has("fluencyAnalysis")) {
                checkResult.setFluencyAnalysis(jsonNode.get("fluencyAnalysis").asText());
            }

            // 解析逻辑评估
            if (jsonNode.has("logicEvaluation")) {
                checkResult.setLogicEvaluation(jsonNode.get("logicEvaluation").asText());
            }

            // 解析写作建议
            JsonNode writingSuggestions = jsonNode.get("writingSuggestions");
            if (writingSuggestions != null && writingSuggestions.isArray()) {
                List<String> suggestions = new ArrayList<>();
                for (JsonNode suggestion : writingSuggestions) {
                    suggestions.add(suggestion.asText());
                }
                checkResult.setWritingSuggestions(String.join(";", suggestions));
            }

            // 解析词汇推荐
            JsonNode vocabulary = jsonNode.get("vocabularyRecommendations");
            if (vocabulary != null && vocabulary.isArray()) {
                List<String> vocabList = new ArrayList<>();
                for (JsonNode vocab : vocabulary) {
                    vocabList.add(vocab.asText());
                }
                checkResult.setVocabularyRecommendations(String.join(";", vocabList));
            }

            // 解析总分
            if (jsonNode.has("overallScore")) {
                checkResult.setOverallScore(jsonNode.get("overallScore").asInt());
            }

        } catch (Exception e) {
            e.printStackTrace();
            checkResult.setGrammarErrors("AI解析失败");
            checkResult.setFluencyAnalysis("AI解析失败，请重新尝试");
            checkResult.setLogicEvaluation("AI解析失败，请重新尝试");
            checkResult.setWritingSuggestions("AI解析失败");
            checkResult.setVocabularyRecommendations("AI解析失败");
            checkResult.setOverallScore(0);
        }

        return checkResult;
    }

    /**
     * 将批改结果实体转换为DTO
     * @param checkResult 批改结果实体
     * @param essay 关联的作文
     * @return 批改结果DTO
     */
    private CheckResultDto convertToDto(CheckResult checkResult, Essay essay) {
        CheckResultDto dto = new CheckResultDto();
        // 将属性复制给DTO
        dto.setId(checkResult.getId());
        dto.setEssayId(checkResult.getEssayId());
        dto.setEssayTitle(essay.getTitle());
        dto.setFluencyAnalysis(checkResult.getFluencyAnalysis());
        dto.setLogicEvaluation(checkResult.getLogicEvaluation());
        dto.setOverallScore(checkResult.getOverallScore());
        dto.setCreatedAt(checkResult.getCreatedAt());

        // 处理分隔符分隔的字段
        if (checkResult.getGrammarErrors() != null) {
            dto.setGrammarErrors(List.of(checkResult.getGrammarErrors().split("\u0001")));
        }

        if (checkResult.getWritingSuggestions() != null) {
            dto.setWritingSuggestions(List.of(checkResult.getWritingSuggestions().split(";")));
        }

        if (checkResult.getVocabularyRecommendations() != null) {
            dto.setVocabularyRecommendations(List.of(checkResult.getVocabularyRecommendations().split(";")));
        }

        return dto;
    }

    /**
     * 更新作文
     * @param id 作文ID
     * @param title 新标题
     * @param content 新内容
     * @return 更新后的作文（未找到返回null）
     */
    public Essay updateEssay(Long id, String title, String content) {
        Optional<Essay> essayOpt = essayRepository.findById(id);
        if (essayOpt.isPresent()) {
            Essay essay = essayOpt.get();
            essay.setTitle(title);
            essay.setContent(content);
            return essayRepository.save(essay);
        }
        return null;
    }

    /**
     * 删除作文
     * @param id 作文ID
     * @return 是否删除成功
     */
    public boolean deleteEssay(Long id) {
        if (essayRepository.existsById(id)) {
            essayRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
