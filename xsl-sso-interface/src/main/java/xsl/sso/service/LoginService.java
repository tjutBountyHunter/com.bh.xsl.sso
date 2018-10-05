package xsl.sso.service;

import common.pojo.Result;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/21 15:33
 * @Description:
 */
public interface LoginService {

    Result getLoginResult(String username, String password);
}
