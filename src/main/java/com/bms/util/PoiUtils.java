package com.bms.util;

import com.bms.entity.Employee;
import com.bms.entity.Nation;
import com.bms.entity.PoliticsStatus;
import com.bms.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author 咸鱼
 * @date 2019-06-19 20:57
 */
@Slf4j
public final class PoiUtils {
    private static final short DATE_FORMAT = HSSFDataFormat.getBuiltinFormat("m/d/yy");
    private static final int[] COLUMN_WIDTH = {5, 12, 10, 5, 12, 20, 5, 10, 16, 12, 20, 15, 20, 10, 10,
            20, 20, 12, 12, 12, 12, 12, 12, 12, 5};
    private static final String[] COLUMN_NAMES = {"编号", "姓名", "工号", "性别", "出生日期",
            "身份证号", "婚姻状况", "民族", "籍贯", "政治面貌",
            "邮箱", "电话号码", "联系地址", "聘用形式", "最高学历",
            "专业", "毕业院校", "入职日期", "在职状态",  "合同期限(年)",
            "转正日期", "离职日期", "合同起始日期", "合同终止日期", "工龄"};
    private static final String SHEET_NAME = "员工表.xls";
    /**
     * 将上传的excel中的数据转换成{@link Employee}集合
     * @param file 上传的文件对象
     * @param allNations 民族集合
     * @param allPolitics 政治面貌集合
     * @return {@link Employee}集合
     */
    public static List<Employee> importEmp2List(MultipartFile file, List<Nation> allNations, List<PoliticsStatus> allPolitics) {
        Map<String, Integer> nationsMap = translateNations2Map(allNations);
        Map<String, Integer> politicsMap = translatePolitics2Map(allPolitics);
        List<Employee> employees = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Employee employee;
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    if (j == 0) {
                        // 标题行
                        continue;
                    }
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        // 没有数据
                        continue;
                    }
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    employee = new Employee();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellTypeEnum()) {
                            case STRING: {
                                String cellValue = cell.getStringCellValue();
                                if (cellValue == null) {
                                    cellValue = "";
                                }
                                switch (k) {
                                    case 1:
                                        employee.setName(cellValue);
                                        break;
                                    case 2:
                                        employee.setWorkId(cellValue);
                                        break;
                                    case 3:
                                        employee.setGender(cellValue);
                                        break;
                                    case 5:
                                        employee.setIdCard(cellValue);
                                        break;
                                    case 6:
                                        employee.setWedlock(cellValue);
                                        break;
                                    case 7:
                                        employee.setNationId(nationsMap.get(cellValue));
                                        break;
                                    case 8:
                                        employee.setNativePlace(cellValue);
                                        break;
                                    case 9:
                                        employee.setPoliticId(politicsMap.get(cellValue));
                                        break;
                                    case 10:
                                        employee.setEmail(cellValue);
                                        break;
                                    case 11:
                                        employee.setPhone(cellValue);
                                        break;
                                    case 12:
                                        employee.setAddress(cellValue);
                                        break;
                                    case 13:
                                        employee.setEngageForm(cellValue);
                                        break;
                                    case 14:
                                        employee.setTipTopDegree(cellValue);
                                        break;
                                    case 15:
                                        employee.setSpecialty(cellValue);
                                        break;
                                    case 16:
                                        employee.setSchool(cellValue);
                                        break;
                                    case 18:
                                        employee.setWorkState(cellValue);
                                        break;
                                    default:
                                        break;
                                }
                            }
                                break;
                            default:
                            {
                                switch (k) {
                                    case 4:
                                        employee.setBirthday(cell.getDateCellValue());
                                        break;
                                    case 17:
                                        employee.setBeginDate(cell.getDateCellValue());
                                        break;
                                    case 19:
                                        employee.setContractTerm(cell.getNumericCellValue());
                                        break;
                                    case 20:
                                        employee.setConversionTime(cell.getDateCellValue());
                                        break;
                                    case 21:
                                        employee.setNotWorkDate(cell.getDateCellValue());
                                        break;
                                    case 22:
                                        employee.setBeginContract(cell.getDateCellValue());
                                        break;
                                    case 23:
                                        employee.setEndContract(cell.getDateCellValue());
                                        break;
                                    case 24:
                                        employee.setWorkAge((int) cell.getNumericCellValue());
                                        break;
                                    default:
                                        break;
                                }
                            }
                                break;
                        }
                    }
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            log.error("读取文件报错：", e);
        }
        return employees;
    }
    private static Map<String, Integer> translateNations2Map(List<Nation> nations) {
        if (CollectionUtils.isEmpty(nations)) {
            throw new BaseException("未查询到任何民族数据！");
        }
        Map<String, Integer> map = new HashMap<>(nations.size());
        nations.forEach(nation -> map.put(nation.getName(), nation.getId()));
        return map;
    }
    private static Map<String, Integer> translatePolitics2Map(List<PoliticsStatus> politics) {
        if (CollectionUtils.isEmpty(politics)) {
            throw new BaseException("未查询到任何政治面貌数据！");
        }
        Map<String, Integer> map = new HashMap<>(politics.size());
        politics.forEach(politicsStatus -> map.put(politicsStatus.getName(), politicsStatus.getId()));
        return map;
    }

    /**
     * 将所有{@link Employee}导入excel
     * @param employees 所有{@link Employee}
     * @return 字节数组对象
     */
    public static ResponseEntity<byte[]> exportEmp2Excel(List<Employee> employees) {
        HttpHeaders headers;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 2.创建文档摘要
            workbook.createInformationProperties();
            // 3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
            // 3.1文档类别
            dsi.setCategory("员工信息");
            // 3.2设置文档管理员
            dsi.setManager("咸鱼");
            // 3.3设置组织机构
            dsi.setCompany("吹牛逼集团");
            // 4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            // 4.1设置文档主题
            si.setSubject("员工信息表");
            // 4.2设置文档标题
            si.setTitle("员工信息");
            // 4.3设置文档作者
            si.setAuthor("咸鱼");
            // 4.4设置文档备注
            si.setComments("备注信息暂无");
            // 5.创建Excel表单
            HSSFSheet sheet = workbook.createSheet("员工信息表");
            // 6.全局设置
            // 6.1设置日期显示格式
            HSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(DATE_FORMAT);
            // 6.2创建标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // 7.定义列的宽度
            setColumnWidth(sheet, COLUMN_WIDTH);
            // 8.设置表头
            createHeaderRow(sheet, COLUMN_NAMES, headerStyle);

            // 9.装载数据
            loadData(sheet, employees, dateCellStyle);

            // 10.返回
            headers = new HttpHeaders();
            // 设置响应文件（解决文件名中文乱码问题）
            headers.setContentDispositionFormData("attachment",
                    new String(SHEET_NAME.getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置表单每一列的宽度
     * @param sheet excel表单
     * @param columnWidth 宽度数组
     */
    private static void setColumnWidth(HSSFSheet sheet, int[] columnWidth) {
        for (int i = 0; i < columnWidth.length; i++) {
            sheet.setColumnWidth(i, columnWidth[i]*256);
        }
    }

    /**
     * 创建表头
     * @param sheet excel表单
     * @param columnNames 列名数组
     * @param headerStyle 标题的显示样式
     */
    private static void createHeaderRow(HSSFSheet sheet, String[] columnNames, HSSFCellStyle headerStyle) {
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 装载数据
     * @param sheet excel表单
     * @param employees {@link Employee}
     * @param dateCellStyle 表格样式
     */
    private static void loadData(HSSFSheet sheet, List<Employee> employees, HSSFCellStyle dateCellStyle) {
        if (!CollectionUtils.isEmpty(employees)) {
            for (int i = 0; i < employees.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Employee employee = employees.get(i);
                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getName() == null ? "" : employee.getName());
                row.createCell(2).setCellValue(employee.getWorkId() == null ? "" : employee.getWorkId());
                row.createCell(3).setCellValue(employee.getGender() == null ? "" : employee.getGender());

                HSSFCell birthdayCell = row.createCell(4);
                setDateFormatData(birthdayCell, employee.getBirthday() , dateCellStyle);

                row.createCell(5).setCellValue(employee.getIdCard() == null ? "" : employee.getIdCard());
                row.createCell(6).setCellValue(employee.getWedlock() == null ? "" : employee.getWedlock());
                row.createCell(7).setCellValue(employee.getNation().getName() == null ? "" : employee.getNation().getName());
                row.createCell(8).setCellValue(employee.getNativePlace() == null ? "" : employee.getNativePlace());
                row.createCell(9).setCellValue(employee.getPoliticsStatus().getName() == null ? "" : employee.getPoliticsStatus().getName());
                row.createCell(10).setCellValue(employee.getEmail() == null ? "" : employee.getEmail());
                row.createCell(11).setCellValue(employee.getPhone() == null ? "" : employee.getPhone());
                row.createCell(12).setCellValue(employee.getAddress() == null ? "" : employee.getAddress());
                row.createCell(13).setCellValue(employee.getEngageForm() == null ? "" : employee.getEngageForm());
                row.createCell(14).setCellValue(employee.getTipTopDegree() == null ? "" : employee.getTipTopDegree());
                row.createCell(15).setCellValue(employee.getSpecialty() == null ? "" : employee.getSpecialty());
                row.createCell(16).setCellValue(employee.getSchool() == null ? "" : employee.getSchool());

                HSSFCell beginDateCell = row.createCell(17);
                setDateFormatData(beginDateCell, employee.getBeginDate(), dateCellStyle);

                row.createCell(18).setCellValue(employee.getWorkState() == null ? "" : employee.getWorkState());
                row.createCell(19).setCellValue(employee.getContractTerm() == null ? -1 : employee.getContractTerm());

                HSSFCell conversionTimeCell = row.createCell(20);
                setDateFormatData(conversionTimeCell, employee.getConversionTime(), dateCellStyle);
                HSSFCell notWorkDateCell = row.createCell(21);
                setDateFormatData(notWorkDateCell, employee.getNotWorkDate(), dateCellStyle);
                HSSFCell beginContractCell = row.createCell(22);
                setDateFormatData(beginContractCell, employee.getBeginContract(), dateCellStyle);
                HSSFCell endContractCell = row.createCell(23);
                setDateFormatData(endContractCell, employee.getEndContract(), dateCellStyle);

                row.createCell(24).setCellValue(employee.getWorkAge() == null ? -1 : employee.getWorkAge());
            }
        }
    }

    /**
     * 设置日期格式的表格样式
     * @param cell 表格
     * @param date 日期
     * @param dateCellStyle 日期样式
     */
    private static void setDateFormatData(HSSFCell cell, Date date, HSSFCellStyle dateCellStyle) {
        if (date == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(date);
        }
        cell.setCellStyle(dateCellStyle);
    }
}
