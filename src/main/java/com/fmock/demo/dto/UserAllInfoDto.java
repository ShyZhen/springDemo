package com.fmock.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author zhenhuaixiu
 * @Date 2022/5/11 11:04
 * @Version 1.0
 */
@Data
public class UserAllInfoDto {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}
