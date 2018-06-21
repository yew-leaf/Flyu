package us.xingkong.flyu.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @作者: Xuer
 * @创建时间: 2018/6/9 15:09
 * @描述:
 * @更新日志:
 */
public class DateUtil {

    private static SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

    //php的坑，要乘个1000
    public static String formatTime(String time) {
        long l = Long.parseLong(time);
        if (time.length() == 10) {
            l = l * 1000;
        }
        Date date = new Date(l);
        String result = format.format(date);
        if (result.startsWith("0")) {
            result = result.substring(1);
        }
        return result;
    }

    public static String getTimeBefore(String time) {
        long l = 0;
        try {
            Date date = format.parse(formatTime(time));
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(DateUtils.getRelativeTimeSpanString(l));
    }
}
