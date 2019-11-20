package com.bms.util;

import com.bms.entity.Student;
import com.bms.entity.Subject;
import com.bms.entity.enu.StudentEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.invoker.Invoker;
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
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author 咸鱼
 * @date 2019-06-19 20:57
 */
@Slf4j
public final class PoiUtils {
    private static final short DATE_FORMAT = HSSFDataFormat.getBuiltinFormat("m/d/yy");
    private static final int[] COLUMN_WIDTH = {12, 10, 5, 5, 12, 15, 25, 10, 12, 15, 15, 10, 15};
    private static final String[] COLUMN_NAMES = {"姓名", "学号", "年龄", "性别", "家长姓名",
            "家长电话", "报名科目", "报名价格", "报名日期", "开始上课日期",
            "结束上课日期", "学习状态", "暂停上课日期"};
    private static Map<String, Boolean> STUDY_STATE_MAP = new HashMap<>();
    private static final String SHEET_NAME = "学生信息表.xls";
    private static Map<Long, String> subjectIdMap;
    private static Map<String, Long> subjectNameMap;
    private static ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    static {
        STUDY_STATE_MAP.put("正在学习", Boolean.TRUE);
        STUDY_STATE_MAP.put("暂停学习", Boolean.FALSE);
    }
    /**
     * 将上传的excel中的数据转换成{@link Student}集合
     * @param file 上传的文件对象
     * @param subjects 科目集合
     * @return {@link Student}集合
     */
    public static List<Student> importEmp2List(MultipartFile file, List<Subject> subjects) {
        translateSubjects2Map(subjects);

        Reflector studentReflector = reflectorFactory.findForClass(Student.class);

        List<Student> students = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                Student student;
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
                    student = new Student();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        HSSFCell cell = row.getCell(k);
                        String propertyName = StudentEnum.getStudentEnum(k).getEnName();
                        if (studentReflector.hasSetter(propertyName)) {
                            Invoker invoker = studentReflector.getSetInvoker(propertyName);
                            Class<?> setterType = studentReflector.getSetterType(propertyName);
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    String cellValue = cell.getStringCellValue();
                                    if (StringUtils.isNoneBlank(cellValue)) {
                                        if (Integer.class.equals(setterType)) {
                                            //todo：研究研究类型强转
                                            invoker.invoke(student, new Object[]{Integer.valueOf(cellValue)});
                                        } else {
                                            if ("subjectIds".equals(propertyName)) {
                                                List<Long> subjectIds = translateSubjectNames2Id(cellValue);
                                                invoker.invoke(student, new Object[]{subjectIds == null ? null : subjectIds.toArray(new Long[subjectIds.size()])});
                                            } else if ("studyState".equals(propertyName)) {
                                                invoker.invoke(student, new Object[]{STUDY_STATE_MAP.get(cellValue)});
                                            } else {
                                                invoker.invoke(student, new Object[]{cellValue});
                                            }
                                        }
                                    }
                                    break;
                                default:
                                    if (cell.getDateCellValue() != null) {
                                        invoker.invoke(student, new Object[]{CommonUtils.date2LocalDate(cell.getDateCellValue())});
                                    }
                                    break;
                            }

                        }
                    }
                    students.add(student);
                }
            }
        } catch (IOException e) {
            log.error("读取文件报错：", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return students;
    }

    private static List<Long> translateSubjectNames2Id(String cellValue) {
        List<Long> subjectIds = null;
        if (StringUtils.isNoneBlank(cellValue)) {
            subjectIds = new ArrayList<>();
            if (cellValue.contains(FinalName.SUBJECT_NAME_SEPARATOR)) {
                String[] nameArray = cellValue.split(FinalName.SUBJECT_NAME_SEPARATOR);
                for (String subjectName : nameArray) {
                    subjectIds.add(subjectNameMap.get(subjectName));
                }
            } else {
                subjectIds.add(subjectNameMap.get(cellValue));
            }
        }
        return subjectIds;
    }

    private static void translateSubjects2Map(List<Subject> subjects) {
        if (subjectNameMap == null) {
            synchronized (PoiUtils.class) {
                if (subjectNameMap == null) {
                    subjectNameMap = new HashMap<>(subjects.size());
                    subjects.forEach(subject -> subjectNameMap.put(subject.getName(), subject.getId()));
                }
            }
        }
    }

    /**
     * 将所有{@link Student}导入excel
     * @param students 所有{@link Student}
     * @param subjects 所有{@link Subject}
     * @return 字节数组对象
     */
    public static ResponseEntity<byte[]> exportEmp2Excel(List<Student> students, List<Subject> subjects) {
        HttpHeaders headers;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 2.创建文档摘要
            workbook.createInformationProperties();
            // 3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
            // 3.1文档类别
            dsi.setCategory("学生信息");
            // 3.2设置文档管理员
            dsi.setManager("TM");
            // 3.3设置组织机构
            dsi.setCompany("吹牛逼集团");
            // 4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            // 4.1设置文档主题
            si.setSubject("学生信息表");
            // 4.2设置文档标题
            si.setTitle("学生信息");
            // 4.3设置文档作者
            si.setAuthor("TM");
            // 4.4设置文档备注
            si.setComments("备注信息暂无");
            // 5.创建Excel表单
            HSSFSheet sheet = workbook.createSheet("学生信息表");
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
            loadData(sheet, students, subjects, dateCellStyle);

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
     * @param students {@link Student}
     * @param dateCellStyle 表格样式
     */
    private static void loadData(HSSFSheet sheet, List<Student> students, List<Subject> subjects, HSSFCellStyle dateCellStyle) {
        if (!CollectionUtils.isEmpty(students)) {
            for (int i = 0; i < students.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Student student = students.get(i);
                row.createCell(0).setCellValue(student.getName() == null ? "" : student.getName());
                row.createCell(1).setCellValue(student.getIdCard() == null ? "" : student.getIdCard());
                row.createCell(2).setCellValue(student.getAge() == null ? "" : String.valueOf(student.getAge()));
                row.createCell(3).setCellValue(student.getGender() == null ? "" : student.getGender());
                row.createCell(4).setCellValue(student.getParentName() == null ? "" : student.getParentName());
                row.createCell(5).setCellValue(student.getParentPhone() == null ? "" : student.getParentPhone());
                row.createCell(6).setCellValue(student.getSubjectIds() == null ? "" : getSubjectNames(student.getSubjectIds(), subjects));
                row.createCell(7).setCellValue(student.getRegisterPrice() == null ? "" : String.valueOf(student.getRegisterPrice()));

                HSSFCell registerDateCell = row.createCell(8);
                setDateFormatData(registerDateCell, CommonUtils.localDate2Date(student.getRegisterDate()) , dateCellStyle);


                HSSFCell beginDateCell = row.createCell(9);
                setDateFormatData(beginDateCell, CommonUtils.localDate2Date(student.getBeginDate()), dateCellStyle);

                HSSFCell endDateCell = row.createCell(10);
                setDateFormatData(endDateCell, CommonUtils.localDate2Date(student.getEndDate()), dateCellStyle);

                row.createCell(11).setCellValue(student.getStudyState() == null ? "" : student.getStudyState() ? "正在学习" : "暂停学习");

                HSSFCell pauseDateCell = row.createCell(12);
                setDateFormatData(pauseDateCell, CommonUtils.localDate2Date(student.getPauseDate()), dateCellStyle);
            }
        }
    }

    private static String getSubjectNames(Long[] subjectIds, List<Subject> subjects) {
        if (subjectIdMap == null) {
            synchronized (PoiUtils.class) {
                if (subjectIdMap == null) {
                    subjectIdMap = new HashMap<>(subjects.size());
                    subjects.forEach(subject -> subjectIdMap.put(subject.getId(), subject.getName()));
                }
            }
        }
        StringBuilder result = new StringBuilder();
        for (Long subjectId : subjectIds) {
            result.append(subjectIdMap.get(subjectId)).append("/");
        }
        return result.substring(0, result.length() - 1);
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
