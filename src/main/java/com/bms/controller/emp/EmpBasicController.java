package com.bms.controller.emp;

import com.bms.dto.EmployeeDto;
import com.bms.dto.ResultDto;
import com.bms.entity.Employee;
import com.bms.service.EmployeeService;
import com.bms.util.PoiUtils;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-19 20:54
 */
@RestController
@RequestMapping("/employee/basic")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmpBasicController {
    private final EmployeeService employeeService;

    @GetMapping("/basicData")
    public ResultDto getBasicData() {
        Map<String, Object> map = new HashMap<>();
        map.put("nations", employeeService.getAllNations());
        map.put("politics", employeeService.getAllPolitics());
        return ResultDto.success().setObj(map);
    }
    @GetMapping("/emps")
    public ResultDto getEmployees(EmployeeDto employeeDto) {
        return ResultDto.success().setObj(employeeService.getEmployees(employeeDto));
    }

    @PostMapping("/importEmp")
    public ResultDto importEmployees(MultipartFile file) {
        List<Employee> employees = PoiUtils.importEmp2List(file, employeeService.getAllNations(),
                employeeService.getAllPolitics());
        if (!employeeService.addEmployees(employees)) {
            return ResultDto.error("导入失败！");
        }
        return ResultDto.success("导入成功！");
    }
    @GetMapping("/exportEmp")
    public ResponseEntity<byte[]> exportEmployees() {
        return PoiUtils.exportEmp2Excel(employeeService.getAllEmployees());
    }
    @DeleteMapping("/emps/{ids}")
    public ResultDto removeEmployees(@PathVariable String ids) {
        if (!employeeService.removeEmployees(ids)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
    @PutMapping("/emp")
    public ResultDto updateEmployee(@RequestBody @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!employeeService.updateEmployee(employee)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功");
    }
    @PostMapping("/emp")
    public ResultDto addEmployee(@RequestBody @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!employeeService.addEmployee(employee)){
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }
}
