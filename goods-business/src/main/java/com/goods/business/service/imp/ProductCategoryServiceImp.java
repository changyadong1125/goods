package com.goods.business.service.imp;

import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:OutStockServiceImp
 *
 * @author: smile
 * @create: 2023/8/8-20:49
 * @Version: v1.0
 * @Description:
 */
@Service
public class ProductCategoryServiceImp implements ProductCategoryService {
    @Resource
    private ProductCategoryMapper productCategoryMapper;
    @Resource
    private ProductMapper productMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取物资类别层级展示 加分页
     */
    @Override
    public PageVO<ProductCategoryTreeNodeVO> getCategoryTree(Integer pageNum, Integer pageSize) {
        //获取所有的数据
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = getProductCategoryTreeNodeList();
        List<ProductCategoryTreeNodeVO> treeNodeVOS = CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
        // 手动进行分页
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, treeNodeVOS.size());
        List<ProductCategoryTreeNodeVO> pageData = treeNodeVOS.subList(startIndex, endIndex);
        return new PageVO<>(treeNodeVOS.size(), pageData);
    }

    @Override
    public PageVO<ProductCategoryTreeNodeVO> getCategoryTree() {
        //获取所有的数据
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = getProductCategoryTreeNodeList();
        List<ProductCategoryTreeNodeVO> treeNodeVOS = CategoryTreeBuilder.build(productCategoryTreeNodeVOList);
        return new PageVO<>(treeNodeVOS.size(), treeNodeVOS);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取父级层级分页列表
     */
    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        //获取treeNodeVOList 封装
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = getProductCategoryTreeNodeList();
        //封装层级
        return CategoryTreeBuilder.buildParent(productCategoryTreeNodeVOList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加分类数据
     */
    @Override
    public Integer addCategory(ProductCategory productCategory) {
        //添加分类数据
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        return productCategoryMapper.insert(productCategory);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显
     */
    @Override
    public ProductCategory findProductCategoryById(Long id) {
        return productCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改分类信息
     */
    @Override
    public void update(ProductCategory productCategory) {
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.updateByPrimaryKey(productCategory);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除分类
     */
    @Override
    public ResponseBean<String> delete(Long id) throws BusinessException {
        if (hasChildCategories(id)) {
            throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "该层级有子分类，不能删除");
        }

        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        if (!isTopLevelCategory(productCategory)) {
            if (hasProductsInCategory(id)) {
                throw new BusinessException(BusinessCodeEnum.PARAMETER_ERROR, "该层级有物资，不能删除");
            }
        }

        productCategoryMapper.deleteByPrimaryKey(id);
        return ResponseBean.success();
    }

    private boolean hasChildCategories(Long id) {
        Example example = new Example(ProductCategory.class);
        example.createCriteria().andEqualTo("pid", id);
        List<ProductCategory> childCategories = productCategoryMapper.selectByExample(example);
        return !CollectionUtils.isEmpty(childCategories);
    }

    private boolean isTopLevelCategory(ProductCategory category) {
        return category.getPid() == 0;
    }

    private boolean hasProductsInCategory(Long id) {
        Example example = new Example(Product.class);
        example.createCriteria().andEqualTo("threeCategoryId", id);
        List<Product> products = productMapper.selectByExample(example);
        return !CollectionUtils.isEmpty(products);
    }

    private List<ProductCategoryTreeNodeVO> getProductCategoryTreeNodeList() {
        //获取所有的数据
        List<ProductCategory> productCategoryList = productCategoryMapper.selectAll();
        //封装数据
        return productCategoryList.stream().map(productCategory -> {
            //判断当前数据是否为
            ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
            BeanUtils.copyProperties(productCategory, productCategoryTreeNodeVO);
            return productCategoryTreeNodeVO;
        }).collect(Collectors.toList());
    }
}
