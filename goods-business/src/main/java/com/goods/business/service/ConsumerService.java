package com.goods.business.service;

import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;

import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service
 * class:ConsumerService
 *
 * @author: smile
 * @create: 2023/8/8-20:01
 * @Version: v1.0
 * @Description:
 */
public interface ConsumerService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资去处分页列表展示
     */
    PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, String name, String address, String contact);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物流去向
     */
    void add(Consumer consumer);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显
     */
    ConsumerVO edit(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑
     */
    void update(Long id, ConsumerVO consumer);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    void delete(Long id);

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:所有去向
     */
    List<ConsumerVO> findAll();
}
