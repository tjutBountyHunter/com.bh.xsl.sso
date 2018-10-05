package common.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/24 17:14
 * @Description:
 */
public class JWTpojo implements Serializable {

    /** iss: jwt签发者*/
    private String iss ;
    /** sub: jwt所面向的用户*/
    private String sub;
    /** aud: 接收jwt的一方*/
    private String aud;
    /** exp: jwt的过期时间，这个过期时间必须要大于签发时间*/
    private Date exp;
    /** nbf: 定义在什么时间之前，该jwt都是不可用的.*/
    private Date nbf;
    /** iat: jwt的签发时间*/
    private Date iat;
    /** jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。*/
    private String jti;
    /** 扩展参数*/
    private Map<String , Object> extend;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public Date getNbf() {
        return nbf;
    }

    public void setNbf(Date nbf) {
        this.nbf = nbf;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }
}
