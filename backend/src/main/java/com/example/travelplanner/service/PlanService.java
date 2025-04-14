package com.example.travelplanner.service;

import com.example.travelplanner.model.entity.Plan;

import java.util.List;
import java.util.Optional;

/**
 * 行程服务接口
 */
public interface PlanService {
    
    /**
     * 创建行程计划
     * @param plan 行程计划对象
     * @param useAI 是否使用AI推荐
     * @return 创建的行程计划
     */
    Plan createPlan(Plan plan, boolean useAI);
    
    /**
     * 创建行程计划（默认使用AI推荐）
     * @param plan 行程计划对象
     * @return 创建的行程计划
     */
    Plan createPlan(Plan plan);
    
    /**
     * 根据ID查询行程计划
     * @param id 行程计划ID
     * @return 行程计划对象
     */
    Optional<Plan> findById(Long id);
    
    /**
     * 查询用户的所有行程计划
     * @param userId 用户ID
     * @return 行程计划列表
     */
    List<Plan> findByUserId(Long userId);
    
    /**
     * 根据目的地查询行程计划
     * @param destination 目的地
     * @return 行程计划列表
     */
    List<Plan> findByDestination(String destination);
    
    /**
     * 删除行程计划
     * @param id 行程计划ID
     */
    void deletePlan(Long id);
}