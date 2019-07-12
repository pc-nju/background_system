package com.bms.dao;

import com.bms.entity.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:24
 */
public interface SubjectDao {
    /**
     * 新增{@link Subject}
     * @param subject {@link Subject}
     * @return 受影响的行数
     */
    int insertSubject(Subject subject);

    /**
     * 查询{@link Subject}
     * @param subject 待查询{@link Subject}
     * @return {@link Subject}
     */
    List<Subject> selectSubject(Subject subject);

    /**
     * 更新{@link Subject}
     * @param subject {@link Subject}
     * @return 受影响的行数
     */
    int updateSubject(Subject subject);

    /**
     * 删除{@link Subject}
     * @param id {@link Subject} id
     * @return 受影响的行数
     */
    int deleteSubject(@Param("id") Long id);
}
