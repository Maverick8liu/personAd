package com.imooc.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanObject {

    private Long plandId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;

    public void update(AdPlanObject newObject){
        if(null != newObject.getPlandId()){
            this.plandId = newObject.getPlandId();
        }
        if(null != newObject.getUserId()){
            this.userId = newObject.getUserId();
        }

        if(null != newObject.getPlanStatus()){
            this.planStatus = newObject.getPlanStatus();
        }

        if(null != newObject.getStartDate()){
            this.startDate = newObject.startDate;
        }

        if(null != newObject.getEndDate()){
            this.endDate = newObject.endDate;
        }
    }
}
