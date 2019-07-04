package com.bms.service;

import com.bms.dto.EmployeeDto;
import com.bms.entity.Employee;
import com.bms.entity.Nation;
import com.bms.entity.PoliticsStatus;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:24
 */
public interface EmployeeService {
    /**
     * 查询所有的民族
     * @return {@link Nation}
     */
    List<Nation> getAllNations();

    /**
     * 查询所有的政治面貌
     * @return {@link PoliticsStatus}
     */
    List<PoliticsStatus> getAllPolitics();

    /**
     * 新增雇员
     * @param employees {@link Employee}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addEmployees(List<Employee> employees);

    /**
     * 查询所有雇员
     * @return {@link Employee}
     */
    List<Employee> getAllEmployees();

    /**
     * 根据指定条件查询{@link Employee}
     * @param employeeDto 待查询条件对象
     * @return 结果集
     */
    Map<String, Object> getEmployees(EmployeeDto employeeDto);

    /**
     * 根据雇员id删除雇员
     * @param ids id组成的字符串，形如 1,2,3,4
     * @return {@code true}成功 {@code false}失败
     */
    boolean removeEmployees(String ids);

    /**
     * 更新雇员信息
     * @param employee {@link Employee}
     * @return {@code true}成功 {@code false}失败
     */
    boolean updateEmployee(Employee employee);
    /**
     * 新增雇员
     * @param employee {@link Employee}
     * @return {@code true}成功 {@code false}失败
     */
    boolean addEmployee(Employee employee);
}
