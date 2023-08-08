package com.goods.controller.business;

import com.goods.business.service.ProductService;
import com.goods.common.error.BusinessCodeEnum;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.Product;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductStockVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.awt.print.PrinterGraphics;
import java.util.List;

/**
 * project:goods
 * package:com.goods.controller.business.service
 * class:ProductController
 *
 * @author: smile
 * @create: 2023/8/7-18:53
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/business/product")
public class ProductController {
    @Resource
    private ProductService productService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资列表分页展示
     */
    @GetMapping("/findProductList")
    public ResponseBean<PageVO<ProductVO>> findProductList(@RequestParam Integer pageNum,
                                                           @RequestParam Integer pageSize,
                                                           @RequestParam(required = false) String categorys,
                                                           ProductVO productVO) {
        PageVO<ProductVO> pageVO = productService.findProductList(pageNum, pageSize, categorys, productVO);
        return ResponseBean.success(pageVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资资料
     */
    @PostMapping("/add")
    public ResponseBean<?> add(@RequestBody ProductVO productVO) throws BusinessException {
        productService.add(productVO);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资资料
     */
    @GetMapping("/edit/{id}")
    public ResponseBean<?> edit(@PathVariable Long id) {
        ProductVO productVO = productService.getProductById(id);
        return ResponseBean.success(productVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑物资资料
     */
    @PutMapping("update/{id}")
    public ResponseBean<?> update(@PathVariable Long id,
                                  @RequestBody ProductVO productVO) throws BusinessException {
        productService.update(id, productVO);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资回收站
     */
    @PutMapping("remove/{id}")
    public ResponseBean<?> remove(@PathVariable Long id) throws BusinessException {
        productService.remove(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回收站删除
     */
    @DeleteMapping("delete/{id}")
    public ResponseBean<?> delete(@PathVariable Long id) throws BusinessException {
        productService.delete(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:恢复
     */
    @PutMapping("back/{id}")
    public ResponseBean<?> back(@PathVariable Long id) throws BusinessException {
        productService.back(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:通过审核
     */
    @PutMapping("publish/{id}")
    public ResponseBean<?> publish(@PathVariable Long id) throws BusinessException {
        productService.publish(id);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有商品
     */
    @GetMapping("/findProducts")
    public ResponseBean<?> findProducts(@RequestParam Integer pageNum,
                                        @RequestParam Integer pageSize,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) String categorys,
                                        @RequestParam(required = false) String name) {
        PageVO<Product> products = productService.findProducts(pageNum, pageSize, status, categorys, name);
        return ResponseBean.success(products);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description: 获取物资库存
     */
    @GetMapping("/findProductStocks")
    public ResponseBean<?> findProductStocks(@RequestParam Integer pageSize,
                                             @RequestParam Integer pageNum,
                                             @RequestParam(required = false) String categorys,
                                             @RequestParam(required = false) String name) {
        PageVO<ProductStockVO> pageVO = productService.findProductStocks(pageSize, pageNum,categorys,name);
        return ResponseBean.success(pageVO);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取所有库存
     */
    @GetMapping("/findAllStocks")
    public ResponseBean<?> findAllStocks(@RequestParam Integer pageSize,
                                             @RequestParam Integer pageNum,
                                         @RequestParam(required = false) String categorys,
                                         @RequestParam(required = false) String name) {
        List<ProductStockVO> allStocks = productService.findAllStocks(pageSize, pageNum,categorys,name);
        return ResponseBean.success(allStocks);
    }

}
