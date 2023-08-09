package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ProductMapper;
import com.goods.business.mapper.ProductStockMapper;
import com.goods.business.service.ProductService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductStock;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:ProductServiceImp
 *
 * @author: smile
 * @create: 2023/8/7-18:55
 * @Version: v1.0
 * @Description:
 */
@Service
public class ProductServiceImp implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductStockMapper productStockMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资列表分页展示
     */
//    @Override    V1.0
//    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, String categoryKeys, ProductVO productVO) {
//        //分页
//        PageHelper.startPage(pageNum, pageSize);
//        //构建查询条件
//        Example E = new Example(Product.class);
//        Example.Criteria C = E.createCriteria();
//        //获取三级分类
//        Long[] longs = null;
//        if (!StringUtils.isEmpty(categoryKeys)) {
//            List<Long> categoryKeyList = Arrays.stream(categoryKeys.split(",")).map(Long::parseLong).collect(Collectors.toList());
//            longs = categoryKeyList.toArray(new Long[0]);
//            //一级分类
//            if (categoryKeyList.size() > 0 && !StringUtils.isEmpty(categoryKeyList.get(0))) {
//               C.andEqualTo("oneCategoryId", categoryKeyList.get(0));
//            }
//            if (categoryKeyList.size() > 1 && !StringUtils.isEmpty(categoryKeyList.get(1))) {
//                C.andEqualTo("twoCategoryId", categoryKeyList.get(1));
//            }
//            if (categoryKeyList.size() > 2 && !StringUtils.isEmpty(categoryKeyList.get(2))) {
//                C.andEqualTo("threeCategoryId", categoryKeyList.get(2));
//            }
//        }
//        if (null != productVO && !StringUtils.isEmpty(productVO.getName())) {
//           C.andLike("name", "%" + productVO.getName() + "%");
//        }
//        if (null != productVO && !StringUtils.isEmpty(productVO.getStatus())) {
//            C.andEqualTo("status", productVO.getStatus());
//        }
//        List<Product> products = productMapper.selectByExample(E);
//        Long[] finalLongs = longs;
//        List<ProductVO> productVOList = products.stream().map(P -> {
//            ProductVO V = new ProductVO();
//            BeanUtils.copyProperties(P, V);
//            V.setCategoryKeys(finalLongs);
//            return V;
//        }).collect(Collectors.toList());
//        PageInfo<Product> productPageInfo = new PageInfo<>(products);
//        return new PageVO<>(productPageInfo.getTotal(), productVOList);
//    }
    @Override
    public PageVO<ProductVO> findProductList(Integer pageNum, Integer pageSize, String categoryKeys, ProductVO productVO) {
        // 分页
        PageHelper.startPage(pageNum, pageSize);

        // 构建查询条件
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();

        // 构建分类查询条件
        buildCategoryCriteria(categoryKeys, criteria);

        // 构建其他查询条件
        buildOtherCriteria(productVO, criteria);

        // 查询数据库
        List<Product> products = productMapper.selectByExample(example);

        // 转换为ProductVO列表
        List<ProductVO> productVOList = convertToProductVOList(products, categoryKeys);

        // 创建分页结果
        PageInfo<Product> productPageInfo = new PageInfo<>(products);
        return new PageVO<>(productPageInfo.getTotal(), productVOList);
    }

    @Override
    public void add(ProductVO productVO) throws BusinessException {
        try {
            Product product = new Product();
            BeanUtils.copyProperties(productVO, product);
            product.setOneCategoryId(productVO.getCategoryKeys()[0]);
            product.setTwoCategoryId(productVO.getCategoryKeys()[1]);
            product.setThreeCategoryId(productVO.getCategoryKeys()[2]);
            product.setModifiedTime(new Date());
            if (!StringUtils.isEmpty(productVO.getId())) {
                productMapper.updateByPrimaryKey(product);
            } else {
                product.setStatus(2);
                product.setPNum(UUID.randomUUID().toString());
                product.setCreateTime(new Date());
                productMapper.insert(product);
            }
        } catch (BeansException e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("操作失败"));
        }
    }


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资资料
     */
    @Override
    public ProductVO getProductById(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        Long[] categoryKeys = Arrays.asList(product.getOneCategoryId(), product.getTwoCategoryId(), product.getThreeCategoryId()).toArray(new Long[0]);
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product, productVO);
        productVO.setCategoryKeys(categoryKeys);
        return productVO;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑物资资料
     */
    @Override
    public void update(Long id, ProductVO productVO) throws BusinessException {
        productVO.setId(id);
        this.add(productVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收站
     */
    @Override
    public void remove(Long id) throws BusinessException {
        try {
            extracted(id, 1);
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("操作失败"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收站删除
     */
    @Override
    public void delete(Long id) throws BusinessException {
        try {
            productMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("操作失败"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:恢复
     */
    @Override
    public void back(Long id) throws BusinessException {
        try {
            extracted(id, 0);
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("操作失败"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过审核
     */
    @Override
    public void publish(Long id) throws BusinessException {
        try {
            extracted(id, 1);
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("操作失败"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有
     */
    @Override
    public PageVO<Product> findProducts(Integer pageNum, Integer pageSize, Integer status, String categorys, String name) {
        PageHelper.startPage(pageNum, pageSize);
        Example E = new Example(Product.class);
        Example.Criteria criteria = E.createCriteria();
        criteria.andEqualTo("status", status);
        //构建分类条件
        buildCategoryCriteria(categorys, criteria);
        //构建name条件
        ProductVO productVO = new ProductVO();
        productVO.setName(name);
        buildOtherCriteria(productVO, criteria);
        List<Product> productList = productMapper.selectByExample(E);
        PageInfo<Product> productPageInfo = new PageInfo<>(productList);
        return new PageVO<>(productPageInfo.getTotal(), productList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取物资
     */
    @Override
    public PageVO<ProductStockVO> findProductStocks(Integer pageSize, Integer pageNum, String categorys, String name) {
        PageHelper.startPage(pageNum, pageSize);
        Example E = new Example(Product.class);
        Example.Criteria C = E.createCriteria();
        //构建分类条件
        this.buildCategoryCriteria(categorys, C);
        //构建name条件
        ProductVO productVO = new ProductVO();
        productVO.setName(name);
        buildOtherCriteria(productVO, C);
        List<Product> products = productMapper.selectByExample(E);
        List<ProductStockVO> productVOList = products.stream().map(product -> {
            ProductStockVO productStockVO = new ProductStockVO();
            BeanUtils.copyProperties(product, productStockVO);
            //设置库存
            Example example = new Example(ProductStock.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("pNum", product.getPNum());
            List<ProductStock> productStocks = productStockMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(productStocks)) {
                productStockVO.setStock(productStocks.get(0).getStock());
            }
            return productStockVO;
        }).collect(Collectors.toList());
        PageInfo<ProductStockVO> productVOPageInfo = new PageInfo<>(productVOList);
        return new PageVO<>(productVOPageInfo.getTotal(), productVOList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有库存
     */
    @Override
    public List<ProductStockVO> findAllStocks(Integer pageSize, Integer pageNum, String categorys, String name) {
        PageVO<ProductStockVO> productStocks = this.findProductStocks(pageSize, pageNum, categorys, name);
        return productStocks.getRows();
    }

    private void buildCategoryCriteria(String categoryKeys, Example.Criteria criteria) {
        if (!StringUtils.isEmpty(categoryKeys)) {
            List<Long> categoryKeyList = Arrays.stream(categoryKeys.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            for (int i = 0; i < Math.min(categoryKeyList.size(), 3); i++) {
                Long categoryId = categoryKeyList.get(i);
                if (categoryId != null) {
                    String field = getCategoryField(i);
                    criteria.andEqualTo(field, categoryId);
                }
            }
        }
    }

    private void extracted(Long id, Integer status) {
        Product product = productMapper.selectByPrimaryKey(id);
        product.setStatus(status);
        productMapper.updateByPrimaryKey(product);
    }

    private void buildOtherCriteria(ProductVO productVO, Example.Criteria criteria) {
        if (productVO != null) {
            if (!StringUtils.isEmpty(productVO.getName())) {
                criteria.andLike("name", "%" + productVO.getName() + "%");
            }
            if (!StringUtils.isEmpty(productVO.getStatus())) {
                criteria.andEqualTo("status", productVO.getStatus());
            }
        }
    }

    private List<ProductVO> convertToProductVOList(List<Product> products, String categoryKeys) {
        Long[] categoryKeyArray = StringUtils.isEmpty(categoryKeys) ? null :
                Arrays.stream(categoryKeys.split(","))
                        .map(Long::parseLong)
                        .toArray(Long[]::new);

        return products.stream().map(p -> {
            ProductVO v = new ProductVO();
            BeanUtils.copyProperties(p, v);
            v.setCategoryKeys(categoryKeyArray);
            return v;
        }).collect(Collectors.toList());
    }

    private String getCategoryField(int index) {
        return index == 0 ? "oneCategoryId" : (index == 1 ? "twoCategoryId" : "threeCategoryId");
    }
}
