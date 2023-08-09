package com.goods.business.service.imp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ConsumerMapper;
import com.goods.business.service.ConsumerService;
import com.goods.common.model.business.Consumer;
import com.goods.common.vo.business.ConsumerVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:ConsumerServiceImp
 *
 * @author: smile
 * @create: 2023/8/8-20:02
 * @Version: v1.0
 * @Description:
 */
@Service
public class ConsumerServiceImp implements ConsumerService {
    @Resource
    private ConsumerMapper consumerMapper;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:物资去处分页列表展示
     */
    @Override
    public PageVO<ConsumerVO> findConsumerList(Integer pageNum, Integer pageSize, String name, String address, String contact) {
        PageHelper.startPage(pageNum, pageSize);
        Example E = new Example(Consumer.class);
        Example.Criteria C = E.createCriteria();
        if (!StringUtils.isEmpty(name)) C.andLike("name", "%" + name + "%");
        if (!StringUtils.isEmpty(address)) C.andLike("address", "%" + address + "%");
        if (!StringUtils.isEmpty(contact)) C.andLike("contact", "%" + contact + "%");
        List<Consumer> consumers = consumerMapper.selectByExample(E);
        List<ConsumerVO> consumerVOS = consumers.stream().map(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            return consumerVO;
        }).collect(Collectors.toList());
        PageInfo<Consumer> info = new PageInfo<>(consumers);
        return new PageVO<>(info.getTotal(), consumerVOS);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:添加物流去向
     */
    @Override
    public void add(Consumer consumer) {
        consumer.setCreateTime(new Date());
        consumer.setModifiedTime(new Date());
        consumerMapper.insert(consumer);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:回显
     */
    @Override
    public ConsumerVO edit(Long id) {
        Consumer consumer = consumerMapper.selectByPrimaryKey(id);
        ConsumerVO consumerVO = new ConsumerVO();
        BeanUtils.copyProperties(consumer, consumerVO);
        return consumerVO;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:编辑
     */
    @Override
    public void update(Long id, ConsumerVO consumerVO) {
        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(consumerVO, consumer);
        consumer.setModifiedTime(new Date());
        consumer.setId(id);
        consumerMapper.updateByPrimaryKey(consumer);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:删除
     */
    @Override
    public void delete(Long id) {
        consumerMapper.deleteByPrimaryKey(id);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:所有去向
     */
    @Override
    public List<ConsumerVO> findAll() {
        return consumerMapper.selectAll().stream().map(consumer -> {
            ConsumerVO consumerVO = new ConsumerVO();
            BeanUtils.copyProperties(consumer, consumerVO);
            return consumerVO;
        }).collect(Collectors.toList());
    }
}
