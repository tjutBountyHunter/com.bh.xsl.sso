package cn.xsl.sso.controller;

import WebUtils.CookieUtils;
import WebUtils.TokenUtils;
import common.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xsl.sso.service.CheckService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/22 10:37
 * @Description:
 */
@Controller
public class CheckController {

    @Resource
    private CheckService checkService;
    @Resource
    private TokenUtils tokenUtils;
    @Value("${LOGIN_URL}")
    private String LOGIN_URL;
    /**存储token的key*/
    @Value("${COOKIE_ID}")
    private String COOKIE_ID;
    /**成功状态码*/
    private static final int SUCCESS = 200;


    @RequestMapping("/manager/check")
    public String login(HttpServletRequest request ,HttpServletResponse response){
        /**
         *
         * 功能描述: 拦截器重定向页面，检查用户是否登录，已登录，返回要访问的页面，未登录，清空cookie，跳转至登录界面
         *
         * @param: [request]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/9/22 9:57
         */
        String returnUrl = request.getParameter("returnUrl");
        String id = request.getParameter("id");
        if (StringUtils.isNotBlank(id)){
            String cookieId = COOKIE_ID + id;
            String cookieValue = CookieUtils.getCookieValue(request, cookieId);
            if (StringUtils.isNotBlank(cookieValue)){
                Result checkResult = checkService.getCheckResult(cookieValue);
                //未登录
                if (checkResult.getStatus() != SUCCESS){
                    CookieUtils.setCookie(request , response , cookieId ,"" , 0);
                    return "redirect:"+ LOGIN_URL + "?returnUrl=" + returnUrl;
                }
                //已登录
                if (checkResult.getStatus() == SUCCESS){
                    /* 获取登录信息key */
                    String tokenKey = tokenUtils.checkOrLoginSuccess(cookieValue);
                    return "redirect:" + returnUrl + "?tokenKey=" + tokenKey;
                }
            }
        }
        return "redirect:"+ LOGIN_URL + "?returnUrl=" + returnUrl;
    }

}
