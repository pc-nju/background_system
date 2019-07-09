package com.bms.dao;

import com.bms.entity.Campus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:32
 */
public interface CampusDao {
    /**
     * 新增{@link Campus}
     * @param campus {@link Campus}
     * @return 受影响的行数
     */
    int insertCampus(Campus campus);

    /**
     * 查询{@link Campus}
     * @param campus 待查询{@link Campus}
     * @return {@link Campus}
     */
    List<Campus> selectCampus(Campus campus);

    /**
     * 更新{@link Campus}
     * @param campus {@link Campus}
     * @return 受影响的行数
     */
    int updateCampus(Campus campus);

    /**
     * 删除{@link Campus}
     * @param id {@link Campus} id
     * @return 受影响的行数
     */
    int deleteCampus(@Param("id") Long id);
}
