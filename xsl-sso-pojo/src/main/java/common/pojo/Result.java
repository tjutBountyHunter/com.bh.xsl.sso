package common.pojo;

import java.io.Serializable;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/9/21 15:35
 * @Description:
 */
public class Result implements Serializable {

    //返回的数据
    private Object data;
    //状态码
    private Integer status;
    //状态信息
    private String info;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
