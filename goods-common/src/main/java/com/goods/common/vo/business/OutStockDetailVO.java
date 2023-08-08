package com.goods.common.vo.business;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OutStockDetailVO {

    private String outNum;

    private Integer status;

    private Integer type;

    private String operator;

    private ConsumerVO consumerVO;

    private long total;/** 总数**/

    private List<OutStockItemVO> itemVOS=new ArrayList<>();

}
