package com.goods.business.service;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.system.PageVO;
import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service
 * class:BusinessCategoryService
 *
 * @author: smile
 * @create: 2023/8/7-10:11
 * @Version: v1.0
 * @Description:
 */
public interface ProductCategoryService   {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:层级分页展示
     */
    PageVO<ProductCategoryTreeNodeVO> getCategoryTree(Integer pageNum, Integer pageSize);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:重载方法
     */
    PageVO<ProductCategoryTreeNodeVO> getCategoryTree();

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取父级层级列表
     */
    List<ProductCategoryTreeNodeVO> getParentCategoryTree();

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加分类数据
     */
    Integer addCategory(ProductCategory productCategory);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑回显
     */
    ProductCategory findProductCategoryById(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改分类信息
     */
    void update(ProductCategory productCategory);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除分类
     *
     * @return
     */
    ResponseBean<String> delete(Long id) throws BusinessException;
}
