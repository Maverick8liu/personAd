package com.imooc.ad.search;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.ApplicationSearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import com.imooc.ad.search.vo.media.App;
import com.imooc.ad.search.vo.media.Device;
import com.imooc.ad.search.vo.media.Geo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationSearch.class},webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SearchTest {

    @Autowired
    private ISearch search;

   @Test
    public void testFetchAds(){
        SearchRequest request = new SearchRequest();
        request.setMediaId("imooc-ad");

        //第一个测试条件
        request.setRequestInfo(
                new SearchRequest.RequestInfo("aaa",
                        Collections.singletonList(new AdSlot(
                                "ad-x",
                                1,
                                1080,
                                720,
                                Arrays.asList(1,2),
                                1000
                        )),
                        buildExampleApp(),
                        buildExampleGet(),
                        buildExampleDevice()
                        )
        );

        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马","大众"),
                Collections.singletonList(
                        new DistrictFeature.ProviceAndCity(
                                "安徽省", "合肥市"
                        )),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.OR
        ));

        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(search.fetch(request)));


        //第二个测试条件
        request.setRequestInfo(
                new SearchRequest.RequestInfo("aaa",
                        Collections.singletonList(new AdSlot(
                                "ad-y",
                                1,
                                1080,
                                720,
                                Arrays.asList(1,2),
                                1000
                        )),
                        buildExampleApp(),
                        buildExampleGet(),
                        buildExampleDevice()
                )
        );

        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马","大众","标志"),
                Collections.singletonList(
                        new DistrictFeature.ProviceAndCity(
                                "安徽省", "合肥市"
                        )),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.OR
        ));

        System.out.println(JSON.toJSONString(request));
        System.out.println(JSON.toJSONString(search.fetch(request)));



    }


    private App buildExampleApp(){
        return new App("imooc","imooc","com.imooc","video");
    }

    private Geo buildExampleGet(){
        return new Geo((float)100.28,(float)88.61,"shenzhen","广东");
    }

    private Device buildExampleDevice(){
        return new Device(
                "iPhone",
                "0xxxxxx",
                "127.0.0.1",
                "x",
                "1080 720",
                "1080 720",
                "sdfsdf"
        );
    }

    private SearchRequest.FeatureInfo buildExampleFeatureInfo(
            List<String> keywords,
            List<DistrictFeature.ProviceAndCity> proviceAndCities,
            List<String> its,
            FeatureRelation relation){

        return new SearchRequest.FeatureInfo(
                new KeywordFeature(keywords),
                new DistrictFeature(proviceAndCities),
                new ItFeature(its),
                relation
        );
    }
}
