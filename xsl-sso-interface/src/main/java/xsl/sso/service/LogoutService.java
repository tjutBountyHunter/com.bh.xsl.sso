package xsl.sso.service;

import common.pojo.Result;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/22 13:39
 * @Description:
 */
public interface LogoutService {

    Result logoutByToken(String token);
}
