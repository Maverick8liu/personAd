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
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.entity.AdPlan;
import com.imooc.ad.entity.AdUnit;
import com.imooc.ad.entity.Creative;
import com.imooc.ad.entity.unit_condition.AdUnitDistrict;
import com.imooc.ad.entity.unit_condition.AdUnitIt;
import com.imooc.ad.entity.unit_condition.AdUnitKeyword;
import com.imooc.ad.entity.unit_condition.CreativeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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

    //@Test
    public void testWrite(){
        String fileName = String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_PLAN);
        System.out.println("***********fileName*********:"+fileName);
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write("***********fileName*********1:"+fileName);
            writer.write("***********fileName*********2:"+fileName);
            writer.write("***********fileName*********3:"+fileName);
            writer.write("***********fileName*********4:"+fileName);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }
    }

    @Test
    public void dumAdTableData(){
        dumpAdPlanTables(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_PLAN));
        dumpAdUnitTables(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_UNIT));
        dumpCreativeTable(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_CREATIVE));
        dumpAdCreativeUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_CREATIVE_UNIT));
        dumpAdUnitDistrictTable(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_UNIT_DISTRICT));
        dumpAdUnitItTable(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_UNIT_IT));
        dumpAdUnitKeywordTable(String.format("%s%s", DConstant.DATA_ROOT_DTR,DConstant.AD_UNIT_KEYWORD));
    }



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
            writer.close();
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
            writer.close();
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("");
        }
    }

    /**
     * table creative_unit
     * @param fileName
     */
    private void dumpAdCreativeUnitTable(String fileName){
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();

        if(CollectionUtils.isEmpty(creativeUnits)){
            return;
        }
        List<CreativeUnitTable> creativeUnitTables = new ArrayList<>();
        creativeUnits.forEach(p->creativeUnitTables.add(
                new CreativeUnitTable(
                        p.getCreativeId(),
                        p.getUnitId()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(CreativeUnitTable planTable:creativeUnitTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("dumpAdCreativeUnitTable error");
        }

    }

    /**
     * table adunit_district
     * @param fileName
     */
    private void dumpAdUnitDistrictTable(String fileName){
        List<AdUnitDistrict> unitDistricts = unitDistrictRepository.findAll();

        if(CollectionUtils.isEmpty(unitDistricts)){
            return;
        }
        List<AdUnitDistrictTable> unitDistrictTables = new ArrayList<>();
        unitDistricts.forEach(p->unitDistrictTables.add(
                new AdUnitDistrictTable(
                        p.getUnitId(),
                        p.getProvince(),
                        p.getCity()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(AdUnitDistrictTable planTable:unitDistrictTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("dumpAdCreativeUnitTable error");
        }
    }

    /**
     * table adunit_keyword
     * @param fileName
     */
    private void dumpAdUnitKeywordTable(String fileName){
        List<AdUnitKeyword> unitKeywords = unitKeywordRepository.findAll();
        if(CollectionUtils.isEmpty(unitKeywords)){
            return;
        }
        List<AdUnitKeywordTable> unitKeywordTables = new ArrayList<>();
        unitKeywords.forEach(p->unitKeywordTables.add(
                new AdUnitKeywordTable(
                        p.getUnitId(),
                        p.getKeyword()
                )
        ));

        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(AdUnitKeywordTable planTable:unitKeywordTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("dumpAdUnitKeywordTable error");
        }
    }

    /**
     * adunit_it
     * @param fileName
     */
    private void dumpAdUnitItTable(String fileName){
        List<AdUnitIt> unitIts = unitItRepository.findAll();
        if(CollectionUtils.isEmpty(unitIts)){
            return;
        }

        List<AdUnitItTable> unitItTables = new ArrayList<>();
        unitIts.forEach(p->unitItTables.add(
                new AdUnitItTable(
                        p.getUnitId(),
                        p.getItTag()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for(AdUnitItTable planTable:unitItTables){
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("dumpAdUnitItTable error");
        }
    }


}
