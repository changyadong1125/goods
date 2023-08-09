package com.goods.business.service;

import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service
 * class:SupplierService
 *
 * @author: smile
 * @create: 2023/8/7-15:57
 * @Version: v1.0
 * @Description:
 */
public interface SupplierService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件查询物资来源
     */

    PageVO<Supplier> findSupplierList(Integer pageNum, Integer pageSize, SupplierVO supplierVO);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资来源
     */
    void addSupplier(Supplier supplier);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资来源
     */
    Supplier findSupplierById(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改物资来源
     */
    void update(Long id, Supplier supplier);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除物资来源
     */
    void delete(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有
     */
    List<Supplier> findAll();
}
