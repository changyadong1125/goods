package com.goods.config;

import com.goods.common.error.BusinessException;
import com.goods.common.error.SystemException;
import com.goods.common.response.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Configuration
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class MyMebMvcConfigurer implements WebMvcConfigurer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add((httpServletRequest, httpServletResponse, o, e) -> {
            ResponseBean result;
            HashMap<String, Object> errorData = new HashMap<>();
            logger.info("请求错误，url:{}", httpServletRequest.getRequestURL());
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                logger.info("业务模块-错误码：{},错误信息:{}", businessException.getErrorCode(), businessException.getErrorMsg());
                errorData.put("errorCode", businessException.getErrorCode());
                errorData.put("errorMsg", businessException.getErrorMsg());
            }else if(e instanceof SystemException){
                SystemException systemException = (SystemException) e;
                logger.info("系统模块-错误码：{},错误信息:{}", systemException.getErrorCode(), systemException.getErrorMsg());
                errorData.put("errorCode", systemException.getErrorCode());
                errorData.put("errorMsg", systemException.getErrorMsg());
            } else if(e instanceof UnauthorizedException){
                UnauthorizedException unauthorizedException = (UnauthorizedException) e;
                logger.info("系统模块-错误码：{},错误信息:{}", HttpStatus.UNAUTHORIZED.value(), unauthorizedException.getMessage());
                errorData.put("errorCode", HttpStatus.UNAUTHORIZED.value());
                errorData.put("errorMsg", "服务器向你抛了一个异常,并表示（操作无权限）");
            }else if(e instanceof HttpRequestMethodNotSupportedException){
                logger.info("系统模块-错误码：{},错误信息:{}", HttpStatus.BAD_REQUEST.value(), e.getMessage());
                errorData.put("errorCode", HttpStatus.BAD_REQUEST.value());
                errorData.put("errorMsg", "不支持该http请求方式");
            }else if(e instanceof NoHandlerFoundException){
                logger.error("接口不存在-错误码：{},错误信息:{}", HttpStatus.NOT_FOUND.value(),e.getMessage());
                errorData.put("errorCode", HttpStatus.NOT_FOUND.value());
                errorData.put("errorMsg", "API接口:["+httpServletRequest.getServletPath()+"]不存在");
            }  else {
                logger.error("系统异常-错误码：{},错误信息:{}", HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),e);
                errorData.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
                errorData.put("errorMsg", "服务器异常，请联系管理员");
            }
            result = ResponseBean.error(errorData);
            responseResult(httpServletResponse, result);
            return new ModelAndView();
        });
    }


    /**
     * <h2>响应结果</h2>
     *
     * @param response
     * @param result
     */
    private void responseResult(HttpServletResponse response, ResponseBean result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        try {
            ObjectMapper  objectMapper=new ObjectMapper();
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }


}
