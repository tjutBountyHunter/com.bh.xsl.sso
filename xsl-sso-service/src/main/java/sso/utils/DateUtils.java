package sso.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 说明：
 *
 * @Auther: 11432_000
 * @Date: 2018/10/3 19:11
 * @Description:
 */
public class DateUtils {

    public static String getDateToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getDateTimeToString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String dateTimeToString(String dateTime){
        /**
         *
         * 功能描述: 将dateTime类型转换为String。
         *
         * @param: [dateTime]
         * @return: java.lang.String
         * @auther: 11432_000
         * @date: 2018/10/6 11:19
         */
        String format = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(dateTime);
            format = sdf.format(date);
        } catch (ParseException e) {
            System.out.println("异常");
        }
        return format;
    }
}
