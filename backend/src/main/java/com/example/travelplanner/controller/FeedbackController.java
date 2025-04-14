package com.example.travelplanner.controller;

import com.example.travelplanner.model.entity.Feedback;
import com.example.travelplanner.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 反馈控制器
 * 提供反馈提交和查询接口
 */
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 提交反馈
     * @param feedback 反馈信息
     * @return 创建的反馈
     */
    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody Feedback feedback) {
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                // 如果用户已登录，设置用户ID
                feedback.setUserId(Long.parseLong(authentication.getName()));
            }
            
            // 创建反馈
            Feedback createdFeedback = feedbackService.createFeedback(feedback);
            return ResponseEntity.ok(createdFeedback);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取反馈详情
     * @param id 反馈ID
     * @return 反馈详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedbackOpt = feedbackService.findById(id);
        if (feedbackOpt.isPresent()) {
            return ResponseEntity.ok(feedbackOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取当前用户的反馈
     * @return 反馈列表
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyFeedbacks() {
        // 获取当前登录用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // 查询用户的反馈
            Long userId = Long.parseLong(authentication.getName());
            List<Feedback> feedbacks = feedbackService.findByUserId(userId);
            return ResponseEntity.ok(feedbacks);
        } else {
            return ResponseEntity.badRequest().body("用户未登录");
        }
    }

    /**
     * 获取所有反馈（管理员接口）
     * @return 反馈列表
     */
    @GetMapping
    public ResponseEntity<?> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.findAll();
        return ResponseEntity.ok(feedbacks);
    }

    /**
     * 根据状态查询反馈（管理员接口）
     * @param status 状态
     * @return 反馈列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getFeedbacksByStatus(@PathVariable String status) {
        List<Feedback> feedbacks = feedbackService.findByStatus(status);
        return ResponseEntity.ok(feedbacks);
    }

    /**
     * 更新反馈状态（管理员接口）
     * @param id 反馈ID
     * @param statusMap 状态信息
     * @return 更新后的反馈
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateFeedbackStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        try {
            String status = statusMap.get("status");
            Feedback updatedFeedback = feedbackService.updateStatus(id, status);
            return ResponseEntity.ok(updatedFeedback);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除反馈
     * @param id 反馈ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        try {
            feedbackService.deleteFeedback(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "反馈删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}