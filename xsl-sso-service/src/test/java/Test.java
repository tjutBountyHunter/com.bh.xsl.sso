import org.springframework.util.DigestUtils;
import sso.utils.Md5Utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String digest = "2017-09-18 19:11:55.5";
        String format = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(digest);
            System.out.println(date.getTime());
            format = sdf.format(date);
        } catch (ParseException e) {
            System.out.println("异常");
        }
        System.out.println(format);
    }
}
