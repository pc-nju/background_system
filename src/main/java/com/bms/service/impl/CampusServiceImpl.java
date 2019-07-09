package com.bms.service.impl;

import com.bms.dao.CampusDao;
import com.bms.entity.Campus;
import com.bms.exception.BaseException;
import com.bms.service.CampusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:33
 */
@Primary
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class CampusServiceImpl implements CampusService {
    private final CampusDao campusDao;

    @Override
    public boolean addCampus(Campus campus) {
        if (!CollectionUtils.isEmpty(selectCampus(campus))) {
            throw new BaseException("该校区已存在！");
        }
        return insertCampus(campus) == 1;
    }

    @Override
    public List<Campus> getAllCampus() {
        return selectCampus(new Campus());
    }

    @Override
    public boolean updateCampus(Campus campus) {
        Objects.requireNonNull(campus);
        Campus tempCamp = new Campus();
        tempCamp.setId(campus.getId());
        List<Campus> campuses = selectCampus(tempCamp);
        if (CollectionUtils.isEmpty(campuses)) {
            throw new BaseException("不存在该校区！");
        }
        return update(campus) == 1;
    }

    @Override
    public boolean removeCampus(Long id) {
        Campus campus = new Campus();
        campus.setId(id);
        List<Campus> campuses = selectCampus(campus);
        if (CollectionUtils.isEmpty(campuses)) {
            throw new BaseException("不存在该校区！");
        }
        return campusDao.deleteCampus(id) == 1;
    }

    private int insertCampus(Campus campus) {
        return campusDao.insertCampus(campus);
    }
    private List<Campus> selectCampus(Campus campus) {
        return campusDao.selectCampus(campus);
    }
    private int update(Campus campus) {
        return campusDao.updateCampus(campus);
    }
}
