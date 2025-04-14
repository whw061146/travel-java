package com.example.travelplanner.service.impl;

import com.example.travelplanner.model.entity.Feedback;
import com.example.travelplanner.repository.FeedbackRepository;
import com.example.travelplanner.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 反馈服务实现类
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        // 设置创建时间
        if (feedback.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }
        feedback.setUpdatedAt(LocalDateTime.now());
        
        // 设置初始状态
        if (feedback.getStatus() == null || feedback.getStatus().isEmpty()) {
            feedback.setStatus("pending");
        }
        
        // 保存到数据库
        return feedbackRepository.save(feedback);
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public List<Feedback> findByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    @Override
    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> findByStatus(String status) {
        return feedbackRepository.findByStatus(status);
    }

    @Override
    public Feedback updateStatus(Long id, String status) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setStatus(status);
            feedback.setUpdatedAt(LocalDateTime.now());
            return feedbackRepository.save(feedback);
        }
        throw new RuntimeException("反馈不存在");
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}