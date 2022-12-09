package com.fmock.demo.controller.stream;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Author zhenhuaixiu
 * @Date 2022/12/7 17:24
 * @Version 1.0
 */
public class S4 {
    // 使用reduce

    @Test
    public void test1() {
        // map()和filter()都是Stream的转换方法
        // 而Stream.reduce()则是Stream的一个聚合方法，它可以把一个Stream的所有元素按照聚合函数聚合成一个结果

        int initVal = 100;
        int sum = Stream.of(1,2,3,4,5,6,7,8,9).reduce(initVal, (res, b) -> res + b);
        int sum2 = Stream.of(1,2,3,4,5,6,7,8,9).reduce(initVal, Integer::sum);
        System.out.println(sum +"----"+ sum2);

        // res 是后面算法聚合出来的，初始res就是0，b则依次为1~9
        int sum3 = Stream.of(1,2,3,4,5,6,7,8,9).reduce(1, (res, b) -> res * b);  // 注意：因为是乘法，如果初始值是0的话，0乘以任何值都是0，则此处需要设置为1
        System.out.println(sum3);
    }

    // 灵活运用reduce()也可以对Java对象进行操作
    // 下面的代码演示了如何将配置文件的每一行配置通过map()和reduce()操作聚合成一个Map<String, String>
    @Test
    public void test2() {
        List<String> props = List.of("aa=1", "bb=2", "cc=3", "dd=4");

        Map<String, String> map = props.stream().map(kv -> {
            String[] ss = kv.split("\\=", 2);
            return Map.of(ss[0], ss[1]);
        }).reduce(new HashMap<String, String>(), (m, kv2) -> {
            m.putAll(kv2);
            return m;
        });

        map.forEach((k, v) -> {
            System.out.println(k+"---"+v);
        });

//        aa---1
//        bb---2
//        cc---3
//        dd---4
    }
}
