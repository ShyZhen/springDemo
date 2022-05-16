package com.fmock.demo.service.impl;

import com.fmock.demo.dto.UserAllInfoDto;
import com.fmock.demo.dto.UserSimpleInfoDto;
import com.fmock.demo.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 需要添加 @Service 注释才可以在控制器里被注入
 * @Author zhenhuaixiu
 * @Date 2022/5/11 10:52
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public List<UserSimpleInfoDto> getUserDetailSimple(Integer userId) {

        UserSimpleInfoDto userSimpleInfoDto = new UserSimpleInfoDto();
        userSimpleInfoDto.setId(userId);
        userSimpleInfoDto.setName("模拟用户"+userId);

        List<UserSimpleInfoDto> list = new ArrayList<>();
        list.add(userSimpleInfoDto);

        return list;
    }

    @Override
    public List<UserAllInfoDto> getUserDetail(Integer userId) {

        UserAllInfoDto userAllInfoDto = new UserAllInfoDto();
        userAllInfoDto.setId(userId);
        userAllInfoDto.setName("模拟用户"+userId);
        userAllInfoDto.setEmail("user-"+userId+"@email.com");
        userAllInfoDto.setPhone("11111111111");
        userAllInfoDto.setCreatedAt(LocalDateTime.now());

        List<UserAllInfoDto> list = new ArrayList<>();
        list.add(userAllInfoDto);

        return list;
    }
}
