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
     * 获取所有科目分类
     * @return {@link Subject}集合
     */
    List<Subject> selectSubjectsClassification();

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

    /**
     * 根据科目分类id获取该分类下的所有{@link Subject}
     * @param parentId 分类id
     * @return {@link Subject}集合
     */
    List<Subject> selectSubjectsWithParentId(@Param("parentId") Long parentId);

    /**
     * 查询除科目分类以外的所有{@link Subject}
     * @return {@link Subject}集合
     */
    List<Subject> selectAllSubjects();
}
