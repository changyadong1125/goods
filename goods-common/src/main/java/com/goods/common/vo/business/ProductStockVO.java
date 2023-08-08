package com.goods.common.vo.business;

import lombok.Data;

/** 商品库存
 **/
@Data
public class ProductStockVO {

    private Long id;

    private String name;

    private String pNum;

    private String model;

    private String unit;

    private String remark;

    private Long stock;

    private String imageUrl;
}
