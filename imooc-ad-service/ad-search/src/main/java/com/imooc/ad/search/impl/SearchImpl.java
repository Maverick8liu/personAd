package com.imooc.ad.search.impl;

import com.imooc.ad.index.CommonStatus;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.createUnit.CreativeUnitIndex;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interset.UnitItIndex;
import com.imooc.ad.index.keyword.UnitKeywordIndex;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl {

    public SearchResponse fetchAds(SearchRequest request){
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();

        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();

        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();

        ItFeature itFeature = request.getFeatureInfo().getItFeature();

        FeatureRelation relation = request.getFeatureInfo().getRelation();

        //构造响应对象
        SearchResponse response =  new SearchResponse();
        Map<String,List<SearchResponse.Creative>> adSlotZads = response.getAdSlotZAds();

        for(AdSlot adSlot:adSlots){
            Set<Long> targetUnitIdSet = null;
            //根据流量类型获取初始的Adunit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            if(relation == FeatureRelation.AND){
                filterKeywordFeature(adUnitIdSet,keywordFeature);
                filterDistrict(adUnitIdSet,districtFeature);
                filterIt(adUnitIdSet,itFeature);
            }else{
                targetUnitIdSet = getORRelationUnitIds(
                        adUnitIdSet,
                        keywordFeature,
                        districtFeature,
                        itFeature
                );
            }
            List<AdUnitObject> unitObjects =
                    DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);

            fiterAdUnitAndPlanStatus(unitObjects,CommonStatus.VALID);

            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);

            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(adIds);

            //通过Adslot实现CreativeObject的过滤
            filterCreativeByAdSlot(creativeObjects,adSlot.getHeight(),adSlot.getWidth(),adSlot.getType());

            adSlotZads.put(
                    adSlot.getAdSlotCode(),buildCreativeResponse(creativeObjects)
            );


        }


        return null;
    }


    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           ItFeature itFeature){
        if(CollectionUtils.isEmpty(adUnitIdSet)){
            return Collections.emptySet();
        }

        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet,keywordFeature);
        filterDistrict(districtUnitIdSet,districtFeature);
        filterIt(itUnitIdSet,itFeature);

        return  new HashSet<>(
                CollectionUtils.union(
                        CollectionUtils.union(
                                keywordUnitIdSet,
                                districtUnitIdSet),
                                itUnitIdSet
                        )
                );

    }


    private void filterKeywordFeature(
            Collection<Long> adUnitIds,KeywordFeature keywordFeature){
        if (CollectionUtils.isEmpty(adUnitIds)){
                return;
        }

        if(CollectionUtils.isNotEmpty(keywordFeature.getKeyword())){
            CollectionUtils.filter(
              adUnitIds,
              adUnitId -> DataTable.of(UnitKeywordIndex.class).match(adUnitId,keywordFeature.getKeyword())

            );
        }
    }


    private void filterDistrict( Collection<Long> adUnitIds,DistrictFeature districtFeature){
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(UnitDistrictIndex.class).match(adUnitId,districtFeature.getDistricts())

            );
        }
    }

    private void filterIt(Collection<Long> adUnitIds,ItFeature itFeature){
        if (CollectionUtils.isEmpty(adUnitIds)){
            return;
        }

        if(CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(
                    adUnitIds,
                    adUnitId -> DataTable.of(UnitItIndex.class).match(adUnitId,itFeature.getIts())
            );
        }
    }


    private void fiterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status){
        if(CollectionUtils.isEmpty(unitObjects)){
            return;
        }

        CollectionUtils.filter(
                unitObjects,
                object->object.getUnitStatus().equals(status)
                        && object.getAdPlanObject().getPlanStatus().equals(status));
    }


    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,Integer height,Integer width,List<Integer> type ){
        //is no empty
        if(CollectionUtils.isEmpty(creativeObjects)){
            return;
        }

        CollectionUtils.filter(
                creativeObjects,
                object -> object.getAduitStatus().equals(CommonStatus.VALID)
                    && object.getHeight().equals(height)
                    && object.getWidth().equals(width)
                    && type.contains(object.getType())
        );

    }


    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creativeObjects){
         if(CollectionUtils.isEmpty(creativeObjects)){
             return Collections.emptyList();
         }

         CreativeObject randomObject = creativeObjects.get(
                 Math.abs(new Random().nextInt()) % creativeObjects.size()
         );
         return Collections.singletonList(SearchResponse.conver(randomObject));
    }
}
