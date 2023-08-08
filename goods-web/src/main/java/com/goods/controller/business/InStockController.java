package com.goods.controller.business;
import com.goods.business.service.InStockService;
import com.goods.common.model.business.Product;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;


/**
 * project:goods
 * package:com.goods.controller.business
 * class:InStockController
 *
 * @author: smile
 * @create: 2023/8/8-10:26
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/business/inStock")
public class InStockController {
    @Resource
    private InStockService inStockService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库单（入库记录）数据分页列表展示
     */
    @GetMapping("/findInStockList")
    public ResponseBean<?> findInStockList(@RequestParam Integer pageNum,
                                           @RequestParam Integer pageSize,
                                           InStockVO inStockVO) {
        PageVO<InStockVO> inStockList = inStockService.findInStockList(pageNum, pageSize, inStockVO);
        return ResponseBean.success(inStockList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:明细
     */
    @GetMapping("/detail/{id}")
    public ResponseBean<?> detail(@PathVariable Integer id, @RequestParam Integer pageNum) {
        HashMap<String, Object> detail = inStockService.detail(id, pageNum);
        return ResponseBean.success(detail);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加回收站
     */
    @GetMapping("remove/{id}")
    public ResponseBean<?> remove(@PathVariable Integer id) {
        PageVO<Product> products = inStockService.remove(id);
        return ResponseBean.success(products);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库
     */
    @PostMapping("/addIntoStock")
    public ResponseBean<?> addIntoStock(@RequestBody InStockVO inStockVO){
        inStockService.addIntoStock(inStockVO);
        return ResponseBean.success();
    }
}
