package com.goods.system.mapper;


import com.goods.common.model.system.LoginLog;
import com.goods.common.vo.system.UserVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface LoginLogMapper extends Mapper<LoginLog> {

    /**
     * 用户登入报表
     * @param userVO
     * @return
     */
    List<Map<String,Object>> userLoginReport(UserVO userVO);

}
