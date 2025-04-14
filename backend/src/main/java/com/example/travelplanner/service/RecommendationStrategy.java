package com.example.travelplanner.service;

import com.example.travelplanner.model.entity.Plan;

/**
 * 推荐策略接口（策略模式）
 * 定义生成行程的方法
 */
public interface RecommendationStrategy {
    
    /**
     * 生成行程计划
     * @param plan 包含用户请求参数的Plan对象
     * @return 生成的行程计划
     */
    Plan generatePlan(Plan plan);
}