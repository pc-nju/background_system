package com.bms.service;

import com.bms.entity.Subject;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:22
 */
public interface SubjectService {
    /**
     * 新增{@link Subject}
     * @param subject {@link Subject}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addSubject(Subject subject);

    /**
     * 获取所有{@link Subject}
     * @return {@link Subject}集合
     */
    List<Subject> getAllSubjects();

    /**
     * 更新{@link Subject}
     * @param subject 待更新{@link Subject}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateSubject(Subject subject);

    /**
     * 删除指定{@link Subject}
     * @param id {@link Subject}id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeSubject(Long id);

    /**
     * 根据id获取指定{@link Subject}
     * @param id {@link Subject} id
     * @return {@link Subject}
     */
    Subject getSubjectById(Long id);
}
