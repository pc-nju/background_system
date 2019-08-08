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
@RequestMapping("/statistics/lessons")
public class LessonStatisticsController {
    private final SubjectService subjectService;
    private final UserService userService;
    private final LessonService lessonService;

    @GetMapping
    public ResultDto getLessonStatistics(@RequestParam Integer year,
                                         @RequestParam(required = false) Integer month) {
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("subjects", subjectService.getAllSubjects());

        List<User> users = userService.getAllUsers();
        Map<Long, String> userMap = new HashMap<>(users.size());
        users.forEach(user -> userMap.put(user.getId(), user.getName()));
        resultMap.put("users", userMap);

        resultMap.put("statistics", lessonService.getLessonStatistics(year, month));
        return ResultDto.success().setObj(resultMap);
    }
}
