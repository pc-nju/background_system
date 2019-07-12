package com.bms.service.impl;

import com.bms.dao.SubjectDao;
import com.bms.entity.Subject;
import com.bms.exception.BaseException;
import com.bms.service.SubjectService;
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
 * @date 2019-07-10 19:23
 */
@Primary
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectDao;

    @Override
    public boolean addSubject(Subject subject) {
        if (!CollectionUtils.isEmpty(selectSubject(subject))) {
            throw new BaseException("该科目已存在！");
        }
        return insertSubject(subject) == 1;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return selectSubject(new Subject());
    }

    @Override
    public boolean updateSubject(Subject subject) {
        Objects.requireNonNull(subject);
        Subject tempCamp = new Subject();
        tempCamp.setId(subject.getId());
        List<Subject> subjects = selectSubject(tempCamp);
        if (CollectionUtils.isEmpty(subjects)) {
            throw new BaseException("不存在该科目！");
        }
        return update(subject) == 1;
    }

    @Override
    public boolean removeSubject(Long id) {
        Subject subject = new Subject();
        subject.setId(id);
        List<Subject> subjects = selectSubject(subject);
        if (CollectionUtils.isEmpty(subjects)) {
            throw new BaseException("不存在该科目！");
        }
        return subjectDao.deleteSubject(id) == 1;
    }

    private int insertSubject(Subject subject) {
        return subjectDao.insertSubject(subject);
    }
    private List<Subject> selectSubject(Subject subject) {
        return subjectDao.selectSubject(subject);
    }
    private int update(Subject subject) {
        return subjectDao.updateSubject(subject);
    }
}
