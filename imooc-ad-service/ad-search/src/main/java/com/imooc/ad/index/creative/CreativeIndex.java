package com.imooc.ad.index.creative;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long,CreativeObject>{

    private static Map<Long,CreativeObject> objectMap;
    static {
        objectMap = new ConcurrentHashMap<>();
    }

    public List<CreativeObject>  fetch(Collection<Long> adIds){
        List<CreativeObject> creativeObjects = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(adIds)){
            adIds.forEach(p->{
                CreativeObject creativeObject = get(p);
                if( null == creativeObject ){
                    log.error("CreativeObject no fount id is:{}",p);
                    return;
                }
                creativeObjects.add(creativeObject);
            });
        }
        return creativeObjects;
    }

    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("createIndex before add,{}",objectMap);
        objectMap.put(key,value);
        log.info("createIndex after add,{}",objectMap);
    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("createIndex before update,{}",objectMap);
        CreativeObject oldObject = objectMap.get(key);
        if(oldObject != null){
            objectMap.put(key,value);
        }else{
            oldObject.update(value);
        }
        log.info("createIndex after update,{}",objectMap);
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("createIndex before delete,{}",objectMap);
        objectMap.remove(key);
        log.info("createIndex after delete,{}",objectMap);
    }
}
