package com.goods.controller.business;

import com.goods.business.service.OutStockService;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.OutStock;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.business.OutStockVO;
import com.goods.common.vo.system.PageVO;
import org.omg.CORBA.INTERNAL;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * project:goods
 * package:com.goods.controller.business
 * class:OutStockController
 *
 * @author: smile
 * @create: 2023/8/8-20:48
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("business/outStock")
public class OutStockController {
    @Resource
    private OutStockService outStockService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:出库单（发放记录）数据分页列表展示
     */
    @GetMapping("/findOutStockList")
    public ResponseBean<?> findOutStockList(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam(required = false) String outNum,
                                            @RequestParam(required = false) Integer type) {
        PageVO<OutStockVO> pageVO = outStockService.findOutStockList(pageNum, pageSize, status, outNum, type);
        return ResponseBean.success(pageVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:明细
     */
    @GetMapping("detail/{id}")
    public ResponseBean<?> detail(@PathVariable Integer id,
                                  @RequestParam Integer pageNum) {
        Map<String, Object> map = outStockService.detail(id, pageNum);
        return ResponseBean.success(map);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:分发物资
     */
    @PostMapping("/addOutStock")
    public ResponseBean<?> addOutStock(@RequestBody OutStockVO outStockVO) {
        outStockService.addOutStock(outStockVO);
        return ResponseBean.success();
    }


    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过审核
     */
    @PutMapping("/publish/{id}")
    public ResponseBean<?> publish(@PathVariable Long id) throws BusinessException {
        outStockService.publish(id);
        return ResponseBean.success();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    @GetMapping ("/delete/{id}")
    public ResponseBean<?> delete(@PathVariable Long id) throws BusinessException {
        outStockService.delete(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收
     */
    @PutMapping("remove/{id}")
    public ResponseBean<?> remove(@PathVariable Long id) throws BusinessException {
        outStockService.remove(id);
        return ResponseBean.success();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收
     */
    @PutMapping("back/{id}")
    public ResponseBean<?> back(@PathVariable Long id) throws BusinessException {
        outStockService.back(id);
        return ResponseBean.success();
    }


}
