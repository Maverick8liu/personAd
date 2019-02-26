package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.mysql.dto.TableTemplate;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements IListener{

    @Resource(name="")
    private ISender sender;

    private final AggregationListener aggregationListener;
    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }


    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementsLister register:{}-{}");

        Constant.table2Db.forEach((k,v)->
                aggregationListener.register(v,k,this));
    }

    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate tableTemplate = eventData.getTable();
        EventType eventType = eventData.getEventType();

        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(tableTemplate.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        List<String> fieldList = tableTemplate.getOpTypeFieldSetMap().get(opType);
        if(null == fieldList){
            log.warn("{} no support for{]",opType,tableTemplate.getTableName());
            return;
        }

        for(Map<String,String> afterMap : eventData.getAfter()){
            Map<String,String> _afterMap = new HashMap<>();
            for(Map.Entry<String,String> entry:afterMap.entrySet()){
                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName,colValue);
            }
            rowData.getFiledValueMap().add(_afterMap);
        }
        sender.sender(rowData);

    }
}
