package com.bms.controller.system;

import com.bms.dto.ResultDto;
import com.bms.entity.Subject;
import com.bms.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/system/subject")
public class SystemSubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResultDto addSubject(@RequestBody @Valid Subject subject, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!subjectService.addSubject(subject)) {
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }
    @GetMapping("/classification")
    public ResultDto getSubjectsClassification() {
        return ResultDto.success().setObj(subjectService.getSubjectsClassification());
    }
    @GetMapping("/{parentId}")
    public ResultDto getSubjectsWithParentId(@PathVariable Long parentId) {
        return ResultDto.success().setObj(subjectService.getSubjectsWithParentId(parentId));
    }

    @GetMapping
    public ResultDto getAllSubjects() {
        return ResultDto.success().setObj(subjectService.getAllSubjects());
    }

    @PutMapping
    public ResultDto updateSubject(@RequestBody Subject subject) {
        if (!subjectService.updateSubject(subject)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }
    @DeleteMapping("/classification/{id}")
    public ResultDto removeSubjectClassification(@PathVariable Long id) {
        if (!subjectService.removeSubject(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }

    @DeleteMapping("/{id}")
    public ResultDto removeSubject(@PathVariable Long id) {
        if (!subjectService.removeSubject(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
}
