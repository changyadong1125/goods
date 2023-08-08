package com.goods.business.service.imp;

import com.goods.business.mapper.*;
import com.goods.business.service.InStockService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.InStock;
import com.goods.common.model.business.InStockInfo;
import com.goods.common.model.business.ProductStock;
import com.goods.common.model.business.Supplier;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:InStockServiceImp
 *
 * @author: smile
 * @create: 2023/8/8-10:30
 * @Version: v1.0
 * @Description:
 */
@Service
public class InStockServiceImp implements InStockService {
    @Resource
    private InStockMapper inStockMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private InStockInfoMapper inStockInfoMapper;
    @Resource
    private ProductStockMapper productStockMapper;


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库单（入库记录）数据分页列表展示
     */
    @Override
    public PageVO<InStockVO> findInStockList(Integer pageNum, Integer pageSize, InStockVO inStockVO) {
        Integer offset = pageSize * (pageNum - 1);
        List<InStockVO> inStockVOList = inStockMapper.findInStockList(offset, pageSize, inStockVO);
        return new PageVO<>(inStockVOList.size(), inStockVOList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:
     */
    @Override
    public HashMap<String, Object> detail(Integer id, Integer pageNum) {
        HashMap<String, Object> map = new HashMap<>();
        List<InStockItemVO> productStockVOList = inStockMapper.detail(id, pageNum);
        InStockVO inStockVO = inStockMapper.getSupplierVO(id);
        map.put("itemVOS", productStockVOList);
        map.put("total", productStockVOList.size());
        map.put("supplierVO", inStockVO);
        map.put("status", inStockMapper.selectByPrimaryKey(id).getStatus());
        return map;
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
            throw new BusinessException(BusinessCodeEnum.valueOf("修改失败！"));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public void addIntoStock(InStockVO inStockVO) {
        Long supplierId = inStockVO.getSupplierId();
        List<Object> products = inStockVO.getProducts();
        InStock inStock = new InStock();
        Supplier supplier = new Supplier();
        //判断suppId是否存在
        if (null == supplierId) {
            supplier.setCreateTime(new Date());
            supplier.setModifiedTime(new Date());
            BeanUtils.copyProperties(inStockVO, supplier);
            supplierMapper.insert(supplier);
            inStock.setSupplierId(supplier.getId());
        } else {
            BeanUtils.copyProperties(inStockVO, inStock);
        }
        //如果suppId不存在 进行入库操作
        inStock.setCreateTime(new Date());
        inStock.setStatus(2);
        //todo:
        inStock.setOperator("admin");
        inStock.setModified(new Date());
        inStock.setProductNumber(products.size());
        inStock.setInNum(UUID.randomUUID().toString().replaceAll("-", ""));
        inStockMapper.insert(inStock);
        //存储入库明细
        List<InStockInfo> inStockInfoList = products.stream().map(product -> {
            InStockInfo inStockInfo = new InStockInfo();
            inStockInfo.setInNum(inStock.getInNum());
            Map map = (Map) product;
            inStockInfo.setPNum(productMapper.selectByPrimaryKey(map.get("productId")).getPNum());
            inStockInfo.setProductNumber((Integer) map.get("productNumber"));
            inStockInfo.setCreateTime(new Date());
            inStockInfo.setModifiedTime(new Date());
            return inStockInfo;
        }).collect(Collectors.toList());
        inStockInfoList.forEach(inStockInfo -> inStockInfoMapper.insert(inStockInfo));
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
            //修改状态
            extracted(id, 0);
            //入库
            InStock inStock = inStockMapper.selectByPrimaryKey(id);
            Example E = new Example(InStockInfo.class);
            Example.Criteria criteria = E.createCriteria();
            criteria.andEqualTo("inNum", inStock.getInNum());
            List<InStockInfo> inStockInfos = inStockInfoMapper.selectByExample(E);
            inStockInfos.forEach(info -> {
                ProductStock productStock = new ProductStock();
                productStock.setPNum(info.getPNum());
                productStock.setStock(info.getProductNumber().longValue());
                productStockMapper.insert(productStock);
            });
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("修改失败！"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除入库记录明细
        Example E = new Example(InStockInfo.class);
        Example.Criteria criteria = E.createCriteria();
        criteria.andEqualTo("inNum", inStockMapper.selectByPrimaryKey(id).getInNum());
        inStockInfoMapper.deleteByExample(E);
        //删除入库记录
        inStockMapper.deleteByPrimaryKey(id);
    }

    private void extracted(Long id, Integer status) {
        InStock inStock = inStockMapper.selectByPrimaryKey(id);
        inStock.setStatus(status);
        inStockMapper.updateByPrimaryKey(inStock);
    }
}
