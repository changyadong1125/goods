package com.goods.business.service;
import com.goods.common.model.business.Health;
import com.goods.common.vo.business.HealthVO;
import com.goods.common.vo.system.PageVO;

/**
 * project:goods
 * package:com.goods.business.service
 * class:HealthService
 *
 * @author: smile
 * @create: 2023/8/9-8:49
 * @Version: v1.0
 * @Description:
 */
public interface HealthService {
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:是否打卡
     */
    HealthVO isReport(String username);
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:打卡
     */
    void report(Health health, String username);
    /**
     * return:
     * author: smile
     * version: 1.0
     * description:历史打卡
     */
    PageVO<Health>  history(Integer pageNum, Integer pageSize);
}
