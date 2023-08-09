package com.goods.business.service.imp;

import com.goods.business.mapper.*;
import com.goods.business.service.OutStockService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.*;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.OutStockItemVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
public class OutStockServiceImp implements OutStockService {
    @Resource
    private OutStockMapper outStockMapper;
    @Resource
    private ConsumerMapper consumerMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OutStockInfoMapper outStockInfoMapper;
    @Resource
    private ProductStockMapper productStockMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:出库单（发放记录）数据分页列表展示
     */
    @Override
    public PageVO<OutStockVO> findOutStockList(Integer pageNum, Integer pageSize, Integer status, String outNum, Integer type) {
        Integer offset = (pageNum - 1) * pageSize;
        List<OutStockVO> outStockList = outStockMapper.findOutStockList(offset, pageSize, status, outNum, type);
        return new PageVO<>(outStockList.size(), outStockList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查看明细
     */
    @Override
    public Map<String, Object> detail(Integer id, Integer pageNum) {
        HashMap<String, Object> map = new HashMap<>();
        List<OutStockItemVO> outStockItemVOS = outStockMapper.findOUtStockDetail(id, pageNum);
        ConsumerVO consumerVO = outStockMapper.findConsumerVO(id);
        map.put("itemVOS", outStockItemVOS);
        map.put("total", outStockItemVOS.size());
        map.put("consumerVO", consumerVO);
        map.put("status", outStockMapper.selectByPrimaryKey(id).getStatus());
        return map;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("all")
    public void addOutStock(OutStockVO outStockVO, String username) {
        //获取detail
        List<Object> products = outStockVO.getProducts();
        //准备outStock
        OutStock outStock = new OutStock();
        BeanUtils.copyProperties(outStockVO, outStock);
        //生成编号
        String outNum = UUID.randomUUID().toString().replace("-", "");

        //判断是否存在ConsumerId
        Long consumerId = outStockVO.getConsumerId();
        if (null == consumerId) {
            //保存consumer
            Consumer consumer = new Consumer();
            consumer.setCreateTime(new Date());
            consumer.setModifiedTime(new Date());
            BeanUtils.copyProperties(outStockVO, consumer);
            consumerMapper.insert(consumer);
            outStock.setConsumerId(consumer.getId());
        }

        //保存 明细
        AtomicInteger atomicInteger = new AtomicInteger();
        products.forEach(product -> {
            Map map = (Map) product;
            OutStockInfo outStockInfo = new OutStockInfo();
            outStockInfo.setCreateTime(new Date());
            outStockInfo.setModifiedTime(new Date());
            outStockInfo.setOutNum(outNum);
            outStockInfo.setPNum(productMapper.selectByPrimaryKey(map.get("productId")).getPNum());
            outStockInfo.setProductNumber((Integer) map.get("productNumber"));
            atomicInteger.addAndGet((Integer) map.get("productNumber"));
            outStockInfoMapper.insert(outStockInfo);
        });

        //保存 outStock
        outStock.setOutNum(outNum);
        outStock.setCreateTime(new Date());
        outStock.setProductNumber(atomicInteger.get());
        outStock.setStatus(2);
        outStock.setOperator(username);
        outStockMapper.insert(outStock);
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
            extracted(id, 0);
            //减库存
            OutStock outStock = outStockMapper.selectByPrimaryKey(id);
            Example E1 = new Example(OutStock.class);
            Example.Criteria C1 = E1.createCriteria();
            C1.andEqualTo("outNum", outStock.getOutNum());
            List<OutStockInfo> outStockInfos = outStockInfoMapper.selectByExample(E1);
            outStockInfos.forEach(info -> {
                Example E2 = new Example(ProductStock.class);
                Example.Criteria C2 = E2.createCriteria();
                C2.andEqualTo("pNum", info.getPNum());
                List<ProductStock> productStocks = productStockMapper.selectByExample(E2);
                ProductStock productStock = productStocks.get(0);
                productStock.setStock(productStock.getStock() - info.getProductNumber());
                productStockMapper.updateByPrimaryKey(productStock);
            });
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("出库失败！"));
        }
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
            throw new BusinessException(BusinessCodeEnum.valueOf("回收失败！"));
        }
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除出库记录
     */
    @Override
    public void delete(Long id) {
        //删除出库记录明细
        Example E = new Example(OutStockInfo.class);
        Example.Criteria criteria = E.createCriteria();
        criteria.andEqualTo("outNum", outStockMapper.selectByPrimaryKey(id).getOutNum());
        outStockInfoMapper.deleteByExample(E);
        //删除入库记录
        outStockMapper.deleteByPrimaryKey(id);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:还原
     */
    @Override
    public void back(Long id) throws BusinessException {
        try {
            extracted(id, 0);
        } catch (Exception e) {
            throw new BusinessException(BusinessCodeEnum.valueOf("回收失败！"));
        }
    }

    private void extracted(Long id, Integer status) {
        OutStock outStock = outStockMapper.selectByPrimaryKey(id);
        outStock.setStatus(status);
        outStock.setId(id);
        outStockMapper.updateByPrimaryKey(outStock);
    }
}
