package com.goods.business.service;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Product;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service
 * class:ProductService
 *
 * @author: smile
 * @create: 2023/8/7-18:55
 * @Version: v1.0
 * @Description:
 */
public interface ProductService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资列表分页展示
     */
    PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, String categoryKeys, ProductVO productVO);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资资料
     */
    void add(ProductVO productVO) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资资料
     */
    ProductVO getProductById(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑物资资料
     */
    void update(Long id, ProductVO productVO) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收站
     */
    void remove(Long id) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收站删除
     */
    void delete(Long id) throws BusinessException;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:恢复
     */
    void back(Long id) throws BusinessException;

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
     * description:查询所有
     */
    PageVO<Product> findProducts(Integer pageNum, Integer pageSize, Integer status, String categorys, String name);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取物资
     */
    PageVO<ProductStockVO> findProductStocks(Integer pageSize, Integer pageNum, String categorys, String name);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有库存
     */
    List<ProductStockVO> findAllStocks(Integer pageSize, Integer pageNum, String categorys, String name);
}
