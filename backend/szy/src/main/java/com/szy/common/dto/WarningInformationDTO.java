package com.szy.common.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
public class WarningInformationDTO implements Serializable {
    @NotBlank(message = "当前页码不能为空")
    Integer currentPage;

    @NotBlank(message = "每页数量不能为空")
    Integer pageSize;

    String level;

    String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;
}
