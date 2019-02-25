package com.imooc.ad.index.creative;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeObject {

    private Long adId;
    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private  Integer width;
    private Integer aduitStatus;
    private String addUrl;

    public void update(CreativeObject newObject){
        if(null != newObject.getAdId()){
            this.adId = newObject.getAdId();
        }
        if(null != newObject.getName()){
            this.name = newObject.getName();
        }

        if(null != newObject.getType()){
            this.type = newObject.getType();
        }

        if(null != newObject.getMaterialType()){
            this.materialType = newObject.getMaterialType();
        }

        if(null != newObject.getHeight()){
            this.height = newObject.getHeight();
        }

        if(null != newObject.getWidth()){
            this.width = newObject.getWidth();
        }

        if(null != newObject.getAduitStatus()){
            this.aduitStatus = newObject.getAduitStatus();
        }

        if(null != newObject.getAddUrl()){
            this.addUrl = newObject.getAddUrl();
        }
    }

}
