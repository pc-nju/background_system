package com.bms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 咸鱼
 * @date 2019-07-10 19:25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subject {
    private Long id;
    @NotBlank
    private String name;
    /**
     * 1课时对应多少时间
     */
    private Integer minutes;
    private Long parentId;
    private Date createTime;
}
