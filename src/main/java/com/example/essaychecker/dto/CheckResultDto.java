package com.example.essaychecker.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 作文批改结果数据传输对象(DTO)
 * 封装作文批改后的各项分析结果和建议
 */
public class CheckResultDto {
    // 批改结果ID
    private Long id;
    // 批改的作文ID
    private Long essayId;
    // 批改作文的标题
    private String essayTitle;
    // 语法错误
    private List<String> grammarErrors;
    // 流利度分析
    private String fluencyAnalysis;
    // 逻辑评估
    private String logicEvaluation;
    // 写作建议
    private List<String> writingSuggestions;
    // 词汇推荐
    private List<String> vocabularyRecommendations;
    // 总分
    private Integer overallScore;
    // 创建时间
    private LocalDateTime createdAt;

    public CheckResultDto() {}

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEssayId() { return essayId; }
    public void setEssayId(Long essayId) { this.essayId = essayId; }

    public String getEssayTitle() { return essayTitle; }
    public void setEssayTitle(String essayTitle) { this.essayTitle = essayTitle; }

    public List<String> getGrammarErrors() { return grammarErrors; }
    public void setGrammarErrors(List<String> grammarErrors) { this.grammarErrors = grammarErrors; }

    public String getFluencyAnalysis() { return fluencyAnalysis; }
    public void setFluencyAnalysis(String fluencyAnalysis) { this.fluencyAnalysis = fluencyAnalysis; }

    public String getLogicEvaluation() { return logicEvaluation; }
    public void setLogicEvaluation(String logicEvaluation) { this.logicEvaluation = logicEvaluation; }

    public List<String> getWritingSuggestions() { return writingSuggestions; }
    public void setWritingSuggestions(List<String> writingSuggestions) { this.writingSuggestions = writingSuggestions; }

    public List<String> getVocabularyRecommendations() { return vocabularyRecommendations; }
    public void setVocabularyRecommendations(List<String> vocabularyRecommendations) { this.vocabularyRecommendations = vocabularyRecommendations; }

    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
