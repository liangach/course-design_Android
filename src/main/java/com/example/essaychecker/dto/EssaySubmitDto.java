package com.example.essaychecker.dto;

/**
 * 作文提交数据传输对象(DTO)
 * 接收前端提交的作文数据
 */
public class EssaySubmitDto {
    // // 提交用户的ID
    private Long userId;
    // 作文的标题
    private String title;
    // 作文的内容
    private String content;

    // 构造函数
    public EssaySubmitDto() {}

    public EssaySubmitDto(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

    // Getter和Setter方法
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
