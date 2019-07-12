package com.bms.controller;

import com.bms.dto.ResultDto;
import com.bms.entity.Campus;
import com.bms.entity.Period;
import com.bms.entity.Subject;
import com.bms.entity.User;
import com.bms.service.CampusService;
import com.bms.service.PeriodService;
import com.bms.service.SubjectService;
import com.bms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/class/basic")
public class SystemClassController {
    private final CampusService campusService;
    private final SubjectService subjectService;
    private final PeriodService periodService;
    private final UserService userService;
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
}
