package com.goods.controller.business;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.error.BusinessException;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.system.PageVO;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * project:goods
 * package:com.goods.controller.business
 * class:BusinessCategoryController
 *
 * @author: smile
 * @create: 2023/8/7-10:03
 * @Version: v1.0
 * @Description:
 */
@RestController
@Api("物资类别接口")
@RequestMapping("/business/productCategory")
public class ProductCategoryController {
    @Resource
    private ProductCategoryService productCategoryService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资类别树形分页展示
     */
    @GetMapping("/categoryTree")
    public ResponseBean<PageVO<ProductCategoryTreeNodeVO>> categoryTree(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageVO<ProductCategoryTreeNodeVO> pageVO;
        if (StringUtils.isEmpty(pageNum) && StringUtils.isEmpty(pageSize)) {
            pageVO = productCategoryService.getCategoryTree();
        } else {
            pageVO = productCategoryService.getCategoryTree(pageNum, pageSize);
        }
        return ResponseBean.success(pageVO);

    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:获取父级分类列表
     */
    @GetMapping("/getParentCategoryTree")
    public ResponseBean<List<ProductCategoryTreeNodeVO>> getParentCategoryTree() {
        List<ProductCategoryTreeNodeVO> productCategoryTreeNodeVOList = productCategoryService.getParentCategoryTree();
        return ResponseBean.success(productCategoryTreeNodeVOList);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加分类
     */
    @PostMapping("/add")
    public ResponseBean<?> addCategory(@RequestBody ProductCategory productCategory) {
        Integer integer = productCategoryService.addCategory(productCategory);
        if (integer > 0) return ResponseBean.success();
        else return ResponseBean.error("添加失败");
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑分类回显
     */
    @GetMapping("/edit/{id}")
    public ResponseBean<?> edit(@PathVariable Long id) {
        ProductCategory productCategory = productCategoryService.findProductCategoryById(id);
        return ResponseBean.success(productCategory);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改分类信息
     */
    @PutMapping("/update/{id}")
    public ResponseBean<?> update(@PathVariable Long id,
                                  @RequestBody ProductCategory productCategory) {
        productCategoryService.update(productCategory);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除分类
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean<?> delete(@PathVariable Long id) throws BusinessException {
        return productCategoryService.delete(id);
    }
}
