import org.springframework.util.DigestUtils;
import sso.utils.Md5Utils;

import java.io.UnsupportedEncodingException;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/10/5 11:03
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test(){
        String digest = null;
        try {
            digest = DigestUtils.md5DigestAsHex("123456".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(digest);
        String p = Md5Utils.digestMds("123456");
        System.out.println(p);
    }
}
