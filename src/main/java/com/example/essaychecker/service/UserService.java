package com.example.essaychecker.service;

import com.example.essaychecker.entity.User;
import com.example.essaychecker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务类
 * 处理用户相关的业务逻辑，包括注册、登录和用户信息管理
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 注册
    public User register(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User(username, password, email);
        return userRepository.save(user);
    }

    // 登录
    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new RuntimeException("用户名或密码错误");
    }

    // 获取用户信息
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 更新用户信息
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
