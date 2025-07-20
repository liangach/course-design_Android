package com.example.essaychecker.controller;

import com.example.essaychecker.dto.ApiResponse;
import com.example.essaychecker.entity.User;
import com.example.essaychecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器类，处理所有与用户相关的HTTP请求
 * 提供用户注册、登录、查询和更新功能
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param request 包含用户名、密码和邮箱的请求体
     * @return 包含注册结果和用户信息的统一响应
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String email = request.get("email");

            User user = userService.register(username, password, email);
            return ApiResponse.success("注册成功", user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 用户登录接口
     * @param request 包含用户名和密码的请求体
     * @return 包含登录结果、用户信息和简单token的统一响应
     */
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            User user = userService.login(username, password);

            Map<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("token", "token_" + user.getId()); // 简单的token生成

            return ApiResponse.success("登录成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 根据用户ID查询用户信息接口
     * @param id 用户ID
     * @return 包含查询结果和用户信息的统一响应
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ApiResponse.success(user);
        } else {
            return ApiResponse.error("用户不存在");
        }
    }

    /**
     * 根据用户ID更新用户信息接口
     * @param id 用户ID
     * @param user 包含用户信息的请求体
     * @return 包含更新结果和用户信息的统一响应
     */
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ApiResponse.success("更新成功", updatedUser);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
