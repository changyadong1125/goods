package com.goods.system.service.impl;


import com.goods.common.error.SystemCodeEnum;
import com.goods.common.error.SystemException;
import com.goods.common.model.system.LoginLog;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.AddressUtil;
import com.goods.common.utils.IPUtil;
import com.goods.common.vo.system.LoginLogVO;
import com.goods.common.vo.system.PageVO;
import com.goods.common.vo.system.UserVO;
import com.goods.system.mapper.LoginLogMapper;
import com.goods.system.service.LoginLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 登入日志列表
     * @param pageNum
     * @param pageSize
     * @param loginLogVO
     * @return
     */
    @Override
    public PageVO<LoginLogVO> findLoginLogList(Integer pageNum, Integer pageSize, LoginLogVO loginLogVO) {
        PageHelper.startPage(pageNum,pageSize);
        Example o = new Example(LoginLog.class);
        Example.Criteria criteria = o.createCriteria();
        o.setOrderByClause("login_time desc");
        if(loginLogVO.getIp()!=null&&!"".equals(loginLogVO.getIp())){
            criteria.andLike("ip","%"+loginLogVO.getIp()+"%");
        }
        if(loginLogVO.getLocation()!=null&&!"".equals(loginLogVO.getLocation())){
            criteria.andLike("location","%"+loginLogVO.getLocation()+"%");
        }
        if(loginLogVO.getUsername()!=null&&!"".equals(loginLogVO.getUsername())){
            criteria.andLike("username","%"+loginLogVO.getUsername()+"%");
        }
        List<LoginLog> loginLogs = loginLogMapper.selectByExample(o);
        List<LoginLogVO> loginLogVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(loginLogs)){
            for (LoginLog loginLog : loginLogs) {
                LoginLogVO logVO = new LoginLogVO();
                BeanUtils.copyProperties(loginLog,logVO);
                loginLogVOS.add(logVO);
            }
        }
        PageInfo<LoginLog> info=new PageInfo<>(loginLogs);
        return new PageVO<>(info.getTotal(),loginLogVOS);
    }

    /**
     * 批量删除日志
     * @param list
     */
    @Override
    public void batchDelete(List<Long> list) throws SystemException {
        for (Long id : list) {
            LoginLog loginLog = loginLogMapper.selectByPrimaryKey(id);
            if(loginLog==null){
                throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"id="+id+"登入日志不存在");
            }
            delete(id);
        }
    }

    /**
     * 登入报表
     * @param userVO
     * @return
     */
    @Override
    public List<Map<String, Object>> loginReport(UserVO userVO) {
        return loginLogMapper.userLoginReport(userVO);
    }

    /**
     * 插入登入日志
     * @param request
     */
    @Transactional
    @Override
    public void add(HttpServletRequest request) {
        loginLogMapper.insert(createLoginLog(request));
    }

    /**
     * 创建登入日志
     * @param
     * @return
     */
    public static LoginLog createLoginLog(HttpServletRequest request) {
        ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(activeUser.getUser().getUsername());
        loginLog.setIp(IPUtil.getIpAddr(request));
        loginLog.setLocation(AddressUtil.getCityInfo(IPUtil.getIpAddr(request)));
        // 获取客户端操作系统
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        OperatingSystem os = userAgent.getOperatingSystem();
        loginLog.setUserSystem(os.getName());
        loginLog.setUserBrowser(browser.getName());
        loginLog.setLoginTime(new Date());
        return loginLog;
    }

    /**
     * 删除登入日志
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) throws SystemException {
        LoginLog loginLog = loginLogMapper.selectByPrimaryKey(id);
        if(loginLog==null){
            throw new SystemException(SystemCodeEnum.PARAMETER_ERROR,"登入日志不存在");
        }
        loginLogMapper.deleteByPrimaryKey(id);
    }

}
