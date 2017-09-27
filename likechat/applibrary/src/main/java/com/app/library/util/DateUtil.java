package com.app.library.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期相关工具栏
 */
public class DateUtil
{
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static SimpleDateFormat simpleDateFormat()
    {
        SIMPLE_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        return SIMPLE_DATE_FORMAT;
    }

    /**
     * 返回时间 HH:mm
     *
     * @param strDate
     * @return
     */
    public static String parseTime(String strDate)
    {
        try
        {
            Date date = simpleDateFormat().parse(strDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String strTime = simpleDateFormat.format(date);
            return strTime;
        }
        catch (Exception e)
        {

        }

        return "";
    }

    /**
     * 返回日期 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static String parseDate(String strDate)
    {
        try
        {
            Date date = simpleDateFormat().parse(strDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String str = simpleDateFormat.format(date);
            return str;
        }
        catch (Exception e)
        {

        }

        return "";
    }

    /**
     *
     * @param date
     * @return yyyy-MM-dd
     */
    public static String parseDate(Date date)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String str = simpleDateFormat.format(date);
            return str;
        }
        catch (Exception e)
        {

        }

        return "";
    }

    /**
     * 将日期信息转换成今天、明天、后天、星期
     *
     * @param strDate
     * @return 0 今天，1 明天，2 后天，-1 昨天 -2 前天
     */
    public static int getDateDetail(String strDate)
    {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        try
        {
            today.setTime(new Date());
            //logCalendar(today);
            today.set(Calendar.HOUR_OF_DAY, 0); // 设置24小时制的时间
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            //logCalendar(today);

            Date date = simpleDateFormat().parse(strDate);
            target.setTime(date);
            //logCalendar(target);
            target.set(Calendar.HOUR_OF_DAY, 0); // 设置24小时制的时间
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
            //logCalendar(target);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -100;
        }

        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return xcts;
    }

    /**
     * 将日期信息转换成今天、明天、后天、星期
     *
     * @param lDate
     * @return 0 今天，1 明天，2 后天，-1 昨天 -2 前天
     */
    public static int getDateDetail(long lDate)
    {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        try
        {
            today.setTime(new Date());
            //logCalendar(today);
            today.set(Calendar.HOUR_OF_DAY, 0); // 设置24小时制的时间
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            //logCalendar(today);

            Date date = new Date(lDate);
            target.setTime(date);
            //logCalendar(target);
            target.set(Calendar.HOUR_OF_DAY, 0); // 设置24小时制的时间
            target.set(Calendar.MINUTE, 0);
            target.set(Calendar.SECOND, 0);
            //logCalendar(target);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -100;
        }

        long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
        int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
        return xcts;
    }

    private static void logCalendar(Calendar calendar)
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String log = String.format("%d-%d-%d %d:%d:%d", year, month, day, hour, minute, second);
        LogUtil.d(log);
    }

    /**
     * 日期转换为字符串
     *
     * @param lDate
     * @return
     */
    public static String formatDate(long lDate)
    {
        return formatDate(new Date(lDate));
    }

    /**
     * 日期转换为字符串
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = getSimpleDateFormat(isToday(date));
            String strDate = simpleDateFormat.format(date);
            return strDate;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return "";
    }

    private static SimpleDateFormat getSimpleDateFormat(boolean today)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = null;
            if (today)
            {
                simpleDateFormat = new SimpleDateFormat(
                        "今天 HH:mm", Locale.getDefault());

                return simpleDateFormat;
            }
            else
            {
                simpleDateFormat = new SimpleDateFormat(
                        "MM月dd日 HH:mm", Locale.getDefault());

                return simpleDateFormat;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断给定日期是否是今天内的日期
     * @param date
     * @return
     */
    private static boolean isToday(Date date)
    {
        try
        {
            Date today = new Date();
            Calendar caledarToday = GregorianCalendar.getInstance(Locale.getDefault());
            caledarToday.setTime(today);
            int todayYear = caledarToday.get(Calendar.YEAR);
            int todayMonth = caledarToday.get(Calendar.MONTH);
            int todayDay = caledarToday.get(Calendar.DAY_OF_MONTH);

            Calendar caledarDate = GregorianCalendar.getInstance(Locale.getDefault());
            caledarDate.setTime(date);
            int dateYear = caledarDate.get(Calendar.YEAR);
            int dateMonth = caledarDate.get(Calendar.MONTH);
            int dateDay = caledarDate.get(Calendar.DAY_OF_MONTH);

            if (todayYear == dateYear && todayMonth == dateMonth && todayDay == dateDay)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
