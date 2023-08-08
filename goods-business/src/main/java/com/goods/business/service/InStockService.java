package com.goods.business.service;

import com.goods.common.error.BusinessException;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;

import java.util.HashMap;

/**
 * project:goods
 * package:com.goods.business.service
 * class:InStockService
 *
 * @author: smile
 * @create: 2023/8/8-10:30
 * @Version: v1.0
 * @Description:
 */
public interface InStockService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库单（入库记录）数据分页列表展示
     *
     * @return
     */
    PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, InStockVO inStockVO);

    HashMap<String, Object> detail(Integer id, Integer pageNum);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加到回收站
     */
    void remove(Long id) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库
     */
    void addIntoStock(InStockVO inStockVO);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过审核
     */
    void publish(Long id) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    void delete(Long id);
}
