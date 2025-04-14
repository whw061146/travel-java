package com.example.travelplanner.repository;

import com.example.travelplanner.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    /**
     * 根据用户ID查询所有反馈
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<Feedback> findByUserId(Long userId);
    
    /**
     * 根据状态查询反馈
     * @param status 状态（pending, processed, ignored）
     * @return 反馈列表
     */
    List<Feedback> findByStatus(String status);
    
    /**
     * 根据用户ID和状态查询反馈
     * @param userId 用户ID
     * @param status 状态
     * @return 反馈列表
     */
    List<Feedback> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * 根据评分查询反馈
     * @param rating 评分
     * @return 反馈列表
     */
    List<Feedback> findByRating(Integer rating);
    
    /**
     * 根据评分范围查询反馈
     * @param minRating 最小评分
     * @param maxRating 最大评分
     * @return 反馈列表
     */
    List<Feedback> findByRatingBetween(Integer minRating, Integer maxRating);
}