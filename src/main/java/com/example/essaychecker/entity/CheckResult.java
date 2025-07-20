package com.example.essaychecker.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * 作文批改结果实体类
 * 对应数据库中的 check_results 表，存储作文批改的详细结果
 */

@Entity
@Table(name = "check_results")
public class CheckResult {
    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增
    private Long id;

    // 关联作文表
    @Column(name = "essay_id")
    private Long essayId;

    // 语法错误分析
    @Column(columnDefinition = "TEXT")
    private String grammarErrors;

    // 流利度分析
    @Column(columnDefinition = "TEXT")
    private String fluencyAnalysis;

    // 逻辑评估
    @Column(columnDefinition = "TEXT")
    private String logicEvaluation;

    // 写作建议
    @Column(columnDefinition = "TEXT")
    private String writingSuggestions;

    // 词汇推荐
    @Column(columnDefinition = "TEXT")
    private String vocabularyRecommendations;

    // 总分
    private Integer overallScore;

    // 创建时间
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 创建时间自动填充
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 构造函数
    public CheckResult() {}

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEssayId() { return essayId; }
    public void setEssayId(Long essayId) { this.essayId = essayId; }

    public String getGrammarErrors() { return grammarErrors; }
    public void setGrammarErrors(String grammarErrors) { this.grammarErrors = grammarErrors; }

    public String getFluencyAnalysis() { return fluencyAnalysis; }
    public void setFluencyAnalysis(String fluencyAnalysis) { this.fluencyAnalysis = fluencyAnalysis; }

    public String getLogicEvaluation() { return logicEvaluation; }
    public void setLogicEvaluation(String logicEvaluation) { this.logicEvaluation = logicEvaluation; }

    public String getWritingSuggestions() { return writingSuggestions; }
    public void setWritingSuggestions(String writingSuggestions) { this.writingSuggestions = writingSuggestions; }

    public String getVocabularyRecommendations() { return vocabularyRecommendations; }
    public void setVocabularyRecommendations(String vocabularyRecommendations) { this.vocabularyRecommendations = vocabularyRecommendations; }

    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
