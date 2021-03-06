package com.imooc.ad.index.createUnit;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adunit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject>{

    private static Map<String,CreativeUnitObject> objectMap;

    private static Map<Long,Set<Long>>  creativeUnitMap;

    private static Map<Long,Set<Long>> unitCreativeMap;

    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex before add,{}",objectMap);
        objectMap.put(key,value);

        Set<Long> unitSet = creativeUnitMap.get(value.getAddId());
        if(CollectionUtils.isEmpty(unitSet)){
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getAddId(),unitSet);
        }

        unitSet.add(value.getUnitId());

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isEmpty(creativeSet)){
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(),creativeSet);
        }

        log.info("CreativeUnitIndex after add,{}",objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {

    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex before delete,{}",objectMap);
        objectMap.remove(key);
        Set<Long> unitSet = creativeUnitMap.get(value.getAddId());
        if(CollectionUtils.isNotEmpty(unitSet)){
            unitSet.remove(value.getUnitId());
        }

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(creativeSet)){
            creativeSet.remove(value.getAddId());
        }



        log.info("CreativeUnitIndex after delete,{}",objectMap);
    }

    public List<Long> selectAds(List<AdUnitObject> unitObjects){
        if(CollectionUtils.isEmpty(unitObjects)){
            return Collections.emptyList();
        }
        List<Long> creativeIds = new ArrayList<>();
        for(AdUnitObject object:unitObjects){
            Set<Long> subCreativeIds = unitCreativeMap.get(object.getUnitId());
            if(CollectionUtils.isEmpty(subCreativeIds)){
                creativeIds.addAll(subCreativeIds);
            }
        }

        return creativeIds;
    }
}
