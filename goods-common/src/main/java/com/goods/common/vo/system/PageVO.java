package com.goods.common.vo.system;

import     lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class PageVO<T> {
    private long total;

    private List<T> rows=new ArrayList<>();

    public PageVO(long total, List<T> data) {
        this.total = total;
        this.rows = data;
    }
}
