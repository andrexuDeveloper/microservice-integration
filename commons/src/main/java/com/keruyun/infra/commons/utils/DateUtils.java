package com.keruyun.infra.commons.utils;



import org.apache.commons.lang3.StringUtils;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @author luhj
 */
public class DateUtils {
  public static final String DATE = "yyyy-MM-dd";
  public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
  public static final String LONG_DATE_TIME = "yyyy-MM-dd HH:mm:ss zzz";
  public static final String TIME = "HH:mm:ss";
  public static final String HOUR_MINUTES = "HH:mm";
  public static final String SHORT_DATE = "yyyy-MM-dd HH:mm";

  public static final String START_SUFFIX = "00:00:00";
  public static final String END_SUFFIX = "23:59:59";

  /**
   * 判断时间格式是否正确
   * 
   * @param dateString 要判断的时间字符串，如"2014-06-01"
   * @param dateFormat 要判断的时间字符串应该符合的时间格式，如"yyyy-MM-dd"
   * @return 正确返回true,错误返回false
   */
  public static boolean isDate(String dateString, String dateFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    try {
      sdf.parse(dateString);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * 将字符串dateStr解析为yyyy-MM-dd格式的日期
   * 
   * @param dateStr
   * @return
   */
  public static Date parseToDate(String dateStr) {
    return parseDate(dateStr, DATE);
  }

  /**
   * 将字符串dateStr解析为yyyy-MM-dd HH:mm:ss格式的日期
   * 
   * @param dateStr
   * @return
   */
  public static Date parseToDateTime(String dateStr) {
    return parseDate(dateStr, DATETIME);
  }

  /**
   * 将字符串dateStr解析为dateFormat格式的日期,如果dateFormat为空，则以yyyy-MM-dd HH:mm:ss格式解析
   * 
   * @param dateStr
   * @param pattern
   * @return
   */
  public static Date parseDate(String dateStr, String pattern) {
    if (dateStr == null || dateStr.trim().length() == 0) {
      return null;
    }
    SimpleDateFormat fmt = new SimpleDateFormat(pattern == null ? DATETIME : pattern);
    try {
      return fmt.parse(dateStr);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 作为开始时间，需要HH：mm：ss为00:00:00
   * 
   * @param date
   * @return
   */
  public static Date getAsStartDateTime(Date date) {
    if (date == null) {
      return null;
    }
    return setTimeForDate(date, "00:00:00");
  }

  /**
   * 作为结束时间，需要HH：mm：ss为23:59:59
   * 
   * @param date
   * @return
   */
  public static Date getAsEndDateTime(Date date) {
    if (date == null) {
      return null;
    }
    return setTimeForDate(date, "23:59:59");
  }

  /**
   * n大于零，获得给定日期 n 天后的日期<br>
   * n小于零，获得给定日期 n 天前的日期
   * 
   * @param date
   * @param n
   * @return
   */
  public static Date addDay(Date date, int n) {
    if (date == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DATE, n);
    return cal.getTime();
  }

  /**
   * 
   * @param date
   * @return 返回yyyy-MM-dd格式的日期字符串
   */
  public static String formatToDate(Date date) {
    return new SimpleDateFormat(DATE).format(date);
  }

  /**
   * 
   * @param date
   * @return 返回HH:mm:ss格式的时间字符串
   */
  public static String formatToTime(Date date) {
    return new SimpleDateFormat(TIME).format(date);
  }

  /**
   * 
   * @param date
   * @return 返回HH:mm格式的时间字符串
   */
  public static String formatToHourAndMinutes(Date date) {
    return new SimpleDateFormat(HOUR_MINUTES).format(date);
  }

  /**
   * 
   * @param date
   * @return 返回yyyy-MM-dd HH:mm:ss格式的日期时间字符串
   */
  public static String formatToDateTime(Date date) {
    return new SimpleDateFormat(DATETIME).format(date);
  }

  /**
   * 
   * @param date
   * @return 返回yyyy-MM-dd HH:mm:ss格式的日期时间字符串
   */
  public static String formatToDateTime(Date date, String format) {
    return new SimpleDateFormat(format).format(date);
  }

  /**
   * 设置日期的时间为timeString给定的值,timeString为空则返回原date
   * 
   * @param date
   * @param timeString 格式为 HH:mm:ss(如 12:10:00)
   * @return
   */
  public static Date setTimeForDate(Date date, String timeString) {
    if (date == null || timeString == null) {
      return date;
    }
    return DateUtils.parseToDateTime(DateUtils.formatToDate(date) + " " + timeString);
  }

  public static int getDayNumBetween(Date fromDate, Date toDate) {
    if (null == fromDate || null == toDate) {
      return -1;
    }
    fromDate = getAsStartDateTime(fromDate);
    toDate = getAsStartDateTime(toDate);
    long intervalMilli = toDate.getTime() - fromDate.getTime();
    int ret = (int) (intervalMilli / (24 * 60 * 60 * 1000));
    return ret < 0 ? ret - 1 : ret + 1;
  }

  public static Date getToday() {
    return setTimeForDate(new Date(), "00:00:00");
  }

  public static Date string2ShortDate(String dateStr) {
    SimpleDateFormat formatter = new SimpleDateFormat(SHORT_DATE);
    Date date = null;
    try {
      date = formatter.parse(dateStr);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return date;
  }





  /**
   * 字符串转化为日期
   * 
   * @param dateStr
   * @param flag true:日期中带时分秒，false:日期中不带时分秒
   * @return
   */
  public static Date string2Date(String dateStr, boolean flag) {

    SimpleDateFormat formatter = getDateFormat(flag);
    Date date = null;
    try {
      date = formatter.parse(dateStr);
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return date;
  }

  public static SimpleDateFormat getDateFormat(boolean flag) {
    SimpleDateFormat formatter = null;
    if (flag) {
      formatter = new SimpleDateFormat(DATETIME);
    } else {
      formatter = new SimpleDateFormat(DATE);
    }

    return formatter;
  }

  /**
   * 获取 时间区域里的 yyyy-mm-dd 包括 开始时间和结束时间
   * 
   * @param starttime 开始时间
   * @param endtime 结束时间
   * @return
   */
  public static Map<String, List<String>> getXlistByDay(Date starttime, Date endtime) {
    try {
      Map<String, List<String>> xlist = new HashMap<String, List<String>>();
      Calendar startCalendar = Calendar.getInstance();
      Calendar endCalendar = Calendar.getInstance();
      startCalendar.setTime(starttime);
      endCalendar.setTime(endtime);
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat showformat = new SimpleDateFormat("yyyy.MM.dd");
      List<String> showxlist = new ArrayList<String>();
      List<String> tempxlist = new ArrayList<String>();
      endCalendar.add(Calendar.DAY_OF_MONTH, 1);
      while (true) {
        if (startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
          tempxlist.add(df.format(startCalendar.getTime()));
          showxlist.add(showformat.format(startCalendar.getTime()));
        } else {
          break;
        }
        startCalendar.add(Calendar.DAY_OF_MONTH, 1);
      }
      xlist.put("xlist", tempxlist);
      xlist.put("showxlist", showxlist);
      return xlist;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }





  /**
   * 获取该年 第几周的开始时间和结束时间
   * 
   * @param year
   * @param week
   * @return
   */
  public static String getStartandEndDay(int year, int week) {
    try {
      String time = "";
      SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
      Calendar c = new GregorianCalendar(Locale.CHINA);
      c.set(Calendar.YEAR, year);
      c.set(Calendar.WEEK_OF_YEAR, week);
      c.setFirstDayOfWeek(Calendar.MONDAY);
      c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
      time = df.format(c.getTime());
      c.roll(Calendar.DAY_OF_WEEK, 6);
      time = time + "~" + df.format(c.getTime());
      return time;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取时间段内的天数
   * 
   * @param starttime
   * @param endtime
   * @return
   * @throws Exception
   */
  public static int getDays(Date starttime, Date endtime) throws Exception {
    int days = (int) ((endtime.getTime() - starttime.getTime()) / ((1000 * 60 * 60 * 24)));
    if (days < 0) {
      throw new Exception("开始时间不能大于结束时间");
    } else {
      return days;
    }
  }

  /**
   * 获取时间段内的天数
   * 
   * @param starttimeStr
   * @param endtimeStr
   * @return
   * @throws Exception
   */
  public static int getDays(String starttimeStr, String endtimeStr) throws Exception {
    Date starttime = parseToDate(starttimeStr);
    Date endtime = parseToDate(endtimeStr);
    int days = (int) ((endtime.getTime() - starttime.getTime()) / ((1000 * 60 * 60 * 24)));
    if (days < 0) {
      throw new Exception("开始时间不能大于结束时间");
    } else {
      return days;
    }
  }

  /**
   * 
   * 获取指定日期前几天的日期
   * 
   * @param dateStr
   * @param days
   * @return
   * @throws Exception
   */
  public static String getLastDay(String dateStr, int days) throws Exception {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE);
    Date date = parseToDate(dateStr);
    Calendar rightNow = Calendar.getInstance();
    rightNow.setTime(date);
    rightNow.add(Calendar.DATE, -days);// 你要加减的日期 -2為前兩天
    Date nowDate = rightNow.getTime();

    StringBuffer sbf = new StringBuffer("");
    StringBuffer reStr = dateFormat.format(nowDate, sbf, new FieldPosition(0));
    return reStr.toString();
  }

  /**
   * x以 时段 为单位时 添加 显示间隔数
   * 
   * @param xlist
   * @param days
   */
  protected void intervalByDay(Map<String, Object> xlist) {
    xlist.put("interval", 0);
  }

  /**
   * x以 yyyy-mm-dd 为单位时 添加 显示间隔数
   * 
   * @param xlist
   * @param days
   */
  protected void intervalByDays(Map<String, Object> xlist, int days) {
    if (days > 10 && days <= 20) {
      xlist.put("interval", 0);
    } else if (days > 20 && days <= 31) {
      xlist.put("interval", 0);
    } else {
      xlist.put("interval", 0);
    }

  }

  /**
   * x以 周 为单位 时候 添加显示间隔数
   * 
   * @param xlist
   * @param weeksCount
   */
  public static void intervalByWeek(Map<String, Object> xlist, int weeksCount) {

    // 7<周数<=14,每隔一周显示一个日期区间
    if (weeksCount > 7 && weeksCount <= 14) {
      xlist.put("interval", 0);
    }
    // 每隔2周
    if (weeksCount > 14 && weeksCount <= 21) {
      xlist.put("interval", 0);
    }
    if (weeksCount > 21) {
      xlist.put("interval", 0);
    } else {
      xlist.put("interval", 0);
    }

  }

  /**
   * x以 月 为单位 时候 添加显示间隔数
   * 
   * @param xlist
   * @param weeksCount
   */
  public static void intervalByMonth(Map<String, Object> xlist) {
    xlist.put("interval", 0);
  }

  /**
   * 获取当年的所有月份
   * 
   * @return
   */
  public static Map<String, List<String>> getMothsByCurrentYear() {
    Calendar now = Calendar.getInstance();
    Map<String, List<String>> xlist = new HashMap<String, List<String>>();
    String year = Integer.toString(now.get(Calendar.YEAR));
    List<String> months = new ArrayList<String>();
    for (int i = 1; i <= 12; i++) {
      if (i < 10) {
        months.add(year + "-0" + i);
      } else {
        months.add(year + "-" + i);
      }
    }
    xlist.put("xlist", months);
    xlist.put("showxlist", months);
    return xlist;

  }









  /**
   * 获取当前日期是星期几，如“星期一”
   * 
   * @param date
   * @return 当前日期是星期几
   */
  public static String getWeekOfDate(Date date) {
    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
    if (w < 0) {
      w = 0;
    }
    return weekDays[w];
  }


  /**
   *
   * @param date
   * @param timeUnit
   *        指Calendar.YEAR：年，Calendar.DAY_OF_YEAR：天;Calendar.HOUR_OF_DAY:小时;Calendar.MINUTE：分;
   *        Calendar .SECOND：秒;Calendar.MILLISECOND：毫秒;
   * @param value
   * @return
   */
  public static Date set(Date date, int timeUnit, int value) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(timeUnit, value);
    return cal.getTime();
  }

  /**
   * 根据TimeUnit增加指定日期的的时间
   *
   * @author andrexu
   * @param date 要增加的日期
   * @param timeUnit 增加的日历字段（只能取 DAYS 到 MILLISECONDS 之间的枚举，否则报错）
   * @param value 增加的值(当值为负数时表示减少)
   * @return
   */
  public static Date add(Date date, int timeUnit, int value) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(timeUnit, value);
    return cal.getTime();
  }

  /**
   *
   * @param date
   * @param timeUnit
   *        指Calendar.YEAR：年，Calendar.DAY_OF_YEAR：天;Calendar.HOUR_OF_DAY:小时;Calendar.MINUTE：分;
   *        Calendar .SECOND：秒;Calendar.MILLISECOND：毫秒;
   * @return
   */
  public static int getValue(Date date, int timeUnit) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(timeUnit);
  }

  /**
   * 一天的开始时间
   * 
   * @return
   */
  public static String getStartTime() {
    Calendar c1 = new GregorianCalendar();
    c1.set(Calendar.HOUR_OF_DAY, 0);
    c1.set(Calendar.MINUTE, 0);
    c1.set(Calendar.SECOND, 0);
    return DateUtils.formatToDateTime(c1.getTime());
  }

  /**
   * 一天的结束时间
   * 
   * @return
   */
  public static String getEndTime() {
    Calendar c2 = new GregorianCalendar();
    c2.set(Calendar.HOUR_OF_DAY, 23);
    c2.set(Calendar.MINUTE, 59);
    c2.set(Calendar.SECOND, 59);
    return DateUtils.formatToDateTime(c2.getTime());
  }

  /**
   * 获取当前日期的前一天
   * 
   * @return
   * @throws ParseException
   * @throws Exception
   */
  public static Date getCurrentDayBefore() {
    // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = new Date();
    Calendar date = Calendar.getInstance();
    date.setTime(beginDate);
    date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
    try {
      Date endDate = dft.parse(dft.format(date.getTime()));
      return endDate;
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }



  /**
   * 按周计算起始日期 * @param year
   * 
   * @param startWeek
   * @param endWeek
   * @return
   */
  public static Map<String, Date> getWeekStartDayAndEndDay(int year, int startWeek, int endWeek) {

    Calendar calFirstDayOfTheYear = new GregorianCalendar(year, Calendar.JANUARY, 1);
    calFirstDayOfTheYear.add(Calendar.DATE, 7 * (startWeek - 1));
    calFirstDayOfTheYear.add(Calendar.DATE, 1);
    int dayOfWeek = calFirstDayOfTheYear.get(Calendar.DAY_OF_WEEK);

    Calendar calFirstDayInWeek = (Calendar) calFirstDayOfTheYear.clone();
    calFirstDayInWeek.add(Calendar.DATE, calFirstDayOfTheYear.getActualMinimum(Calendar.DAY_OF_WEEK) - dayOfWeek);
    calFirstDayInWeek.add(Calendar.DATE, 1);
    Date firstDayInWeek = calFirstDayInWeek.getTime();

    int temp = endWeek - (startWeek - 1);

    Calendar rightNow = Calendar.getInstance();
    rightNow.setTime(firstDayInWeek);
    rightNow.add(Calendar.DATE, temp * 7);// 你要加减的日期
    rightNow.add(Calendar.DATE, -1);
    Date endDate = rightNow.getTime();
    Map<String, Date> map = new HashMap<>();
    map.put("startDate", firstDayInWeek);
    map.put("endDate", endDate);
    return map;
  }

  public static Date longToDate(Long timeMills) {
    // TODO Auto-generated method stub
    Calendar cd = Calendar.getInstance();
    cd.setTimeInMillis(timeMills);
    return cd.getTime();
  }

  /**
   * 获取对应日期的月份
   * 
   * @param date
   * @return
   */
  public static int getMonthByDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int month = cal.get(Calendar.MONTH);
    return month;
  }


  /**
   * 获取当天的年月日. 格式:yyyyMMdd
   * 
   * @return
   */
  public static String getNowDate() {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    return format.format(new Date());
  }

  /**
   * 获取当天的年月日. 格式:yyyyMMdd
   * 
   * @return
   */
  public static String getNowDateYMDHM() {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
    return format.format(new Date());
  }

  /**
   * 把毫秒数变时年月日时分秒
   * 
   * @param millis
   * @return
   */
  public static String formatDuring(long millis) {
    return formatDuring(millis, "yyyy-MM-dd HH:mm:ss");
  }


  public static String formatDuring(long millis, String defaultValue) {
    if (StringUtils.isBlank(defaultValue)) {
      defaultValue = "yyyy-MM-dd HH:mm:ss";
    }
    Calendar cd = Calendar.getInstance();
    cd.setTimeInMillis(millis);
    SimpleDateFormat format = new SimpleDateFormat(defaultValue);
    String sb = format.format(cd.getTime());
    return sb;
  }

  /**
   * 获取开始 结束时间段内的每一天日期
   * 
   * @param dBegin
   * @param dEnd
   * @return
   */
  public static List<Date> findDates(Date dBegin, Date dEnd) {
    List lDate = new ArrayList();
    lDate.add(dBegin);
    Calendar calBegin = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calBegin.setTime(dBegin);
    Calendar calEnd = Calendar.getInstance();
    // 使用给定的 Date 设置此 Calendar 的时间
    calEnd.setTime(dEnd);
    // 测试此日期是否在指定日期之后
    while (dEnd.after(calBegin.getTime())) {
      // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
      calBegin.add(Calendar.DAY_OF_MONTH, 1);
      lDate.add(calBegin.getTime());
    }
    return lDate;
  }

  /**
   * 获取对应月份的总天数
   * 
   * @param date
   * @return
   */
  public static int getMonthDays(Date date) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date); // 放入你的日期
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
  }



  public static void main(String[] args) {

  }


}
