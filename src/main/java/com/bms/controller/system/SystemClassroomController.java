package com.bms.controller.system;

import com.bms.dto.ResultDto;
import com.bms.entity.Classroom;
import com.bms.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 咸鱼
 * @date 2019-07-08 22:31
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/system/classroom")
public class SystemClassroomController {
    private final ClassroomService classroomService;

    @PostMapping
    public ResultDto addClassroom(@RequestBody @Valid Classroom classroom, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!classroomService.addClassroom(classroom)) {
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }

    @GetMapping
    public ResultDto getAllClassrooms() {
        return ResultDto.success().setObj(classroomService.getAllClassrooms());
    }

    @PutMapping
    public ResultDto updateClassroom(@RequestBody Classroom classroom) {
        if (!classroomService.updateClassroom(classroom)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @DeleteMapping("/{id}")
    public ResultDto removeClassroom(@PathVariable Long id) {
        if (!classroomService.removeClassroom(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
}
