package com.imooc.ad.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.mysql.constant.OpType;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    @PostConstruct
    public void init(){
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_PLAN)
        );
        adPlanStrings.forEach(p-> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p,AdPlanTable.class),
                OpType.ADD
        ));
        /**************************/
        List<String> adCreativeStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(p-> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(p,CreativeTable.class),
                OpType.ADD
        ));
        /*****************/
        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_UNIT)
        );

        adUnitStrings.forEach(p->AdLevelDataHandler.handleLevel3(
                JSON.parseObject(p, AdUnitTable.class),
                OpType.ADD
        ));

        /**********/
        List<String> creativeUnitStrings = loadDumpData(
                String.format(
                        "%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_CREATIVE_UNIT
                )
        );

        creativeUnitStrings.forEach(p->AdLevelDataHandler.handleLevel3(
                JSON.parseObject(p, CreativeUnitTable.class),
                OpType.ADD
        ));
        /************/
        List<String> unitDistrictStrings = loadDumpData(
                String.format(
                        "%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_UNIT_DISTRICT
                )
        );
        unitDistrictStrings.forEach(p->AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p, AdUnitDistrictTable.class),
                OpType.ADD
        ));
        /*****/
        List<String> unitItStrings = loadDumpData(
                String.format(
                        "%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_UNIT_IT
                )
        );
        unitItStrings.forEach(p->AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p,AdUnitItTable.class),
                OpType.ADD
        ));
        /***************/
        List<String> unitKeywordStrings = loadDumpData(
                String.format(
                        "%s%s",
                        DConstant.DATA_ROOT_DTR,
                        DConstant.AD_UNIT_KEYWORD
                )
        );
        unitKeywordStrings.forEach(p -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(p,AdUnitKeywordTable.class),
                OpType.ADD
        ));


    }
    private List<String> loadDumpData(String filename){
        try(BufferedReader br = Files.newBufferedReader(
                Paths.get(filename)
        )){
            return br.lines().collect(Collectors.toList());
        }catch (IOException ex){
            throw new RuntimeException();
        }

    }
}
