package com.imooc.ad.handler;

import com.imooc.ad.dump.table.*;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.createUnit.CreativeUnitIndex;
import com.imooc.ad.index.createUnit.CreativeUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interset.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.索引之间存在着层级的划粉，也就是依赖关系的划分
 * 2.加载全量索引其实是增量索引 “添加”的一种特殊实现
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable planTable, OpType type){
        AdPlanObject planObject = new AdPlanObject(
                planTable.getPlandId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handlerBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlandId(),
                planObject,
                type
        );

    }


    public static void handleLevel2(CreativeTable creativeTable,
                                    OpType type){
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAduitStatus(),
                creativeTable.getAddUrl()
        );
        handlerBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }


    public static void handleLevel3(AdUnitTable unitTable,OpType type){
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).
                                    get(unitTable.getPlandId());
        if(null == adPlanObject){
            log.error("handlerleve3 found AdPlanObject Error:{}",
                unitTable.getPlandId());
            return;
        }

        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlandId(),
                adPlanObject
        );
        handlerBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                unitObject.getUnitId(),
                unitObject,
                type
        );
    }


    public static void handleLevel3(CreativeUnitTable creativeUnitTable,
                                    OpType type){
        if(type == OpType.UPDATE){
            log.error("creativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(creativeUnitTable.getUnitId());

        CreativeObject creativeObject = DataTable.of(
                CreativeIndex.class
        ).get(creativeUnitTable.getAddId());

        if(null == unitObject || null == creativeObject){
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAddId(),
                creativeUnitTable.getUnitId()
        );

        handlerBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAddId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }


    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable,
                                    OpType type){
        if(type == OpType.UPDATE){
            log.error("creativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitDistrictTable.getUnitId());

        if(unitObject == null){
            log.error("AdUnitDistrictTable index error:{}",
                    unitDistrictTable.getUnitId());
        }

        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        Set<Long> value = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );

        handlerBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                type
        );
    }


    public static void handleLevel4(AdUnitItTable unitItTable,
                                    OpType type){
        if(type == OpType.UPDATE){
            log.error("AdUnitItTable not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitItTable.getUnitId());

        if(unitObject == null){
            log.error("AdUnitDistrictTable index error:{}",
                    unitItTable.getUnitId());
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );

        handlerBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }


    public static void handleLevel4(AdUnitKeywordTable unitKeywordTable,
                                    OpType type){
        if(type == OpType.UPDATE){
            log.error("AdUnitItTable not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitKeywordTable.getUnitId());

        if(unitObject == null){
            log.error("AdUnitDistrictTable index error:{}",
                    unitKeywordTable.getUnitId());
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitKeywordTable.getUnitId())
        );

        handlerBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                 unitKeywordTable.getKeyword(),
                value,
                type
        );
    }

    private static <K,V> void handlerBinlogEvent(IndexAware<K,V> index,
                                                 K key,
                                                 V value,
                                                 OpType type){
        switch (type){
            case ADD:
                index.add(key,value);
                break;
            case UPDATE:
                index.update(key,value);
                break;
            case DELETE:
                index.delete(key,value);
                break;
            default:
                break;
        }
    }



}
