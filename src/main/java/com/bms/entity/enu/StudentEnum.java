package com.bms.entity.enu;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 咸鱼
 * @date 2019-11-19 21:04
 */
public enum StudentEnum {
    NAME("name", "姓名"),
    ID_CARD("idCard", "学号"),
    AGE("age", "年龄"),
    GENDER("gender", "性别"),
    PARENT_NAME("parentName", "家长姓名"),
    PARENT_PHONE("parentPhone", "家长电话"),
    SUBJECT_IDS("subjectIds", "报名科目"),
    REGISTER_PRICE("registerPrice", "报名价格"),
    REGISTER_DATE("registerDate", "报名日期"),
    BEGIN_DATE("beginDate", "开始上课日期"),
    END_DATE("endDate", "结束上课日期"),
    STUDY_STATE("studyState", "学习状态"),
    PAUSE_DATE("pauseDate", "暂停上课日期");
    private String enName;
    private String zhName;
    private static Map<Integer, StudentEnum> indexMap  = new HashMap<>();

    static {
        for (int i = 0; i < StudentEnum.values().length; i++) {
            indexMap.put(StudentEnum.values()[i].ordinal(), StudentEnum.values()[i]);
        }
    }

    StudentEnum(String enName, String zhName) {
        this.enName = enName;
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public static StudentEnum getStudentEnum(Integer index) {
        return indexMap.get(index);
    }
}
