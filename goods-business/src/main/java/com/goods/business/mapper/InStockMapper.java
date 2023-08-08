package com.goods.business.mapper;

import com.goods.common.model.business.InStock;
import com.goods.common.model.business.Product;
import com.goods.common.vo.business.InStockItemVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.ProductStockVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * project:goods
 * package:com.goods.business.mapper
 * class:InStockMapper
 *
 * @author: smile
 * @create: 2023/8/8-10:31
 * @Version: v1.0
 * @Description:
 */
public interface InStockMapper extends Mapper<InStock> {
    List<InStockVO> findInStockList(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize, InStockVO inStockVO);

    List<InStockItemVO> detail(Integer id, Integer pageNum);

    InStockVO getSupplierVO(Integer id);
}

