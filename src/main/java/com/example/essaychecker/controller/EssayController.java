package com.example.essaychecker.controller;

import com.example.essaychecker.dto.ApiResponse;
import com.example.essaychecker.dto.CheckResultDto;
import com.example.essaychecker.dto.EssaySubmitDto;
import com.example.essaychecker.entity.Essay;
import com.example.essaychecker.service.EssayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 作文控制器类，处理所有与作文相关的HTTP请求
 * 提供作文的提交、查询、修改、删除和批改功能
 */

@RestController  // 返回的数据自动转为JSON格式
@RequestMapping("/essay")
@CrossOrigin(origins = "*")  // 允许所有来源的跨域请求
public class EssayController {

    @Autowired   // 自动注入EssayService，处理用户相关的业务逻辑
    private EssayService essayService;

    /**
     * 提交作文
     * @param essaySubmitDto 包含用户ID、作文标题和内容的DTO对象
     * @return 包含操作结果和作文数据的统一响应
     */
    @PostMapping("/submit")
    public ApiResponse<Essay> submitEssay(@RequestBody EssaySubmitDto essaySubmitDto) {
        try {
            // 调用服务层保存作文
            Essay essay = essayService.saveEssay(
                    essaySubmitDto.getUserId(),
                    essaySubmitDto.getTitle(),
                    essaySubmitDto.getContent()
            );
            return ApiResponse.success("作文提交成功", essay);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取指定用户的所有作文
     * @param userId 用户ID
     * @return 包含用户作文列表的统一响应
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Essay>> getUserEssays(@PathVariable Long userId) {
        try {
            List<Essay> essays = essayService.getUserEssays(userId);
            return ApiResponse.success(essays);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }


    /**
     * 根据ID获取单篇作文
     * @param id 作文ID
     * @return 包含作文数据的统一响应
     */
    @GetMapping("/{id}")
    public ApiResponse<Essay> getEssayById(@PathVariable Long id) {
        try {
            Essay essay = essayService.getEssayById(id);
            if (essay != null) {
                return ApiResponse.success(essay);
            } else {
                return ApiResponse.error("作文不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 批改作文
     * @param essayId 要批改的作文ID
     * @return 包含批改结果的统一响应
     */
    @PostMapping("/check/{essayId}")
    public ApiResponse<CheckResultDto> checkEssay(@PathVariable Long essayId) {
        try {
            CheckResultDto result = essayService.checkEssay(essayId);
            return ApiResponse.success("批改完成", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新作文
     * @param id 要更新的作文ID
     * @param request 包含作文标题和内容的JSON对象
     * @return 包含更新后的作文数据的统一响应
     */
    @PutMapping("/{id}")
    public ApiResponse<Essay> updateEssay(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String content = request.get("content");

            Essay essay = essayService.updateEssay(id, title, content);
            if (essay != null) {
                return ApiResponse.success("更新成功", essay);
            } else {
                return ApiResponse.error("作文不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除作文
     * @param id 要删除的作文ID
     * @return 删除结果统一响应
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEssay(@PathVariable Long id) {
        try {
            boolean deleted = essayService.deleteEssay(id);
            if (deleted) {
                return ApiResponse.success("删除成功", null);
            } else {
                return ApiResponse.error("作文不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
}
