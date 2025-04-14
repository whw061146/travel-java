package com.example.travelplanner.service;

import com.example.travelplanner.model.entity.Plan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本推荐策略实现类
 * 基于预设规则生成行程计划，作为AI接口不可用时的备用方案
 */
@Service
public class BasicRecommendationStrategy implements RecommendationStrategy {

    // 预设的热门目的地及其景点
    private static final Map<String, String[]> POPULAR_ATTRACTIONS = new HashMap<>();
    
    static {
        // 初始化一些热门目的地及景点
        POPULAR_ATTRACTIONS.put("北京", new String[]{"故宫", "长城", "颐和园", "天坛", "王府井", "798艺术区"});
        POPULAR_ATTRACTIONS.put("上海", new String[]{"外滩", "东方明珠", "豫园", "迪士尼乐园", "南京路", "田子坊"});
        POPULAR_ATTRACTIONS.put("广州", new String[]{"白云山", "陈家祠", "沙面", "长隆旅游度假区", "广州塔", "上下九步行街"});
        POPULAR_ATTRACTIONS.put("深圳", new String[]{"世界之窗", "欢乐谷", "东部华侨城", "深圳湾公园", "大梅沙", "深圳博物馆"});
        POPULAR_ATTRACTIONS.put("杭州", new String[]{"西湖", "灵隐寺", "西溪湿地", "宋城", "千岛湖", "雷峰塔"});
    }

    @Override
    public Plan generatePlan(Plan plan) {
        StringBuilder resultBuilder = new StringBuilder();
        String destination = plan.getDestination();
        int days = plan.getDays() != null ? plan.getDays() : 3; // 默认3天行程
        
        resultBuilder.append("## ").append(destination).append("旅游行程推荐\n\n");
        
        // 获取目的地的景点，如果没有预设则使用通用模板
        String[] attractions = POPULAR_ATTRACTIONS.getOrDefault(destination, 
                new String[]{"当地博物馆", "城市公园", "历史街区", "特色美食街", "购物中心", "文化景点"});
        
        // 生成每日行程
        for (int day = 1; day <= days; day++) {
            resultBuilder.append("### 第").append(day).append("天\n\n");
            
            // 上午行程
            resultBuilder.append("- 上午：参观").append(attractions[(day - 1) % attractions.length])
                    .append("，了解当地文化历史\n");
            
            // 中午行程
            resultBuilder.append("- 中午：在附近的餐厅品尝当地特色美食\n");
            
            // 下午行程
            resultBuilder.append("- 下午：游览").append(attractions[(day + 1) % attractions.length])
                    .append("，欣赏美景\n");
            
            // 晚上行程
            resultBuilder.append("- 晚上：在").append(attractions[(day + 2) % attractions.length])
                    .append("附近享用晚餐，体验当地夜生活\n\n");
        }
        
        // 添加交通和住宿建议
        resultBuilder.append("## 交通建议\n\n");
        resultBuilder.append("- 市内交通：建议使用公共交通或打车\n");
        resultBuilder.append("- 景点间交通：主要景点之间可以乘坐公交或地铁\n\n");
        
        resultBuilder.append("## 住宿建议\n\n");
        resultBuilder.append("- 推荐住在市中心，交通便利，周边设施齐全\n");
        resultBuilder.append("- 如果偏好安静，可以选择郊区度假酒店\n\n");
        
        // 将生成的行程保存到plan对象
        plan.setPlanResult(resultBuilder.toString());
        
        return plan;
    }
}