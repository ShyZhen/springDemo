package com.fmock.demo.controller.design;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @Author zhenhuaixiu
 * @Date 2022/12/15 15:36
 * @Version 1.0
 */

// 工厂模式调用示例
public class D1 {

    @Test
    public void d1() {
        NumberFactory numberFactory = NumberFactory.getInstance();

        Number res = numberFactory.parse("235");
        System.out.println(res);
    }

}




// 先定义个抽象的工厂接口,解析字符串到Number的Factory
interface NumberFactory {

    // 创建方法
    public Number parse(String s);

    // 获取工厂实例
    static NumberFactory getInstance() {
        return impl;
    }

    static NumberFactoryImpl impl = new NumberFactoryImpl();

}

// 有了工厂接口，再编写一个实现类
class NumberFactoryImpl implements NumberFactory {
//    // 支持浮点数
//    public Number parse(String s) {
//        return new BigDecimal(s);
//    }

    public Number parse(String s) {
        return Integer.valueOf(s);
    }
}


