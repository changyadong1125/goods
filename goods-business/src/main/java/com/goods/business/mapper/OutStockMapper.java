package com.goods.business.mapper;

import com.goods.common.model.business.OutStock;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.OutStockItemVO;
import com.goods.common.vo.business.OutStockVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * project:goods
 * package:com.goods.business.mapper
 * class:OutStockMapper
 *
 * @author: smile
 * @create: 2023/8/8-20:49
 * @Version: v1.0
 * @Description:
 */
public interface OutStockMapper extends Mapper<OutStock> {
    List<OutStockVO> findOutStockList(Integer offset, Integer pageSize, Integer status, String outNum, Integer type);

    List<OutStockItemVO> findOUtStockDetail(Integer id, Integer pageNum);

    ConsumerVO findConsumerVO(Integer id);

    List<ConsumerVO> findAll();
}
