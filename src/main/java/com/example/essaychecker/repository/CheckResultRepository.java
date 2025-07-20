package com.example.essaychecker.repository;

import com.example.essaychecker.entity.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 批改结果数据访问接口
 * 提供对CheckResult实体的数据库操作
 */
@Repository
public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    // 根据作文ID查询批改结果
    Optional<CheckResult> findByEssayId(Long essayId);
    // 根据多个作文ID批量查询批改结果
    List<CheckResult> findByEssayIdIn(List<Long> essayIds);
}
