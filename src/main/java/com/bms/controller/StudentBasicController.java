package com.bms.controller;

import com.bms.dto.StudentDto;
import com.bms.dto.ResultDto;
import com.bms.entity.Student;
import com.bms.service.StudentService;
import com.bms.service.SubjectService;
import com.bms.util.PoiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-19 20:54
 */
@RestController
@RequestMapping("/student/basic")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentBasicController {
    private final StudentService studentService;
    private final SubjectService subjectService;

    @GetMapping("/basicData")
    public ResultDto getBasicData() {
        return ResultDto.success().setObj(subjectService.getAllSubjects());
    }
    @GetMapping("/students")
    public ResultDto getStudents(StudentDto studentDto) {
        return ResultDto.success().setObj(studentService.getStudents(studentDto));
    }

    @PostMapping("/importStudent")
    public ResultDto importStudents(MultipartFile file) {
        List<Student> students = PoiUtils.importEmp2List(file, subjectService.getAllSubjects());
        if (!studentService.addStudents(students)) {
            return ResultDto.error("导入失败！");
        }
        return ResultDto.success("导入成功！");
    }
    @GetMapping("/exportStudent")
    public ResponseEntity<byte[]> exportStudents() {
        return PoiUtils.exportEmp2Excel(studentService.getAllStudents(), subjectService.getAllSubjects());
    }
    
    @DeleteMapping("/students/{ids}")
    public ResultDto removeStudents(@PathVariable String ids) {
        if (!studentService.removeStudents(ids)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }

    @PostMapping("/student")
    public ResultDto addStudent(@RequestBody @Valid Student student, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!studentService.addStudent(student)){
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }

    @PutMapping("/student")
    public ResultDto updateStudent(@RequestBody @Valid Student student, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!studentService.updateStudent(student)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功");
    }

    @PutMapping("/student/state")
    public ResultDto updateStudentStudyState(@RequestParam Long id,
                                             @RequestParam Boolean studyState,
                                             @RequestParam Boolean updateEndDate) {
        if (!studentService.updateStudentStudyState(id, studyState, updateEndDate)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功");
    }

}
