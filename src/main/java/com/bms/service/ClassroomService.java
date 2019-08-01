package com.bms.service;

import com.bms.entity.Classroom;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:33
 */
public interface ClassroomService {
    /**
     * 新增{@link Classroom}
     * @param classroom {@link Classroom}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addClassroom(Classroom classroom);

    /**
     * 查询所有{@link Classroom}
     * @return {@link Classroom}集合
     */
    List<Classroom> getAllClassrooms();

    /**
     * 更新{@link Classroom}
     * @param classroom {@link Classroom}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateClassroom(Classroom classroom);

    /**
     * 删除{@link Classroom}
     * @param id {@link Classroom}id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeClassroom(Long id);
}
