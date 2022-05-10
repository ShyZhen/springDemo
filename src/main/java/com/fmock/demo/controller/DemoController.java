package com.fmock.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhenhuaixiu
 * @Date 2022/4/29 15:17
 * @Version 1.0
 */

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    public void init() {}

    @GetMapping(value = "/test", produces = "application/json")
    public void test() {
        System.out.println("sdflkjsdfklj");
    }
}
