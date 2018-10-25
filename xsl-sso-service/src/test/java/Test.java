import org.springframework.util.DigestUtils;
import sso.utils.DateUtils;
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
        String dateTimeToString = DateUtils.getDateTimeToString();
        dateTimeToString = dateTimeToString.substring(0 ,dateTimeToString.length()-2);
        System.out.println(dateTimeToString);
    }
}
