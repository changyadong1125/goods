package com.goods.common.vo.business;

import lombok.Data;


@Data
public class OutStockItemVO {

    private Long id;

    private String pNum;

    private String name;

    private String model;

    private String unit;

    private String imageUrl;

    private int count;

}
