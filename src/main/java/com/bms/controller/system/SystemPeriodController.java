package com.bms.controller.system;

import com.bms.dto.ResultDto;
import com.bms.entity.Period;
import com.bms.service.PeriodService;
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
@RequestMapping("/system/period")
public class SystemPeriodController {
    private final PeriodService periodService;

    @PostMapping
    public ResultDto addPeriod(@RequestBody @Valid Period period, BindingResult result) {
        if (result.hasErrors()) {
            return ResultDto.error("前端参数有误！");
        }
        if (!periodService.addPeriod(period)) {
            return ResultDto.error("新增失败！");
        }
        return ResultDto.success("新增成功！");
    }

    @GetMapping
    public ResultDto getAllPeriods() {
        return ResultDto.success().setObj(periodService.getAllPeriods());
    }

    @PutMapping
    public ResultDto updatePeriod(@RequestBody Period period) {
        if (!periodService.updatePeriod(period)) {
            return ResultDto.error("更新失败！");
        }
        return ResultDto.success("更新成功！");
    }

    @DeleteMapping("/{id}")

    public ResultDto removePeriod(@PathVariable Long id) {
        if (!periodService.removePeriod(id)) {
            return ResultDto.error("删除失败！");
        }
        return ResultDto.success("删除成功！");
    }
}
