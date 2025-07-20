package com.example.essaychecker.dto;

/**
 * 统一API响应封装类
 * 标准化所有Controller的返回格式
 * @param <T> 响应数据的泛型类型
 */
public class ApiResponse<T> {
    // 操作是否成功
    private boolean success;
    // 响应消息（成功或错误信息）
    private String message;
    // 响应数据
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data);
    }

    // 带消息的成功响应
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // 错误响应
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // Getter和Setter方法
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
