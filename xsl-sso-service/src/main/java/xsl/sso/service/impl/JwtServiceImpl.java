package xsl.sso.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import common.pojo.JWTpojo;
import common.pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sso.utils.JedisClient;
import sso.utils.JsonUtils;
import sso.utils.ResultUtils;
import xsl.sso.service.JwtService;

import javax.annotation.Resource;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/24 13:28
 * @Description:
 */
@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    @Resource
    private JedisClient jedisClient;

    /** jwt 密匙，不要轻易更改*/
    private static final String SECRET = "jfiskaiifdhgailbghailfgahgfkdyvd";
    /** token有效期*/
    private static final int CALENDAR_UTIL = Calendar.MINUTE;
    private static final int TERM_OF_VALIDITY = 15;
    /** header和payload编码格式*/
    private static final JWSAlgorithm JWT_CODE = JWSAlgorithm.HS256;
    /** 网络时间获取地址*/
    private static final String URL_BY_GET_TIME = "http://www.baidu.com";
    private static final int SUCCESS = 100;
    /** token key*/
    @Value("${TOKEN_KEY_PREFIX}")
    private String TOKEN_KEY_PREFIX;
    /** token失效时间*/
    @Value("${TOKEN_EXPIRE_TIME}")
    private int TOKEN_EXPIRE_TIME;

    @Override
    public Result getJWTToken(JWTpojo jwTpojo) {
        /**
         *
         * 功能描述: 获取验证token，返回码：200-成功，500-失败
         *
         * @param: [payloadMap]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/9/24 15:16
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(CALENDAR_UTIL,TERM_OF_VALIDITY);
        Date date = calendar.getTime();
        jwTpojo.setExp(date);
        Result result = new Result();
        //JWT 头部
        JWSHeader header = new JWSHeader(JWT_CODE);
        //JWT payload 有效载荷
        Payload payload = new Payload(JsonUtils.objectToJson(jwTpojo));
        //拼接头部和有效载荷
        JWSObject jwsObject = new JWSObject(header , payload);
        //生成sign
        try {
            JWSSigner jwsSigner = new MACSigner(SECRET);
            //添加sign
            jwsObject.sign(jwsSigner);
            return ResultUtils.setResult(200,"成功",jwsObject.serialize());
        }catch (Exception e){
            LOGGER.error("JWT 签名生成错误");
            return ResultUtils.setResult(500,"签名生成失败");
        }
    }

    @Override
    public boolean checkJWTSign(String token) {
        /**
         *
         * 功能描述: 校验token是否有效，有效返回true，无效返回false
         *
         * @param: [token]
         * @return: boolean
         * @auther: 11432_000
         * @date: 2018/9/24 18:03
         */
        boolean flag = true;
        Result payload = getPayloadByToken(token);
        if (payload.getStatus() != SUCCESS){
            flag = false;
        }
        if (flag){
            JWTpojo data =(JWTpojo) payload.getData();
            long exp = Long.valueOf(data.getExp().getTime());
            long now = Long.valueOf(getInternetTime().getTime());
            if (now > exp){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public Result getPayloadByToken(String token) {
        /**
         *
         * 功能描述: 根据token取token中的数据，返回值：200-成功，404-提取失败
         *
         * @param: [token]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/24 16:44
         */
        try {
            //反序列化
            JWSObject jwsObject = JWSObject.parse(token);
            //获取有效载荷
            Payload payload = jwsObject.getPayload();
            //获取校验签名
            JWSVerifier verifier = new MACVerifier(SECRET);
            if (!jwsObject.verify(verifier)){
                return ResultUtils.setResult(404,"签名校验失败");
            }
            //返回值
            JWTpojo jwTpojo = JsonUtils.jsonToPojo(payload.toString(), JWTpojo.class);
            return ResultUtils.setResult(200,jwTpojo);
        }catch (Exception e){
            LOGGER.error("提取信息失败");
            return ResultUtils.setResult(404,"提取信息失败");
        }
    }

    @Override
    public Result setTokenInRedis(String token) {
        /**
         *
         * 功能描述: 将token存入Redis，并返回tokenKey。
         *
         * @param: [token]
         * @return: pojo.Result
         * @auther: 11432_000
         * @date: 2018/9/25 17:48
         */
        String uuid = UUID.randomUUID().toString();
        String tokenKey = TOKEN_KEY_PREFIX + uuid;
        //将token存入缓存
        jedisClient.set(tokenKey , token);
        //设置失效时间
        jedisClient.expire(tokenKey , TOKEN_EXPIRE_TIME);
        return ResultUtils.setResult(200,"成功",uuid);
    }

    public Date getInternetTime(){
        /**
         *
         * 功能描述: 获取网络时间，失败返回本地时间。
         *
         * @param: []
         * @return: java.util.Date
         * @auther: 11432_000
         * @date: 2018/9/24 17:47
         */
        try {
            URL url = new URL(URL_BY_GET_TIME);
            //获取链接
            URLConnection urlConnection = url.openConnection();
            //连接
            urlConnection.connect();
            long date = urlConnection.getDate();
            return new Date(date);
        }catch (Exception e){

        }
        return new Date();
    }
}
