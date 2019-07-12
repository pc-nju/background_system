package com.bms.service;

import com.bms.entity.Period;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:22
 */
public interface PeriodService {
    /**
     * 新增{@link Period}
     * @param period {@link Period}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addPeriod(Period period);

    /**
     * 获取所有{@link Period}
     * @return {@link Period}集合
     */
    List<Period> getAllPeriods();

    /**
     * 更新{@link Period}
     * @param period 待更新{@link Period}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updatePeriod(Period period);

    /**
     * 删除指定{@link Period}
     * @param id {@link Period}id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removePeriod(Long id);
}
