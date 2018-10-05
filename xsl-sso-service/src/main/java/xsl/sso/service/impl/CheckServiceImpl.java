package xsl.sso.service.impl;

import cn.xsl.mapper.XslManagerMapper;
import cn.xsl.pojo.XslManager;
import cn.xsl.pojo.XslManagerExample;
import common.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sso.utils.JedisClient;
import sso.utils.JsonUtils;
import sso.utils.ResultUtils;
import xsl.sso.service.CheckService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/21 16:14
 * @Description:
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    private JedisClient jedisClient;
    @Resource
    private XslManagerMapper xslManagerMapper;
    /**token的前缀*/
    @Value("${XSL_MANAGER_INFO_KEY}")
    private String XSL_MANAGER_INFO_KEY;
    /**用户信息失效时间*/
    @Value("${LOGIN_EXPIRE_TIME}")
    private Integer LOGIN_EXPIRE_TIME;

    @Override
    public Result getCheckResult(String key) {
        /**
         *
         * 功能描述: 检查用户是否已登录，200-成功，403-用户状态不正确或未登录
         *
         * @param: [token]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/22 9:54
         */
        String json = jedisClient.get(XSL_MANAGER_INFO_KEY + key);
        if (StringUtils.isBlank(json)){
            return ResultUtils.setResult(404,"请登录");
        }
        //将缓存信息转换为pojo对象
        XslManager xslManager = JsonUtils.jsonToPojo(json, XslManager.class);

        if (checkManagerStatus(xslManager.getId()) != 0){
            return ResultUtils.setResult(500,"请登录");
        }
        //更新失效时间
        jedisClient.expire(XSL_MANAGER_INFO_KEY + key ,LOGIN_EXPIRE_TIME);
        Integer id = xslManager.getId();
        return ResultUtils.setResult(200,"已登录",id);
    }

    private int checkManagerStatus(int managerId){
        /**
         *
         * 功能描述: 根据用户名查询用户状态。0-正常 ，1-冻结或未查询到
         *
         * @param: [username]
         * @return: cn.xsl.pojo.XslManager
         * @auther: 11432_000
         * @date: 2018/9/21 21:41
         */
        XslManagerExample example = new XslManagerExample();
        XslManagerExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(managerId);
        List<XslManager> xslManagers = xslManagerMapper.selectByExample(example);
        if (xslManagers != null && xslManagers.size() > 0){
            return xslManagers.get(0).getStatus();
        }
        return 1;
    }
}
