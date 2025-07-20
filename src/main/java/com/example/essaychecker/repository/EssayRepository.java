package com.example.essaychecker.repository;

import com.example.essaychecker.entity.Essay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作文数据访问仓库接口
 * 提供对Essay实体的数据库操作
 */
@Repository
public interface EssayRepository extends JpaRepository<Essay, Long> {
    // 根据用户ID查询该用户的所有作文，按创建时间降序排列
    List<Essay> findByUserIdOrderByCreatedAtDesc(Long userId);
    // 根据用户ID查询该用户的所有作文，按创建时间降序排列
    List<Essay> findByUserIdAndTitleContainingOrderByCreatedAtDesc(Long userId, String title);
}
