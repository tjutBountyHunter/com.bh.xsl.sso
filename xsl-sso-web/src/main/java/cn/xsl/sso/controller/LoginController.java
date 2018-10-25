package cn.xsl.sso.controller;

import WebUtils.CookieUtils;
import WebUtils.JsonUtils;
import WebUtils.TokenUtils;
import common.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xsl.sso.service.LoginService;
import xsl.sso.service.LogoutService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/22 09:12
 * @Description:
 */
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private LoginService loginService;
    @Resource
    private LogoutService logoutService;
    @Resource
    private TokenUtils tokenUtils;
    /**存储token的key*/
    @Value("${COOKIE_ID}")
    private String COOKIE_ID;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @Value("${STATU}")
    private String STATU;
    @Value("${RETURN_URL}")
    private String RETURN_URL;
    @Value("${TOKEN_EXPIRE_TIME}")
    private int TOKEN_EXPIRE_TIME;

    /**成功状态码*/
    private static final int SUCCESS = 200;


    @RequestMapping("/login")
    public String index(HttpServletRequest request){
        /**
         *
         * 功能描述: 跳转登录界面
         *
         * @param: [request]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/9/22 10:21
         *
         */
        getUrl(request);
        request.setAttribute("returnUrl",request.getParameter("returnUrl"));
        return "index";
    }

    @RequestMapping("/manager/login")
    @ResponseBody
    public String managerLogin(HttpServletRequest request, HttpServletResponse response){
        /**
         *
         * 功能描述: 登录验证，若登录成功，跳转至登录前想要访问的页面，返回tokenKey。
         *
         * @param: [request, response]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/9/22 13:26
         */
        Map map = new HashMap<String, Integer>(4);
        String username = request.getParameter("username");
        String passwd = request.getParameter("passwd");
        String returnUrl = request.getParameter("returnUrl");
        Result loginResult = loginService.getLoginResult(username, passwd);
        if (loginResult.getStatus() == SUCCESS){
            Map<String,String> json = JsonUtils.jsonToPojo(loginResult.getData().toString(), Map.class);
            String cookieId = COOKIE_ID + json.get("id");
            logout(request , response, cookieId);
            String tokenKey = tokenUtils.checkOrLoginSuccess(json.get("managerKey"));
            map.put(STATU,loginResult.getStatus());
            map.put(RETURN_URL,returnUrl);
            setCookie(response , TOKEN_KEY ,tokenKey);
            setCookie(response ,cookieId ,json.get("managerKey"));
            return JsonUtils.objectToJson(map);
        }
        map.put("statu",loginResult.getStatus());
        return JsonUtils.objectToJson(map);
    }

    public Result logout(HttpServletRequest request , HttpServletResponse response ,String cookieId){
        /**
         *
         * 功能描述: 登出功能
         *
         * @param: [request, response]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/10/2 10:45
         */
        Result result = new Result();
        result.setStatus(200);
        result.setInfo("登出成功");
        logoutService.logoutByToken(CookieUtils.getCookieValue(request,cookieId));
        CookieUtils.setCookie(request , response , cookieId ,"" , 0);
        return result;
    }

    private void setCookie(HttpServletResponse response ,String name , String value){
        Cookie cookie = new Cookie(name , value);
        cookie.setDomain("47.93.230.61");
        cookie.setPath("/");
        cookie.setMaxAge(TOKEN_EXPIRE_TIME);
        response.addCookie(cookie);
    }


    private void getUrl(HttpServletRequest request){
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
        request.setAttribute("basePath",basePath);
    }
}
