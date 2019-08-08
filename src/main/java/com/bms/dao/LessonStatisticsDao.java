package com.bms.dao;

import com.bms.entity.LessonStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-12 19:13
 */
public interface LessonStatisticsDao {
    /**
     * 获取课时统计
     * @param year 年份
     * @param month 月份
     * @return {@link LessonStatistics}集合
     */
    List<LessonStatistics> selectLessonStatistics(@Param("year") Integer year, @Param("month") Integer month);
}
