package xsl.sso.service.impl;

import common.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sso.utils.JedisClient;
import sso.utils.ResultUtils;
import xsl.sso.service.LogoutService;

import javax.annotation.Resource;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/22 13:40
 * @Description:
 */
@Service
public class LogoutServiceImpl implements LogoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private JedisClient jedisClient;
    /**token的前缀*/
    @Value("${XSL_MANAGER_INFO_KEY}")
    private String XSL_MANAGER_INFO_KEY;

    @Override
    public Result logoutByToken(String managerInfoKey) {

        Long del = jedisClient.del(XSL_MANAGER_INFO_KEY + managerInfoKey);
        if (del == 0){
            return ResultUtils.setResult(200,"登录已失效");
        }
        return ResultUtils.setResult(200,"登出成功");
    }
}
