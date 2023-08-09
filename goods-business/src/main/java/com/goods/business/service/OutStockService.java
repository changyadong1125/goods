package com.goods.business.service;
import com.goods.common.error.BusinessException;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import java.util.Map;

/**
 * project:goods
 * package:com.goods.business.service
 * class:OutStockService
 *
 * @author: smile
 * @create: 2023/8/8-20:49
 * @Version: v1.0
 * @Description:
 */
public interface OutStockService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:出库单（发放记录）数据分页列表展示
     */
    PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, Integer status, String outNum, Integer type);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查看明细
     */
    Map<String, Object> detail(Integer id, Integer pageNum);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加
     */
    void addOutStock(OutStockVO outStockVO, String username);

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
     * description:添加回收站
     */
    void remove(Long id) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除出库记录
     */
    void delete(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:还原
     */
    void back(Long id) throws BusinessException;
}
