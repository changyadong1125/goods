package com.goods.system.service;

import com.goods.common.error.SystemException;
import com.goods.common.model.system.Log;
import com.goods.common.vo.system.LogVO;
import com.goods.common.vo.system.PageVO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * 系统日志
 */
public interface LogService  {

    /**
     * 异步保存操作日志
     */
    @Async("CodeAsyncThreadPool")
    void saveLog(Log log);


    /**
     * 删除登入日志
     * @param id
     */
    void delete(Long id) throws SystemException;


    /**
     * 登入日志列表
     * @param pageNum
     * @param pageSize
     * @param logVO
     * @return
     */
    PageVO<LogVO> findLogList(Integer pageNum, Integer pageSize, LogVO logVO);

    /**
     * 批量删除登入日志
     * @param list
     */
    void batchDelete(List<Long> list) throws SystemException;
}
