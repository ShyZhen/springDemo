package com.fmock.demo.controller;

import com.fmock.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhenhuaixiu
 * @Date 2022/4/29 15:17
 * @Version 1.0
 */

@RestController
@RequestMapping("/demo")
public class DemoController {

    private IUserService iUserService;

    @Autowired
    public void init(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping(value = "/test", produces = "application/json")
    public String test() {
        return "hello";
    }

    @GetMapping(value = "/user/{type}", produces = "application/json")
    public List<? extends Object> getUserSimple(
            @RequestParam(value = "id", required = true) int id,          // get参数
            @PathVariable(value = "type", required = true) String type    // 路径参数
    ) {
        if (type.equals("all")) {
            return this.iUserService.getUserDetail(id);
        } else {
            return this.iUserService.getUserDetailSimple(id);
        }
    }
}
