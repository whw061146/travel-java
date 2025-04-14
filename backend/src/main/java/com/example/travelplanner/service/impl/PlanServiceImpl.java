package com.example.travelplanner.service.impl;

import com.example.travelplanner.model.entity.Plan;
import com.example.travelplanner.repository.PlanRepository;
import com.example.travelplanner.service.PlanService;
import com.example.travelplanner.service.RecommendationFactory;
import com.example.travelplanner.service.RecommendationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 行程服务实现类
 */
@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final RecommendationFactory recommendationFactory;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository, RecommendationFactory recommendationFactory) {
        this.planRepository = planRepository;
        this.recommendationFactory = recommendationFactory;
    }

    @Override
    public Plan createPlan(Plan plan, boolean useAI) {
        // 设置创建时间
        if (plan.getCreatedAt() == null) {
            plan.setCreatedAt(LocalDateTime.now());
        }
        plan.setUpdatedAt(LocalDateTime.now());
        
        // 检查是否有相似的查询，如果有则直接返回已有结果（简单缓存机制）
        List<Plan> similarPlans = planRepository.findByUserIdAndDestination(plan.getUserId(), plan.getDestination());
        for (Plan existingPlan : similarPlans) {
            // 如果目的地、天数和偏好都相同，则认为是相似查询
            if (isSimilarPlan(existingPlan, plan)) {
                return existingPlan;
            }
        }
        
        // 获取推荐策略
        RecommendationStrategy strategy = recommendationFactory.getStrategy(useAI);
        
        // 生成行程计划
        Plan generatedPlan = strategy.generatePlan(plan);
        
        // 保存到数据库
        return planRepository.save(generatedPlan);
    }

    @Override
    public Plan createPlan(Plan plan) {
        // 默认使用AI推荐
        return createPlan(plan, true);
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public List<Plan> findByUserId(Long userId) {
        return planRepository.findByUserId(userId);
    }

    @Override
    public List<Plan> findByDestination(String destination) {
        return planRepository.findByDestination(destination);
    }

    @Override
    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
    
    /**
     * 判断两个行程计划是否相似
     * @param plan1 行程计划1
     * @param plan2 行程计划2
     * @return 是否相似
     */
    private boolean isSimilarPlan(Plan plan1, Plan plan2) {
        // 目的地必须相同
        if (!plan1.getDestination().equals(plan2.getDestination())) {
            return false;
        }
        
        // 天数必须相同
        if (plan1.getDays() == null && plan2.getDays() != null ||
            plan1.getDays() != null && !plan1.getDays().equals(plan2.getDays())) {
            return false;
        }
        
        // 出发地必须相同（如果都有设置）
        if (plan1.getDeparture() != null && plan2.getDeparture() != null &&
            !plan1.getDeparture().equals(plan2.getDeparture())) {
            return false;
        }
        
        // 偏好必须相似（简单判断，可以使用更复杂的相似度算法）
        if (plan1.getPreferences() != null && plan2.getPreferences() != null &&
            !plan1.getPreferences().equals(plan2.getPreferences())) {
            return false;
        }
        
        // 如果以上条件都满足，则认为是相似的行程计划
        return true;
    }
}