package com.bms.util;

import com.bms.dto.ResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 咸鱼
 * @date 2019-06-04 19:45
 */
public final class CommonUtils {
    /**
     * 私有构造函数，工具类不可被实例化
     */
    private CommonUtils() {}



    public static void writeResponse(HttpServletResponse response, ResultDto resultDto) throws IOException {
        writeResponse(response, resultDto, null);
    }
    public static void writeResponse(HttpServletResponse response, ResultDto resultDto, Integer status) throws IOException {
        response.setContentType(FinalName.CONTENT_TYPE);
        if (status != null) {
            response.setStatus(status);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(resultDto));
        out.flush();
        out.close();
    }
    public static String getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        return year + "年第" + week + "周";
    }
    public static Date getMondayInWeek(String week) {
        int year = Integer.parseInt(week.substring(0, week.indexOf("年")));
        int weekOfYear = Integer.parseInt(week.substring(week.indexOf("第") + 1, week.indexOf("周")));
        Calendar calendar = Calendar.getInstance();
        calendar.setWeekDate(year, weekOfYear, Calendar.MONDAY);
        return calendar.getTime();
    }
    public static String formatDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DateTimeFormatter.ofPattern(FinalName.DATE_FORMAT));
    }
    public static LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDateTime parseDate(String dateStr) {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public static LocalTime parseTime(String timeStr) {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
    }
    public static LocalDateTime getLastTimeOfYear(int year) {
        return parseDate(year + "-12-31 23:59");
    }
}
