package com.example.travelplanner.controller;

import com.example.travelplanner.model.entity.Plan;
import com.example.travelplanner.service.PlanService;
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
 * 行程控制器
 * 提供行程规划和历史记录查询接口
 */
@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    /**
     * 创建行程计划
     * @param plan 行程计划信息
     * @return 创建的行程计划
     */
    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody Plan plan) {
        try {
            // 获取当前登录用户ID
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                // 如果用户已登录，设置用户ID
                // 注意：这里假设UserDetailsService返回的principal中的username是用户ID的字符串形式
                // 实际项目中可能需要调整，比如通过UserService查询用户ID
                plan.setUserId(Long.parseLong(authentication.getName()));
            }
            
            // 创建行程计划
            Plan createdPlan = planService.createPlan(plan);
            return ResponseEntity.ok(createdPlan);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取行程计划详情
     * @param id 行程计划ID
     * @return 行程计划详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanById(@PathVariable Long id) {
        Optional<Plan> planOpt = planService.findById(id);
        if (planOpt.isPresent()) {
            return ResponseEntity.ok(planOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取当前用户的历史行程计划
     * @return 历史行程计划列表
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory() {
        // 获取当前登录用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            // 查询用户的历史行程计划
            Long userId = Long.parseLong(authentication.getName());
            List<Plan> plans = planService.findByUserId(userId);
            return ResponseEntity.ok(plans);
        } else {
            return ResponseEntity.badRequest().body("用户未登录");
        }
    }

    /**
     * 根据目的地查询行程计划
     * @param destination 目的地
     * @return 行程计划列表
     */
    @GetMapping("/destination/{destination}")
    public ResponseEntity<?> getPlansByDestination(@PathVariable String destination) {
        List<Plan> plans = planService.findByDestination(destination);
        return ResponseEntity.ok(plans);
    }

    /**
     * 删除行程计划
     * @param id 行程计划ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        try {
            planService.deletePlan(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "行程计划删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}