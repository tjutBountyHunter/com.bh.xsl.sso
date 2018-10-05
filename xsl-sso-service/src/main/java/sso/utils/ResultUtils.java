package sso.utils;

import common.pojo.Result;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/21 22:33
 * @Description:
 */
public class ResultUtils {

    public static Result setResult(int code , String msg , Object data){
        /**
         *
         * 功能描述: 设置返回的状态码，状态信息和数据。
         *
         * @param: [code, msg, data]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/21 22:04
         */
        Result result = new Result();
        result.setInfo(msg);
        result.setStatus(code);
        result.setData(data);
        return result;
    }


    public static Result setResult(int code ,Object data){
        /**
         *
         * 功能描述: 设置返回的状态码和数据
         *
         * @param: [code, msg]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/21 22:06
         */
        Result result = new Result();
        result.setData(data);
        result.setStatus(code);
        return result;
    }
}
