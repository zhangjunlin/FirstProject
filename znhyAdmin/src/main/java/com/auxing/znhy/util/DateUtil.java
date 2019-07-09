package com.auxing.znhy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;

/**
 * Created by 顾杨 on 2016/12/8.
 */
public class DateUtil {

    //返回时间戳，不显示毫秒，输入字符串为年月日，不包括时分秒毫秒
    public static long dateToTimeStamp(String datetime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime() / 1000;
        return ts;
    }

    //返回时间戳，不显示毫秒，输入字符串为年月日，包括时分秒，不包括毫秒
    public static long dateToTimeStampHMS(String datetime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime() / 1000;
        return ts;
    }

    //返回时间戳，显示毫秒，输入字符串为年月日，包括时分秒毫秒
    public static long dateToTimeStampHMSS(String datetime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime();
        return ts;
    }
    
    //返回时间戳
    public static long dateToTimeStampHM(String datetime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime();
        return ts;
    }

    //将指定格式的时间数据，转成时间戳，包括毫秒
    public static long dateToTimeStampHMSS(String datetime, String pattern) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(datetime);
        long ts = date.getTime();
        return ts;
    }

    //返回系统时间
    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    
    //返回系统时间
    public static String getCurrentTime1() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return date;
    }


    public static String dateFormat19(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSSZ");
        return simpleDateFormat.format(date);
    }


    //输入date返回yyyy/MM/dd
    public static String dateFormat(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    //输入date返回yyyy-MM-dd'T'HH:mm:ss
    public static String dateFormatISO8601(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return df.format(date);
    }

    //将毫秒级的时间戳转成秒级的时间戳
    public static long dateToSecondsTimeStamp(Date date) {
        long ts = date.getTime() / 1000;
        return ts;
    }

    //获取当前时间毫秒数
    public static long currentTimeMills() {
        return System.currentTimeMillis();
    }

    //获取今天零点零分零秒的毫秒数
    public static long currentZeroTimeMills() {
        return currentTimeMills() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
    }

    //获取指定日期的零点零分零秒的毫秒数
    public static String specifiedZeroTimeMills(String time) {
        StringBuilder stringBuilder = new StringBuilder();
        String ymd = time.substring(0, 10);
        return stringBuilder.append(ymd).append(" 00:00:00").toString();


//        return time / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
//        return time / (1000 * 3600 * 24) * (1000 * 3600 * 24);
    }

    //获取指定日期的23点59分59秒
    public static String specifiedEndTimeMills(String time) {
        StringBuilder stringBuilder = new StringBuilder();
        String ymd = time.substring(0, 10);
        return stringBuilder.append(ymd).append(" 23:59:59").toString();


//        return time / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
//        return time / (1000 * 3600 * 24) * (1000 * 3600 * 24);
    }



    //获取今天23点59分59秒的毫秒数
    public static long currentEndZeroTimeMills() {
        return currentZeroTimeMills() + 24 * 60 * 60 * 1000 - 1;
    }

    //获取今天零点零分零秒的毫秒数
    public static String currentZeroTimeMillsRtnStr() {
        return dateFormat(new Date(currentZeroTimeMills()), "yyyy-MM-dd HH:mm:ss");
    }

    //获取今天23点59分59秒的毫秒数
    public static String currentEndZeroTimeMillsRtnStr() {
        return dateFormat(new Date(currentEndZeroTimeMills()), "yyyy-MM-dd HH:mm:ss");
    }


    //获取昨天的这一时间的毫秒数
    public static long currentYesterdayTimeMills() {
        return System.currentTimeMillis() - 24 * 60 * 60 * 1000;
    }
    
    public static String getStartTime(String time, int m){
    	Long l = null;
		try {
			l = dateToTimeStampHM(time)+m*60*1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(l);
        date = date.substring(date.length()-5);
        return time+"-"+date;
    }
    
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }
    
    //通过开始时间和持续时间，查询剩余时间
    public static String getSurplusTime(String startTime, Integer duration) {
    	long time = 0;
    	try {
			time = dateToTimeStampHM(startTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	time += duration*1000*60;
    	
    	long myTime = System.currentTimeMillis();
    	
    	long diff = time - myTime;
    	
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
    	
        String hour = diff % nd / nh +((diff / nd)*24)+"";//计算差多少小时
        String min = diff % nd % nh / nm +"";//计算差多少分钟

    	
    	return hour + "小时" + min + "分钟";
    }
    
    public static String getEndDate(String startTime,String hour, String minute){
    	int hourInt = 0;
    	int minuteInt = 0;
    	if(hour != null && !"".equals(hour)){
    		hourInt = Integer.parseInt(hour);
    	}
    	if(minute != null && !"".equals(minute)){
    		minuteInt = Integer.parseInt(minute);
    	}
    	int min = hourInt*60+minuteInt;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    	Date newDate = null;
		try {
			newDate = df.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(newDate);
    	calendar.add(Calendar.MINUTE, min);
    	String endTime = df.format(calendar.getTime());
    	
    	return endTime;
    }
    
    
    public static void main(String[] args) throws ParseException {
    	System.out.println(getSurplusTime("2018-11-09 10:24", 6000));
    }

}
