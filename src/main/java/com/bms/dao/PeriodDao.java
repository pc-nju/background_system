package com.bms.dao;

import com.bms.entity.Period;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:24
 */
public interface PeriodDao {
    /**
     * 新增{@link Period}
     * @param period {@link Period}
     * @return 受影响的行数
     */
    int insertPeriod(Period period);

    /**
     * 查询{@link Period}
     * @param period 待查询{@link Period}
     * @return {@link Period}
     */
    List<Period> selectPeriod(Period period);

    /**
     * 更新{@link Period}
     * @param period {@link Period}
     * @return 受影响的行数
     */
    int updatePeriod(Period period);

    /**
     * 删除{@link Period}
     * @param id {@link Period} id
     * @return 受影响的行数
     */
    int deletePeriod(@Param("id") Long id);
}
