package com.bms.controller.system;

import com.bms.dto.ResultDto;
import com.bms.entity.Campus;
import com.bms.service.CampusService;
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
@RequestMapping("/system/campus")
public class SystemCampusController {
    private final CampusService campusService;

    @PostMapping
    public ResultDto addCampus(@RequestBody @Valid Campus campus, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!campusService.addCampus(campus)) {
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }

    @GetMapping
    public ResultDto getAllCampus() {
        return ResultDto.success().setObj(campusService.getAllCampus());
    }

    @PutMapping
    public ResultDto updateCampus(@RequestBody Campus campus) {
        if (!campusService.updateCampus(campus)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @DeleteMapping("/{id}")

    public ResultDto removeCampus(@PathVariable Long id) {
        if (!campusService.removeCampus(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
}
