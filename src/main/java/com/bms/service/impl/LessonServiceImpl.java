package com.bms.service.impl;

import com.bms.dao.LessonDao;
import com.bms.dao.LessonStatisticsDao;
import com.bms.dto.LessonDto;
import com.bms.dto.PlanDto;
import com.bms.entity.*;
import com.bms.exception.BaseException;
import com.bms.service.LessonService;
import com.bms.service.PeriodService;
import com.bms.service.SubjectService;
import com.bms.service.UserService;
import com.bms.util.FinalName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.bms.util.CommonUtils.*;


/**
 * @author 咸鱼
 * @date 2019-07-12 19:11
 */
@Primary
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class LessonServiceImpl implements LessonService {
    private final LessonDao lessonDao;
    private final PeriodService periodService;
    private final SubjectService subjectService;
    private final UserService userService;
    private final LessonStatisticsDao lessonStatisticsDao;

    /**
     * 业务逻辑：
     * 首先，查询当前科目，在当前时间段内，是否安排了其他老师来上
     * 其次，查询当前老师，在当前时间段内，是否安排了其他课程
     * 最后，查询当前教室，在当前时间段内，是否安排了其他课程
     */
    @Override
    public boolean addLesson(LessonVo lessonVo) {
        Subject subject = subjectService.getSubjectById(lessonVo.getSubjectId());
        if (subject == null) {
            throw new BaseException("待修改课程科目不存在！");
        }
        List<Lesson> lessons = new ArrayList<>();
        Lesson lesson = new Lesson();
        lessons.add(lesson);
        lesson.setCampusId(lessonVo.getCampusId());
        lesson.setSubjectId(lessonVo.getSubjectId());
        lesson.setUserId(lessonVo.getUserId());
        lesson.setClassroomId(lessonVo.getClassroomId());
        lesson.setStartTime(lessonVo.getStartTime());
        lesson.setEndTime(lessonVo.getEndTime());

        int lessonStatistics = getStatistics(lesson.getStartTime(), lesson.getEndTime(), subject.getMinutes());
        lesson.setLessonStatistics(lessonStatistics);

        lesson.setWeek(getWeekOfYear(localDateTime2Date(lesson.getStartTime())));
        // 若是循环排课，则将当年所有周该时间段排成该课程
        if (lessonVo.getIsWeekCycle()) {
            LocalDateTime lastTimeOfYear = getLastTimeOfYear(lesson.getEndTime().getYear());
            // 加一周
            LocalDateTime startTime = lesson.getStartTime().plusDays(FinalName.DAYS_OF_WEEK);
            LocalDateTime endTime = lesson.getEndTime().plusDays(FinalName.DAYS_OF_WEEK);
            while (!endTime.isAfter(lastTimeOfYear)){
                Lesson tempLesson = new Lesson();
                lessons.add(tempLesson);
                tempLesson.setCampusId(lesson.getCampusId());
                tempLesson.setSubjectId(lesson.getSubjectId());
                tempLesson.setUserId(lesson.getUserId());
                tempLesson.setClassroomId(lesson.getClassroomId());
                tempLesson.setStartTime(startTime);
                tempLesson.setEndTime(endTime);
                tempLesson.setLessonStatistics(lessonStatistics);
                tempLesson.setWeek(getWeekOfYear(localDateTime2Date(endTime)));
                startTime = startTime.plusDays(FinalName.DAYS_OF_WEEK);
                endTime = endTime.plusDays(FinalName.DAYS_OF_WEEK);
            }
        }
        //检测冲突
        lessons.forEach(this::detectConflicts);
        return lessonDao.insertLessons(lessons) == lessons.size();
    }

    /**
     * 将上课时间转换成课时，1个半小时为1个课时
     */
    private int getStatistics(@NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime, Integer minutesPerLesson) {
        return (int) Math.round(Duration.between(startTime, endTime).get(ChronoUnit.SECONDS) / (minutesPerLesson * 60.0));
    }

    @Override
    public List<LessonDto> getLessons(Date startTime, Long userId, Long subjectId, Long campusId, Long classroomId) {
        Lesson lesson = new Lesson();
        if (startTime != null) {
            lesson.setWeek(getWeekOfYear(startTime));
        }
        if (userId != null) {
            lesson.setUserId(userId);
        }
        if (subjectId != null) {
            lesson.setSubjectId(subjectId);
        }
        if (campusId != null) {
            lesson.setCampusId(campusId);
        }
        if (classroomId != null) {
            lesson.setClassroomId(classroomId);
        }
        List<Lesson> lessons = selectLessons(lesson);
        List<Period> periods = periodService.getAllPeriods();
        Map<String, Map<String, Map<Integer, List<Lesson>>>> map = list2Map(lessons, periods);
        List<LessonDto> lessonDtos = new ArrayList<>();
        map.forEach((week, value) -> {
            LessonDto lessonDto = new LessonDto();
            lessonDtos.add(lessonDto);
            lessonDto.setWeek(week);
            lessonDto.setMonday(getMondayInWeek(week));
            List<PlanDto> plans = new ArrayList<>();
            // 初始化，保证没有数据的行也能显示
            periods.forEach(period -> {
                PlanDto plan = new PlanDto(period.getName(), null, null, null, null, null, null, null);
                plans.add(plan);
            });
            lessonDto.setPlans(plans);
            value.forEach((period, value1) -> plans.forEach(plan -> {
                if (plan.getPeriod().equals(period)) {
                    value1.forEach((index, value2) -> {
                        switch (index) {
                            case 0:
                                plan.setMon(value2);
                                break;
                            case 1:
                                plan.setTue(value2);
                                break;
                            case 2:
                                plan.setWed(value2);
                                break;
                            case 3:
                                plan.setThurs(value2);
                                break;
                            case 4:
                                plan.setFri(value2);
                                break;
                            case 5:
                                plan.setSat(value2);
                                break;
                            case 6:
                                plan.setSun(value2);
                                break;
                            default:
                                break;
                        }
                    });
                }
            }));
        });
        return lessonDtos;
    }

    @Override
    public boolean updateLesson(Lesson lesson) {
        if (lessonDao.selectLessonById(lesson.getId()) == null) {
            throw new BaseException("该课程不存在！");
        }
        Subject subject = subjectService.getSubjectById(lesson.getSubjectId());
        if (subject == null) {
            throw new BaseException("待修改课程科目不存在！");
        }
        // 检测当前课程计划是否和数据库中的已有计划存在冲突
        detectConflicts(lesson);
        lesson.setLessonStatistics(getStatistics(lesson.getStartTime(), lesson.getEndTime(), subject.getMinutes()));
        lesson.setWeek(getWeekOfYear(localDateTime2Date(lesson.getStartTime())));
        return lessonDao.updateLesson(lesson) == 1;
    }

    @Override
    public boolean removeLesson(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new BaseException("无待删除课程！");
        }
        String[] idArr = ids.split(",");
        // 当删除失败时，有可能已经删除一部分了，所以需要主动抛出异常，触发事务回滚
        if (lessonDao.deleteLessons(idArr) != idArr.length) {
            throw new BaseException("删除失败！");
        }
        return true;
    }

    @Override
    public Object getLessonStatistics(Integer year, Integer month) {
        if (year == null) {
            throw new BaseException("年份为空，无法获取统计数据！");
        }
        List<LessonStatistics> lessonStatisticsList = lessonStatisticsDao.selectLessonStatistics(year, month);
        Map<Long, Map<String, Integer>> resultMap = new LinkedHashMap<>();

        if (!CollectionUtils.isEmpty(lessonStatisticsList)) {
            // 查询所有课程
            List<Subject> subjects = subjectService.getAllSubjects();
            List<User> users = userService.getAllUsers();
            users.forEach(user -> {
                Map<String, Integer> map = new LinkedHashMap<>();
                subjects.forEach(subject -> map.putIfAbsent(subject.getName(), 0));
                resultMap.putIfAbsent(user.getId(), map);
            });
            lessonStatisticsList.forEach(lessonStatistics -> {
                Map<String, Integer> tempMap = resultMap.get(lessonStatistics.getUserId());
                // 若不是90分钟一节课，则需要转换成相应的课时
                tempMap.put(lessonStatistics.getSubjectName(), lessonStatistics.getLessonStatistics());
            });
        }
        return resultMap;
    }

    private Map<String, Map<String, Map<Integer, List<Lesson>>>> list2Map(List<Lesson> lessons, List<Period> periods) {
        Map<String, Map<String, Map<Integer, List<Lesson>>>> map = new HashMap<>();
        lessons.forEach(lesson -> {
            Map<String, Map<Integer, List<Lesson>>> map1 = map.computeIfAbsent(lesson.getWeek(), k -> new HashMap<>());
            LocalDateTime startLocalDateTime = lesson.getStartTime();
            final int dayOfWeeK = startLocalDateTime.getDayOfWeek().getValue() - 1;
            LocalTime startLocalTime = LocalTime.of(startLocalDateTime.getHour(), startLocalDateTime.getMinute());
            periods.forEach(period -> {
                String[] arr = period.getName().split("-");
                if (arr.length == FinalName.ARR_LENGTH) {
                    LocalTime startRangeLocalTime = parseTime(arr[0]);
                    LocalTime endRangeLocalTime = parseTime(arr[1]);
                    if (!startLocalTime.isBefore(startRangeLocalTime) && !startLocalTime.isAfter(endRangeLocalTime)) {
                        Map<Integer, List<Lesson>> map2 = map1.computeIfAbsent(period.getName(), k -> new HashMap<>());
                        List<Lesson> lessons1 = map2.computeIfAbsent(dayOfWeeK, k -> new ArrayList<>());
                        lessons1.add(lesson);
                    }
                }
            });
        });
        return map;
    }

    /**
     *     检测当前排课计划是否和数据库中已有计划存在冲突（PS：当{@link Lesson}id存在时，说明是更新{@link Lesson}操作，
     * 这时在查询数据库检测冲突时，需要排除当前{@link Lesson}的id，也就是 l.id != #{id}）
     * @param lesson {@link Lesson}
     */
    private void detectConflicts(Lesson lesson) {
        //第一次查询：查询当前科目，在当前时间段内，是否已存在记录（PS：是否安排其他老师来上）
        Lesson tempLesson1 = new Lesson();
        if (lesson.getId() != null) {
            tempLesson1.setId(lesson.getId());
        }
        tempLesson1.setSubjectId(lesson.getSubjectId());
        tempLesson1.setStartTime(lesson.getStartTime());
        tempLesson1.setEndTime(lesson.getEndTime());
        List<Lesson> lessons1 = selectLessons(tempLesson1);
        if (!CollectionUtils.isEmpty(lessons1)) {
            throw new BaseException("科目【" + lessons1.get(0).getSubject().getName() + "】在当前时间段内已安排给【"
                    + lessons1.get(0).getUser().getName() + "】老师！");
        }

        //第二次查询：查询当前老师，在当前时间段内，是否已存在记录（PS：是否安排了其他课程）
        Lesson tempLesson2 = new Lesson();
        if (lesson.getId() != null) {
            tempLesson2.setId(lesson.getId());
        }
        tempLesson2.setUserId(lesson.getUserId());
        tempLesson2.setStartTime(lesson.getStartTime());
        tempLesson2.setEndTime(lesson.getEndTime());
        List<Lesson> lessons2 = selectLessons(tempLesson2);
        if (!CollectionUtils.isEmpty(lessons2)) {
            throw new BaseException("老师【" + lessons2.get(0).getUser().getName() + "】在当前时间段内已给其安排了【"
                    + lessons2.get(0).getSubject().getName() + "】课程！");
        }

        //第三次查询：查询当前教室，在当前时间段内，是否已存在记录（PS：是否安排了其他课程）
        Lesson tempLesson3 = new Lesson();
        if (lesson.getId() != null) {
            tempLesson3.setId(lesson.getId());
        }
        tempLesson3.setClassroomId(lesson.getClassroomId());
        tempLesson3.setStartTime(lesson.getStartTime());
        tempLesson3.setEndTime(lesson.getEndTime());
        List<Lesson> lessons3 = selectLessons(tempLesson3);
        if (!CollectionUtils.isEmpty(lessons3)) {
            throw new BaseException("教室【" + lessons3.get(0).getClassroom().getName() + "】在当前时间段内已给其安排了【"
                    + lessons3.get(0).getSubject().getName() + "】课程！");
        }
    }

    private List<Lesson> selectLessons(Lesson lesson) {
        return lessonDao.selectLessons(lesson);
    }
}
