package com.goods.business.mapper;

import com.goods.common.model.business.Health;
import tk.mybatis.mapper.common.Mapper;

/**
 * project:goods
 * package:com.goods.business.mapper
 * class:HealthMapper
 *
 * @author: smile
 * @create: 2023/8/9-8:50
 * @Version: v1.0
 * @Description:
 */
public interface HealthMapper  extends Mapper<Health> {
    Health isReport();
}
