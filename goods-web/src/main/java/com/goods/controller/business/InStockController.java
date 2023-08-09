package com.goods.controller.business;
import com.goods.business.service.InStockService;
import com.goods.common.error.BusinessException;
import com.goods.common.response.ResponseBean;
import com.goods.common.utils.JWTUtils;
import com.goods.common.vo.business.InStockVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @PutMapping("remove/{id}")
    public ResponseBean<?> remove(@PathVariable Long id) throws BusinessException {
        inStockService.remove(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:入库
     */
    @PostMapping("/addIntoStock")
    public ResponseBean<?> addIntoStock(@RequestBody InStockVO inStockVO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = JWTUtils.getUsername(token);
        inStockService.addIntoStock(inStockVO,username);
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
        inStockService.publish(id);
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
        inStockService.delete(id);
        return ResponseBean.success();
    }
}
