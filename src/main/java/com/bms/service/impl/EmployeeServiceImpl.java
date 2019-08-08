package com.bms.service.impl;

import com.bms.dao.EmployeeDao;
import com.bms.dto.EmployeeDto;
import com.bms.entity.Employee;
import com.bms.entity.Nation;
import com.bms.entity.PoliticsStatus;
import com.bms.exception.BaseException;
import com.bms.service.EmployeeService;
import com.bms.util.FinalName;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 咸鱼
 * @date 2019-06-19 21:24
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = RuntimeException.class)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;

    @Override
    public List<Nation> getAllNations() {
        return employeeDao.getAllNations();
    }

    @Override
    public List<PoliticsStatus> getAllPolitics() {
        return employeeDao.getAllPolitics();
    }

    @Override
    public boolean addEmployees(List<Employee> employees) {
        return employeeDao.insertEmployees(employees) == employees.size();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.selectAllEmployees();
    }

    @Override
    public Map<String, Object> getEmployees(EmployeeDto employeeDto) {
        // 设置默认起始页和每页数量
        if (employeeDto.getPageNum() == null) {
            employeeDto.setPageNum(1);
        }
        if (employeeDto.getPageSize() == null) {
            employeeDto.setPageSize(10);
        }
        String beginDateScope = employeeDto.getBeginDateScope();
        if (beginDateScope != null && beginDateScope.contains(FinalName.SEPARATOR)) {
            String[] dates = beginDateScope.split(FinalName.SEPARATOR);
            employeeDto.setStartBeginDate(LocalDate.parse(dates[0], FinalName.DATE_TIME_FORMATTER));
            employeeDto.setEndBeginDate(LocalDate.parse(dates[1], FinalName.DATE_TIME_FORMATTER));
        }
        PageHelper.startPage(employeeDto.getPageNum(), employeeDto.getPageSize());
        List<Employee> employees = employeeDao.getEmployees(employeeDto);

        PageInfo<Employee> employeePageInfo = new PageInfo<>(employees);
        // 复制集合
        ArrayList<Employee> copyEmployees = new ArrayList<>(employees);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", employeePageInfo.getTotal());
        copyEmployees.forEach(this::calculateWorkAge);
        resultMap.put("employees", copyEmployees);
        return resultMap;
    }

    @Override
    public boolean removeEmployees(String ids) {
        if (StringUtils.isEmpty(ids.trim())) {
            throw new BaseException("前端参数有误！");
        }
        String[] arrIds = ids.split(FinalName.SEPARATOR);
        if (employeeDao.deleteEmployees(arrIds) == arrIds.length) {
            return true;
        }
        // 如果删除数量和id数量不等，则说明前端参数有误，应触发事务回滚，否则会删除一部分数据
        throw new RuntimeException();
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        Employee emp = new Employee();
        emp.setId(employee.getId());
        Employee employeeInDb= selectEmployee(emp);
        if (employeeInDb == null) {
            throw new BaseException("不存在该用户！");
        }
        if (!employeeInDb.getBeginContract().equals(employee.getBeginContract()) ||
                !employeeInDb.getEndContract().equals(employee.getEndContract())) {
            // 要更新合同期限
            calculateContractTerm(employee);
        }
        return employeeDao.updateEmployee(employee) == 1;
    }

    @Override
    public boolean addEmployee(Employee employee) {
        Employee emp = new Employee();
        // 由同姓名的，但是身份证号肯定唯一
        emp.setIdCard(employee.getIdCard());
        Employee employeeInDb= selectEmployee(emp);
        if (employeeInDb != null) {
            throw new BaseException("该用户已存在！");
        }
        // 生成工号，用0补足8位
        int affectedRow;
        synchronized (this) {
            employee.setWorkId(String.format("%08d", Integer.parseInt(employeeDao.selectMaxId()) + 1));
            calculateContractTerm(employee);
            affectedRow = employeeDao.insertEmployee(employee);
        }
        // 生成合同期限
        return affectedRow == 1;
    }

    /**
     * 计算合同期限
     */
    private void calculateContractTerm(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        // 保留小数点后两位
        employee.setContractTerm(Double.parseDouble(FinalName.DECIMAL_FORMAT.format((endContract.getTime()
                - beginContract.getTime()) / FinalName.YEAR_TO_MILLISECONDS)));
    }

    /**
     * 计算工龄
     */
    private void calculateWorkAge(Employee employee) {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        Calendar endContract = Calendar.getInstance();
        endContract.setTime(employee.getEndContract());
        long end;
        if (endContract.before(now)) {
            end = employee.getEndContract().getTime();
        } else {
            end = System.currentTimeMillis();
        }
        employee.setWorkAge((int) ((end - employee.getBeginContract().getTime()) / FinalName.YEAR_TO_MILLISECONDS));
    }
    
    private Employee selectEmployee(Employee employee) {
        return employeeDao.selectEmployee(employee);
    }

}
