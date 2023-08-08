package com.goods.controller.business;

import com.goods.business.service.SupplierService;
import com.goods.common.model.business.Supplier;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.SupplierVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * project:goods
 * package:com.goods.controller.business
 * class:SupplierController
 *
 * @author: smile
 * @create: 2023/8/7-15:45
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/business/supplier")
public class SupplierController {
    @Resource
    private SupplierService supplierService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:根据条件查询物资来源
     */
    @GetMapping("/findSupplierList")
    public ResponseBean<PageVO<Supplier>> findSupplierList(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize,
                                            SupplierVO supplierVO) {
        PageVO<Supplier> page = supplierService.findSupplierList(pageNum, pageSize, supplierVO);
        return ResponseBean.success(page);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资来源
     */
    @PostMapping("/add")
    public ResponseBean<?> addSupplier(@RequestBody Supplier supplier) {
        supplierService.addSupplier(supplier);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显物资来源
     */
    @GetMapping("/edit/{id}")
    public ResponseBean<?> edit(@PathVariable Long id) {
        Supplier supplier = supplierService.findSupplierById(id);
        return ResponseBean.success(supplier);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改物资来源
     */
    @PutMapping("/update/{id}")
    public ResponseBean<?> update(@PathVariable Long id,
                                  @RequestBody Supplier supplier) {
        supplierService.update(id, supplier);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除物资来源
     */
    @DeleteMapping("/delete/{id}")
    public ResponseBean<?> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return ResponseBean.success();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:查询所有来源
     */
    @GetMapping("findAll")
    public ResponseBean<?> findAll(){
        List<Supplier> supplierList =  supplierService.findAll();
        return ResponseBean.success(supplierList);
    }

}
