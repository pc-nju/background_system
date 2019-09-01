package com.bms.service.impl;

import com.bms.dao.PeriodDao;
import com.bms.entity.Period;
import com.bms.exception.BaseException;
import com.bms.service.PeriodService;
import com.bms.util.FinalName;
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
public class PeriodServiceImpl implements PeriodService {
    private final PeriodDao periodDao;

    @Override
    public boolean addPeriod(Period period) {
        if (!CollectionUtils.isEmpty(selectPeriod(period))) {
            throw new BaseException("该时间段已存在！");
        }
        handleTimePeriodStr(period);
        return insertPeriod(period) == 1;
    }

    @Override
    public List<Period> getAllPeriods() {
        return selectPeriod(new Period());
    }

    @Override
    public boolean updatePeriod(Period period) {
        Objects.requireNonNull(period);
        Period tempCamp = new Period();
        tempCamp.setId(period.getId());
        List<Period> periods = selectPeriod(tempCamp);
        if (CollectionUtils.isEmpty(periods)) {
            throw new BaseException("不存在该该时间段！");
        }
        handleTimePeriodStr(period);
        return update(period) == 1;
    }

    @Override
    public boolean removePeriod(Long id) {
        Period period = new Period();
        period.setId(id);
        List<Period> periods = selectPeriod(period);
        if (CollectionUtils.isEmpty(periods)) {
            throw new BaseException("不存在该时间段！");
        }
        return periodDao.deletePeriod(id) == 1;
    }

    private int insertPeriod(Period period) {
        return periodDao.insertPeriod(period);
    }
    private List<Period> selectPeriod(Period period) {
        return periodDao.selectPeriod(period);
    }
    private int update(Period period) {
        return periodDao.updatePeriod(period);
    }

    /**
     * 处理时间段，防止格式错误，比如：8:20 08:20
     * @param period {@link Period}
     */
    private void handleTimePeriodStr(Period period) {
        if (period.getName().contains(FinalName.PERIOD_SEPARATOR)){
            String[] periodArr = period.getName().split(FinalName.PERIOD_SEPARATOR);
            period.setName(formatTime(periodArr[0]) + "-" + formatTime(periodArr[1]));
        } else {
            throw new BaseException("前端时间段格式错误！");
        }
    }
    private String formatTime(String periodStr) {
        String[] timeArr;
        if (periodStr.contains(FinalName.SEPARATOR_EN)){
            timeArr = periodStr.split(FinalName.SEPARATOR_EN);
        } else if (periodStr.contains(FinalName.SEPARATOR_CH)){
            timeArr = periodStr.split(FinalName.SEPARATOR_CH);
        } else {
            throw new BaseException("前端时间段格式错误！");
        }
        StringBuilder sb = new StringBuilder(5);
        String hourStr = timeArr[0];
        String minuteStr = timeArr[1];
        if (hourStr.length() < FinalName.TIME_FORMAT_LENGTH) {
            hourStr = "0" + hourStr;
        }
        if (minuteStr.length() < FinalName.TIME_FORMAT_LENGTH) {
            minuteStr = "0" + minuteStr;
        }
        return sb.append(hourStr).append(FinalName.SEPARATOR_EN).append(minuteStr).toString();
    }
}
