package com.fmock.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author zhenhuaixiu
 * @Date 2022/5/10 17:21
 * @Version 1.0
 */
@Data
public class Users {
    private Integer id;

    private String name;

    private String email;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
