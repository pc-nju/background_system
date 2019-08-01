package com.bms.dao;

import com.bms.entity.Classroom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:32
 */
public interface ClassroomDao {
    /**
     * 新增{@link Classroom}
     * @param classroom {@link Classroom}
     * @return 受影响的行数
     */
    int insertClassroom(Classroom classroom);

    /**
     * 查询{@link Classroom}
     * @param classroom 待查询{@link Classroom}
     * @return {@link Classroom}
     */
    List<Classroom> selectClassroom(Classroom classroom);

    /**
     * 更新{@link Classroom}
     * @param classroom {@link Classroom}
     * @return 受影响的行数
     */
    int updateClassroom(Classroom classroom);

    /**
     * 删除{@link Classroom}
     * @param id {@link Classroom} id
     * @return 受影响的行数
     */
    int deleteClassroom(@Param("id") Long id);
}
