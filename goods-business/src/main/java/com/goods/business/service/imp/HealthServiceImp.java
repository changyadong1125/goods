package com.goods.business.service.imp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.HealthMapper;
import com.goods.business.service.HealthService;
import com.goods.common.model.business.Health;
import com.goods.common.model.system.User;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;
import com.goods.system.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * project:goods
 * package:com.goods.business.service.imp
 * class:HealthServiceImp
 *
 * @author: smile
 * @create: 2023/8/9-8:49
 * @Version: v1.0
 * @Description:
 */
@Service
public class HealthServiceImp implements HealthService {
    @Resource
    private HealthMapper healthMapper;
    @Resource
    private UserService userService;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:是否打卡
     */
    @Override
    public HealthVO isReport() {
        Health health = healthMapper.isReport();
        HealthVO healthVO = new HealthVO();
        if (null != health) {
            BeanUtils.copyProperties(health, healthVO);
            return healthVO;
        }
        return null;
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:打卡
     */
    @Override
    public void report(Health health, String username) {
        health.setCreateTime(new Date());
        User admin = userService.findUserByName(username);
        health.setUserId(admin.getId());
        healthMapper.insert(health);
    }

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:历史打卡
     */
    @Override
    public PageVO<Health> history(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Health> health = healthMapper.selectAll();
        PageInfo<Health> healthPageInfo = new PageInfo<>(health);
        return new PageVO<>(healthPageInfo.getTotal(), health);
    }
}
