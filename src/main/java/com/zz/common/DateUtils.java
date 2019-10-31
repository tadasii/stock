package com.zz.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private static final String DATEFULLSTR="yyyy-MM-dd HH:mm:ss";

    private static final String DATE8STR="yyyyMMdd";

    private static final String TIME6STR="HHmmss";
    public static final SimpleDateFormat getDate8Format(){
        return new SimpleDateFormat(DATE8STR);
    }

    public static final SimpleDateFormat getTime6Format(){
        return new SimpleDateFormat(TIME6STR);
    }

    public static final String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * @return
     * @comment 获取当前时间，精确到毫秒str_DateFormat为yyMMddHHmmssSSS或者yyyyMMddHHmmssSSS
     *
     */
    public static String getCurrentTimeByFormat(String str_DateFormat){
        Calendar cal=Calendar.getInstance();
        Date date=cal.getTime();
        try{
            SimpleDateFormat sdFormat=new SimpleDateFormat(str_DateFormat);
            String myTime=sdFormat.format(date);
            return myTime;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCurrentDate8Str(){
        return getCurrentTimeByFormat(DATE8STR);
    }
    /**
     * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制)<br>
     * 如Sat May 11 17:24:21 CST 2002 to '2002-05-11 17:24:21'<br>
     * @param sDate  Date 日期<br>
     * @return String 字符串<br>
     */
    public static String checkDate(String sDate) {
        String sReturn = "1";
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(sDate);
        if (isNum.matches()) {
            int year = Integer.parseInt(sDate.substring(0, 4));
            int month = Integer.parseInt(sDate.substring(4, 6));
            int day = Integer.parseInt(sDate.substring(6, 8));
            int hour = Integer.parseInt(sDate.substring(8, 10));
            int minute = Integer.parseInt(sDate.substring(10, 12));
            int second = Integer.parseInt(sDate.substring(12, 14));
            if (month > 12 || month < 1) {
                sReturn = "-1";
            }
            // 2月份判断是不是闰年
            if (month == 2) {
                if (year % 4 == 0 && year % 100 != 0) {
                    if (day > 29 || day < 1) {
                        sReturn = "-1";
                    }
                } else {
                    if (day > 28 || day < 1) {
                        sReturn = "-1";
                    }
                }
            }
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day > 31 || day < 1) {
                    sReturn = "-1";
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30 || day < 1) {
                    sReturn = "-1";
                }
            }
            if (hour > 24 || hour < 0) {
                sReturn = "-1";
            }
            if (minute > 60 || minute < 0) {
                sReturn = "-1";
            }
            if (second > 60 || second < 0) {
                sReturn = "-1";
            }
        } else {
            sReturn = "-1";
        }
        return sReturn;
    }

    public static Date transFormatDateStr(String formatDateStr){
        Date trlDt = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATEFULLSTR);
        try {
            trlDt = sdf.parse(formatDateStr);
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误");
        }
        return trlDt;
    }

    public static Date transFormatDateStr(String formatDateStr, String pattern){
        Date trlDt = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            trlDt = sdf.parse(formatDateStr);
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误");
        }
        return trlDt;
    }

    /**
     * 获取昨天的时间
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date date = calendar.getTime();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayStr = df.format(date);
        Date yesterdayDate = null;
        try {
            yesterdayDate = df.parse(yesterdayStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return  yesterdayDate;
    }

    /**
     * 获取年开始时间
     * @param i 0当年 -1去年 1明年
     * @return
     */
    public static Date getYearStartTime(int i){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date result = calendar.getTime();
        return  result;
    }

    /**
     * 获取给定时间的当天最后时间
     * @param date
     * @return
     */
    public static Date getEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar.getTime();
    }


    /**
     * 获取年结束时间
     * @param i 0当年 -1去年 1明年
     * @return
     */
    public static Date getYearEndTime(int i){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH,11);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        Date result = calendar.getTime();
        return  result;
    }

    /**
     * 获得给定时间的月份开始时间
     * @param date
     * @return
     */
    public static Date getMothStartTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date result = calendar.getTime();
        return  result;
    }

    /**
     * 获得给定时间的月份结束时间
     * @param date
     * @return
     */
    public static Date getMothEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        Date result = calendar.getTime();
        return  result;
    }
    /**
     * 增加月份
     * @param date 时间
     * @param i 月份数
     * @return 增加月份的时间
     */
    public static Date addMonth(Date date,int i){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,i);
        return calendar.getTime();
    }

    public static int getTwoDateDay(Date beginDate,Date endDate){
       long res= (endDate.getTime()-beginDate.getTime())/(60*60*24*1000);
        return (int) res +1;
    }

    /**
     * 判断时间是否是今天
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = simpleDateFormat.format(new Date());
        String input = simpleDateFormat.format(date);
        if (today.equals(input)) {
            return true;
        }
        return false;
    }

//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        Date beginDate = sdf.parse("2019-05-01");
//        Date endDate = sdf.parse("2019-05-01");
//        System.out.println(getTwoDateDay(beginDate,endDate));
//    }

    public static Date parseForDate(String source){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
             date = sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static long parseForNumber(String source){
        Date date = parseForDate(source);
        return  date.getTime();
    }

    public static String getDateStr(Date date,String pattern){
        try{
            SimpleDateFormat sdFormat=new SimpleDateFormat(pattern);
            String myTime=sdFormat.format(date);
            return myTime;
        } catch (Exception e) {
            return "";
        }
    }


}
