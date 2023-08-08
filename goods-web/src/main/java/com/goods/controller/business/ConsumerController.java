package com.goods.controller.business;

import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * project:goods
 * package:com.goods.controller.business
 * class:ConsumerController
 *
 * @author: smile
 * @create: 2023/8/8-20:00
 * @Version: v1.0
 * @Description:
 */
@RestController
@RequestMapping("/business/consumer")
public class ConsumerController {
    @Resource
    private ConsumerService consumerService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资去处分页列表展示
     */
    @GetMapping("/findConsumerList")
    public ResponseBean<?> findConsumerList(@RequestParam Integer pageNum,
                                            @RequestParam Integer pageSize,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) String address,
                                            @RequestParam(required = false) String contact) {
        PageVO<ConsumerVO> consumerVOPageVO = consumerService.findConsumerList(pageNum, pageSize, name, address, contact);
        return ResponseBean.success(consumerVOPageVO);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物资去向
     */
    @PostMapping("/add")
    public ResponseBean<?> add(@RequestBody Consumer consumer) {
        consumerService.add(consumer);
        return ResponseBean.success();
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显
     */
    @GetMapping("/edit/{id}")
    public ResponseBean<?> edit(@PathVariable Long id) {
        ConsumerVO consumerVO = consumerService.edit(id);
        return ResponseBean.success(consumerVO);
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:修改
     */
    @PutMapping("/update/{id}")
    public ResponseBean<?> update(@PathVariable Long id,
                                  @RequestBody ConsumerVO consumerVO){
        consumerService.update(id,consumerVO);
        return ResponseBean.success();
    }
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    @DeleteMapping("delete/{id}")
    public ResponseBean<?> delete(@PathVariable Long id){
        consumerService.delete(id);
        return ResponseBean.success();
    }
}
