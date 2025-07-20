package com.example.essaychecker.repository;

import com.example.essaychecker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问仓库接口
 * 提供对User实体的数据库操作
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 通过用户名查询用户
    Optional<User> findByUsername(String username);
    // 检查用户名是否存在
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
