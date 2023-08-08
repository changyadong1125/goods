package com.goods.common.vo.business;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InStockDetailVO {

    private String inNum;

    private Integer status;

    private Integer type;

    private String operator;

    private SupplierVO supplierVO;

    private long total;/** 总数**/

    private List<InStockItemVO> itemVOS=new ArrayList<>();

}


