package com.bms.service;

import com.bms.entity.Campus;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:33
 */
public interface CampusService {
    /**
     * 新增{@link Campus}
     * @param campus {@link Campus}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addCampus(Campus campus);

    /**
     * 查询所有{@link Campus}
     * @return {@link Campus}集合
     */
    List<Campus> getAllCampus();

    /**
     * 更新{@link Campus}
     * @param campus {@link Campus}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateCampus(Campus campus);

    /**
     * 删除{@link Campus}
     * @param id {@link Campus}id
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeCampus(Long id);
}
