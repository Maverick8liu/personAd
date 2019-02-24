package com.imooc.ad.dao;

import com.imooc.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<AdPlan,Long>{


    AdPlan findByUserIdAndPlanName(Long userId,String planName);

    List<AdPlan> findAllByPlanStatus(Integer status);

    List<AdPlan> findAllByIdInAndUserId(List<Long> ids, Long userId);

    AdPlan findByIdAndUserId(Long id, Long userId);
}
