package com.example.footer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 乃军 on 2017/11/24.
 */

public class ConvertDemo {

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /*
   * 将13位时间戳字符串转时间格式
   * */
    public static Date timestamp2Date(String str_num) throws ParseException {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(toLong(str_num)));

        Date date1=sdf.parse(date);
        return date1;

    }

    /*
   * 将13位时间戳字符串转时间格式
   * */
    public static String timestampDate(String str_num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");


        String date = sdf.format(new Date(toLong(str_num)));
        return date;

    }


    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     *
     * @author wxy
     * @param ctime
     *            时间
     * @param format
     *            格式 格式描述:例如:yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String showTime(Date ctime, String format) {
        //System.out.println("当前时间是："+new Timestamp(System.currentTimeMillis()));


        //System.out.println("发布时间是："+df.format(ctime).toString());
        String r = "";
        if(ctime==null)return r;
        if(format==null)format="MM-dd HH:mm";

        long nowtimelong = System.currentTimeMillis();

        long ctimelong = ctime.getTime();
        long result = Math.abs(nowtimelong - ctimelong);

        if(result < 60000){// 一分钟内
            long seconds = result / 1000;
            if(seconds == 0){
                r = "刚刚";
            }else{
                r = seconds + "秒前";
            }
        }else if (result >= 60000 && result < 3600000){// 一小时内
            long seconds = result / 60000;
            r = seconds + "分钟前";
        }else if (result >= 3600000 && result < 86400000){// 一天内
            long seconds = result / 3600000;
            r = seconds + "小时前";
        }else if (result >= 86400000 && result < 1702967296){// 三十天内
            long seconds = result / 86400000;
            r = seconds + "天前";
        }else{// 日期格式
            format="MM-dd HH:mm";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(ctime).toString();
        }
        return r;
    }


    /**
     * String转long
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }
}
