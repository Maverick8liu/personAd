package com.imooc.ad.Service;

import com.alibaba.fastjson.JSON;
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
import com.imooc.ad.dump.table.AdUnitTable;
import com.imooc.ad.dump.table.CreativeTable;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.Creative;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
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
        adPlans.forEach(p->planTables.add(
                new AdPlanTable(p.getId(),
                    p.getUserId(),
                    p.getPlanStatus(),
                    p.getStartDate(),
                    p.getEndDate()
                )
        ));
      Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(AdPlanTable planTable:planTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }

    }
    private void dumpAdUnitTables(String fileName){
        List<AdUnit> units =  unitRepository.findByUnitStatus(CommonStatus.VALID.getStatus());
        if(CollectionUtils.isEmpty(units)){
            return ;
        }
        List<AdUnitTable> unitTables = new ArrayList<>();
        units.forEach(p-> unitTables.add(
                new AdUnitTable(
                        p.getId(),
                        p.getUnitStatus(),
                        p.getPositionType(),
                        p.getPlanId()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(AdUnitTable planTable:unitTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }
    }


    private void dumpCreativeTable(String fileName){
        List<Creative> creatives = creativeRepository.findAll();
        if(CollectionUtils.isEmpty(creatives)){
            return;
        }

        List<CreativeTable> creativeTables = new ArrayList<>();
        creatives.forEach(p->creativeTables.add(
                new CreativeTable(
                        p.getId(),
                        p.getName(),
                        p.getType(),
                        p.getMaterialType(),
                        p.getHeight(),
                        p.getWidth(),
                        p.getAuditStatus(),
                        p.getUrl()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(CreativeTable planTable:creativeTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }


    }
}
