package com.goods.system.aspect;

import com.alibaba.fastjson.JSON;
import com.goods.common.annotation.ControllerEndpoint;
import com.goods.common.model.system.Log;
import com.goods.common.response.ActiveUser;
import com.goods.common.utils.AddressUtil;
import com.goods.common.utils.IPUtil;
import com.goods.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * 系统日志切面
 */
@Slf4j
@Aspect
@Component
public class ControllerEndpointAspect extends AspectSupport {

    private Log sysLog=new Log();

    private long startTime;


    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.goods.common.annotation.ControllerEndpoint)")
    public void pointcut() {
    }

    /**
     * 环绕通知
     * @param joinPoint
     */
    @Around("pointcut()")
    public Object saveSysLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result=null;
        //开始时间
        startTime=System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取注解
        ControllerEndpoint controllerEndpoint = method.getAnnotation(ControllerEndpoint.class);
        if(controllerEndpoint!=null){
            String operation = controllerEndpoint.operation();
            //注解上的操作描述
            sysLog.setOperation(operation);
        }

        //请求的参数
        Object[] args = joinPoint.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        sysLog.setParams("paramName:"+ Arrays.toString(paramNames) +",args:"+ Arrays.toString(args));

        //请求的IP
        HttpServletRequest request =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddr = IPUtil.getIpAddr(request);
        sysLog.setIp(ipAddr);
        //地理位置
        sysLog.setLocation(AddressUtil.getCityInfo(ipAddr));
        //操作人
        ActiveUser activeUser= (ActiveUser) SecurityUtils.getSubject().getPrincipal();
        sysLog.setUsername(activeUser.getUser().getUsername());
        //添加时间
        sysLog.setCreateTime(new Date());
        //执行目标方法
        result=joinPoint.proceed();
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()\n"
                +"\nresponse:"+postHandle(result));
        //执行耗时
        sysLog.setTime(System.currentTimeMillis()-startTime);
        //保存系统日志
        logService.saveLog(sysLog);

        return result;
    }

    /**
     * 返回数据
     * @param retVal
     * @return
     */
    private String postHandle(Object retVal) {
        if(null == retVal){
            return "";
        }
        return JSON.toJSONString(retVal);
    }
}



