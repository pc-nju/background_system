package com.bms.service.impl;

import com.bms.dao.ClassroomDao;
import com.bms.entity.Classroom;
import com.bms.exception.BaseException;
import com.bms.service.ClassroomService;
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
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomDao classroomDao;

    @Override
    public boolean addClassroom(Classroom classroom) {
        if (!CollectionUtils.isEmpty(selectClassroom(classroom))) {
            throw new BaseException("该校区已存在！");
        }
        return insertClassroom(classroom) == 1;
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return selectClassroom(new Classroom());
    }

    @Override
    public boolean updateClassroom(Classroom classroom) {
        Objects.requireNonNull(classroom);
        Classroom tempCamp = new Classroom();
        tempCamp.setId(classroom.getId());
        List<Classroom> classroomes = selectClassroom(tempCamp);
        if (CollectionUtils.isEmpty(classroomes)) {
            throw new BaseException("不存在该校区！");
        }
        return update(classroom) == 1;
    }

    @Override
    public boolean removeClassroom(Long id) {
        Classroom classroom = new Classroom();
        classroom.setId(id);
        List<Classroom> classroomes = selectClassroom(classroom);
        if (CollectionUtils.isEmpty(classroomes)) {
            throw new BaseException("不存在该校区！");
        }
        return classroomDao.deleteClassroom(id) == 1;
    }

    private int insertClassroom(Classroom classroom) {
        return classroomDao.insertClassroom(classroom);
    }
    private List<Classroom> selectClassroom(Classroom classroom) {
        return classroomDao.selectClassroom(classroom);
    }
    private int update(Classroom classroom) {
        return classroomDao.updateClassroom(classroom);
    }
}
