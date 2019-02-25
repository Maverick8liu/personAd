package com.imooc.ad.Service;

import com.imooc.ad.Application;
import com.imooc.ad.constant.CommonStatus;
import com.imooc.ad.dao.AdPlanRepository;
import com.imooc.ad.dao.AdUnitRepository;
import com.imooc.ad.dao.CreativeRepository;
import com.imooc.ad.dao.unit_condtion.AdUnitDistrictRepository;
import com.imooc.ad.dao.unit_condtion.AdUnitItRepository;
import com.imooc.ad.dao.unit_condtion.AdUnitKeywordRepository;
import com.imooc.ad.dao.unit_condtion.CreativeUnitRepository;
import com.imooc.ad.dump.table.AdPlanTable;
import com.imooc.ad.entity.AdPlan;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpService {

    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;
    @Autowired
    private AdUnitItRepository unitItRepository;
    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;

    private void dumpAdPlanTables(String fileName){
        List<AdPlan> adPlans = planRepository.findAllByPlanStatus(
                CommonStatus.VALID.getStatus()
        );

        if(CollectionUtils.isEmpty(adPlans)){
            return;
        }

        List<AdPlanTable> planTables = new ArrayList<>();
      /*  adPlans.forEach(p->planTables.add(
                p.getId(),
                p.getUserId(),
                p.getPlanStatus(),
                p.getStartDate(),
                p.getEndDate()
        ));*/


    }

}
