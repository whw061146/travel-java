package com.example.travelplanner.repository;

import com.example.travelplanner.model.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    /**
     * 根据用户ID查询所有行程计划
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
     * 根据用户ID和目的地查询行程计划
     * @param userId 用户ID
     * @param destination 目的地
     * @return 行程计划列表
     */
    List<Plan> findByUserIdAndDestination(Long userId, String destination);
}