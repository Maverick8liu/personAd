package com.imooc.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanTable {
    private Long plandId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;

}
