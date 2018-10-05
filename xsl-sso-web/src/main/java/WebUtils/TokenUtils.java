package WebUtils;

import common.pojo.JWTpojo;
import common.pojo.Result;
import xsl.sso.service.JwtService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/25 18:20
 * @Description:
 */
public class TokenUtils {

    @Resource
    private JwtService jwtService;
    /**成功状态码*/
    private static final int SUCCESS = 200;

    public String checkOrLoginSuccess(String managerInfoKey){
        /**
         *
         * 功能描述: 生成token，并将用户信息的key存入token，然后将该token存入Redis，返回tokenKey
         *
         * @param: [managerInfoKey]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/9/25 18:12
         */
        JWTpojo tpojo = new JWTpojo();
        Map<String,Object> map = new HashMap<String,Object>(4);
        map.put("managerInfoKey",managerInfoKey);
        tpojo.setExtend(map);
        Result tokenResult = jwtService.getJWTToken(tpojo);
        if (tokenResult.getStatus() == SUCCESS){
            String token = tokenResult.getData().toString();
            /* 获取tokenKey */
            Result setTokenInRedis = jwtService.setTokenInRedis(token);
            String tokenKey = setTokenInRedis.getData().toString();
            return tokenKey;
        }
        return "";
    }
}
