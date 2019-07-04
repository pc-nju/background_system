package com.bms.dao;

import com.bms.dto.EmployeeDto;
import com.bms.entity.Employee;
import com.bms.entity.Nation;
import com.bms.entity.PoliticsStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:25
 */
public interface EmployeeDao {
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
     * @return 受影响的行数
     */
    int insertEmployees(@Param("employees") List<Employee> employees);
    /**
     * 查询所有雇员
     * @return {@link Employee}
     */
    List<Employee> selectAllEmployees();

    /**
     * 根据指定条件查询{@link Employee}
     * @param employeeDto 待查询条件对象
     * @return {@link Employee}
     */
    List<Employee> getEmployees(EmployeeDto employeeDto);

    /**
     * 根据雇员id删除雇员
     * @param arrIds 雇员id组成的数组
     * @return 受影响的行数
     */
    int deleteEmployees(@Param("ids") String[] arrIds);

    /**
     * 更新雇员信息
     * @param employee {@link Employee}
     * @return 受影响的行数
     */
    int updateEmployee(Employee employee);

    /**
     * 根据条件查询{@link Employee}
     * @param employee 待查询条件
     * @return {@link Employee}
     */
    Employee selectEmployee(Employee employee);

    /**
     * 查询当前数据库中记录的最大id
     * @return 最大id
     */
    String selectMaxId();

    /**
     * 插入{@link Employee}
     * @param employee {@link Employee}
     * @return 受影响的行数
     */
    int insertEmployee(Employee employee);
}
