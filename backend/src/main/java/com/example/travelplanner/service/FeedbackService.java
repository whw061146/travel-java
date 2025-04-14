package com.example.travelplanner.service;

import com.example.travelplanner.model.entity.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * 反馈服务接口
 */
public interface FeedbackService {
    
    /**
     * 创建反馈
     * @param feedback 反馈对象
     * @return 创建的反馈
     */
    Feedback createFeedback(Feedback feedback);
    
    /**
     * 根据ID查询反馈
     * @param id 反馈ID
     * @return 反馈对象
     */
    Optional<Feedback> findById(Long id);
    
    /**
     * 查询用户的所有反馈
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<Feedback> findByUserId(Long userId);
    
    /**
     * 查询所有反馈
     * @return 反馈列表
     */
    List<Feedback> findAll();
    
    /**
     * 根据状态查询反馈
     * @param status 状态
     * @return 反馈列表
     */
    List<Feedback> findByStatus(String status);
    
    /**
     * 更新反馈状态
     * @param id 反馈ID
     * @param status 新状态
     * @return 更新后的反馈
     */
    Feedback updateStatus(Long id, String status);
    
    /**
     * 删除反馈
     * @param id 反馈ID
     */
    void deleteFeedback(Long id);
}