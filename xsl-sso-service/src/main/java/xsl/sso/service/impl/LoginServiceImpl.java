package xsl.sso.service.impl;

import cn.xsl.mapper.XslManagerMapper;
import cn.xsl.pojo.XslManager;
import cn.xsl.pojo.XslManagerExample;
import common.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import sso.utils.*;
import xsl.sso.service.LoginService;

import javax.annotation.Resource;
import java.util.*;

/**
 * 说明：登录服务
 *
 * @Auther: 11432_000
 * @Date: 2018/9/21 15:49
 * @Description:
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
//    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Resource
    private JedisClient jedisClient;
    @Resource
    private XslManagerMapper xslManagerMapper;
    /**key的前缀*/
    @Value("${XSL_MANAGER_INFO_KEY}")
    private String XSL_MANAGER_INFO_KEY;
    /**用户信息失效时间*/
    @Value("${LOGIN_EXPIRE_TIME}")
    private Integer LOGIN_EXPIRE_TIME;
    @Value("${PASSWORD_PREFIX}")
    private String PASSWORD_PREFIX;
    @Value("${PASSWORD_SUFFIX}")
    private String PASSWORD_SUFFIX;

    @Override
    public Result getLoginResult(String username, String password) {
        /**
         *
         * 功能描述: 登录验证，状态码：403-账号状态错误，404-用户名或密码错误，200-登录成功
         *
         * @param: [username, password]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/21 22:24
         */
        password = password.replace(PASSWORD_PREFIX,"");
        password = password.replaceAll(PASSWORD_SUFFIX,"");
        password = Md5Utils.digestMds(password);
        XslManager xslManager = getManagerInfo(username, password);
        if (username == null || password == null){
            LOGGER.info("尝试登录名："+ username + ",尝试登录密码：" + password);
            return ResultUtils.setResult(404,"用户名或密码错误！");
        }
        if (xslManager == null){
            LOGGER.info("尝试登录名："+ username + ",尝试登录密码：" + password);
            return ResultUtils.setResult(404,"用户名或密码错误！");
        }
        if (xslManager.getStatus() != 0){
            LOGGER.info("尝试登录名："+ username + ",尝试登录密码：" + password);
            return ResultUtils.setResult(500,"该账号已冻结！");
        }
        String key = UUID.randomUUID().toString();
        //清空密码
        xslManager.setManagerPassword(null);
        //将用户信息写入缓存。
        jedisClient.set(XSL_MANAGER_INFO_KEY + key , JsonUtils.objectToJson(xslManager));
        //设置失效时间
        jedisClient.expire(XSL_MANAGER_INFO_KEY + key ,LOGIN_EXPIRE_TIME);
        LOGGER.error("管理员：" + username + "登录成功");
        Map<String,String> map = new HashMap<String, String>();
        map.put("managerKey",key);
        map.put("id",xslManager.getId().toString());
        return ResultUtils.setResult(200,"登录成功", JsonUtils.objectToJson(map));
    }

    private XslManager getManagerInfo(String username, String password){
        /**
         *
         * 功能描述: 根据用户名和密码查找管理员信息，查找失败返回null。
         *
         * @param: [username, password]
         * @return: cn.xsl.pojo.XslManager
         * @auther: 11432_000
         * @date: 2018/9/21 21:41
         */
        XslManagerExample example = new XslManagerExample();
        XslManagerExample.Criteria criteria = example.createCriteria();
        criteria.andManagerNameEqualTo(username);
        criteria.andManagerPasswordEqualTo(password);
        List<XslManager> xslManagers = xslManagerMapper.selectByExample(example);
        if (xslManagers != null && xslManagers.size() > 0){
            xslManagers.get(0).setLastLoginDate(new Date());
            xslManagerMapper.updateByPrimaryKeySelective(xslManagers.get(0));
            return xslManagers.get(0);
        }
        return null;
    }


}
