package com.example.travelplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 推荐策略工厂类（工厂模式）
 * 负责根据条件选择并创建RecommendationStrategy实例
 */
@Component
public class RecommendationFactory {

    private final AIRecommendationStrategy aiRecommendationStrategy;
    private final BasicRecommendationStrategy basicRecommendationStrategy;

    @Autowired
    public RecommendationFactory(AIRecommendationStrategy aiRecommendationStrategy,
                                BasicRecommendationStrategy basicRecommendationStrategy) {
        this.aiRecommendationStrategy = aiRecommendationStrategy;
        this.basicRecommendationStrategy = basicRecommendationStrategy;
    }

    /**
     * 获取推荐策略
     * @param useAI 是否使用AI推荐
     * @return 推荐策略实例
     */
    public RecommendationStrategy getStrategy(boolean useAI) {
        if (useAI) {
            return aiRecommendationStrategy;
        } else {
            return basicRecommendationStrategy;
        }
    }

    /**
     * 获取默认推荐策略（默认使用AI推荐）
     * @return 推荐策略实例
     */
    public RecommendationStrategy getDefaultStrategy() {
        return aiRecommendationStrategy;
    }

    /**
     * 根据AI服务可用性获取推荐策略
     * 如果AI服务不可用，则使用基本推荐策略
     * @param aiServiceAvailable AI服务是否可用
     * @return 推荐策略实例
     */
    public RecommendationStrategy getStrategyByAvailability(boolean aiServiceAvailable) {
        if (aiServiceAvailable) {
            return aiRecommendationStrategy;
        } else {
            return basicRecommendationStrategy;
        }
    }
}