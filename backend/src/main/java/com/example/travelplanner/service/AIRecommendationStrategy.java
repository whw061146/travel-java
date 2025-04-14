package com.example.travelplanner.service;

import com.example.travelplanner.config.KimiApiClient;
import com.example.travelplanner.model.entity.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基于AI的推荐策略实现类
 * 使用Kimi API生成行程计划
 */
@Service
public class AIRecommendationStrategy implements RecommendationStrategy {

    private final KimiApiClient kimiApiClient;

    @Autowired
    public AIRecommendationStrategy(KimiApiClient kimiApiClient) {
        this.kimiApiClient = kimiApiClient;
    }

    @Override
    public Plan generatePlan(Plan plan) {
        // 构建提示词
        String prompt = buildPrompt(plan);
        
        // 调用Kimi API获取行程推荐
        String aiResponse = kimiApiClient.sendRequest(prompt);
        
        // 将AI返回的结果保存到plan对象
        plan.setPlanResult(aiResponse);
        
        return plan;
    }
    
    /**
     * 根据用户请求构建提示词
     * @param plan 行程计划对象
     * @return 构建的提示词
     */
    private String buildPrompt(Plan plan) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("请为我规划一次");
        
        // 添加出发地信息（如果有）
        if (plan.getDeparture() != null && !plan.getDeparture().isEmpty()) {
            promptBuilder.append("从").append(plan.getDeparture());
        }
        
        // 添加目的地信息
        promptBuilder.append("到").append(plan.getDestination());
        
        // 添加天数信息
        if (plan.getDays() != null && plan.getDays() > 0) {
            promptBuilder.append("的").append(plan.getDays()).append("日旅游行程");
        } else {
            promptBuilder.append("的旅游行程");
        }
        
        // 添加偏好信息（如果有）
        if (plan.getPreferences() != null && !plan.getPreferences().isEmpty()) {
            promptBuilder.append("，我的偏好是：").append(plan.getPreferences());
        }
        
        // 添加详细要求
        promptBuilder.append("。请包括每日景点和路线推荐，以及交通、住宿和用餐建议。请以日期为结构组织内容，使其清晰易读。");
        
        return promptBuilder.toString();
    }
}