package com.fmock.demo.service;

import com.fmock.demo.dto.UserAllInfoDto;
import com.fmock.demo.dto.UserSimpleInfoDto;

import java.util.List;

/**
 * @Author zhenhuaixiu
 * @Date 2022/5/11 10:55
 * @Version 1.0
 */
public interface IUserService {
    public List<UserSimpleInfoDto> getUserDetailSimple(Integer userId);
    public List<UserAllInfoDto> getUserDetail(Integer userId);
}
