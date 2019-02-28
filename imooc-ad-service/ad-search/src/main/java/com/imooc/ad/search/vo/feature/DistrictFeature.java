package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature {

    private List<ProviceAndCity> districts;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviceAndCity{
        private String province;
        private String city;
    }
}
