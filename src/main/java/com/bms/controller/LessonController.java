package com.bms.controller;

import com.bms.dto.ResultDto;
import com.bms.entity.*;
import com.bms.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-07-10 21:19
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/lesson/basic")
public class LessonController {
    private final CampusService campusService;
    private final SubjectService subjectService;
    private final PeriodService periodService;
    private final UserService userService;

    private final LessonService lessonService;

    @GetMapping("/datas")
    public ResultDto getBasicDatas() {
        List<Campus> campus = campusService.getAllCampus();
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Period> periods = periodService.getAllPeriods();
        List<User> users = userService.getAllUsers();
        if (CollectionUtils.isEmpty(campus)) {
            return ResultDto.error("请先设置校区信息，再进行排课！");
        }
        if (CollectionUtils.isEmpty(subjects)) {
            return ResultDto.error("请先设置科目信息，再进行排课！");
        }
        if (CollectionUtils.isEmpty(periods)) {
            return ResultDto.error("请先设置时间段，再进行排课！");
        }
        if (CollectionUtils.isEmpty(users)) {
            return ResultDto.error("请先增加教师，再进行排课！");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("campuses", campus);
        resultMap.put("subjects", subjects);
        resultMap.put("periods", periods);
        resultMap.put("users", users);
        return ResultDto.success().setObj(resultMap);
    }

    @PostMapping
    public ResultDto addLesson(@RequestBody @Valid Lesson lesson, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误!");
        }
        if (!lessonService.addLesson(lesson)) {
            return ResultDto.error("新增失败!");
        }
        return ResultDto.success("新增成功！");
    }

    @GetMapping
    public ResultDto getLessons(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")Date startTime,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Long subjectId,
                                   @RequestParam(required = false) Long campusId) {
        return ResultDto.success().setObj(lessonService.getLessons(startTime, userId, subjectId, campusId));
    }

    @GetMapping("/users")
    public ResultDto getAllUsers() {
        return ResultDto.success().setObj(userService.getAllUsers());
    }

    @GetMapping("/subjects")
    public ResultDto getAllSubjects() {
        return ResultDto.success().setObj(subjectService.getAllSubjects());
    }

    @GetMapping("/campuses")
    public ResultDto getAllCampuses() {
        return ResultDto.success().setObj(campusService.getAllCampus());
    }

    @PutMapping
    public ResultDto updateLesson(@RequestBody Lesson lesson) {
        if (!lessonService.updateLesson(lesson)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @DeleteMapping("/{ids}")
    public ResultDto removeLesson(@PathVariable String ids) {
        if (!lessonService.removeLesson(ids)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }

}
