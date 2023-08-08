package com.goods.business.service.imp;

import com.goods.business.mapper.InStockMapper;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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
    public PageVO<Product> remove(Integer id) {

        return null;
    }

    @Override
    public void addIntoStock(InStockVO inStockVO) {
        //
    }

}
