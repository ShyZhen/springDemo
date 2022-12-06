package com.fmock.demo.controller.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author zhenhuaixiu
 * @Date 2022/11/24 14:53
 * @Version 1.0
 */
public class S2 {

    // 所谓map操作，就是把一种操作运算，映射到一个序列的每一个元素上
    // 使用Stream.map()，把一个Stream转换为另一个Stream
    // map操作，把一个Stream的每个元素一一对应到应用了目标函数的结果上
    @Test
    public void func1() {
        Stream<Integer> stream1 = Stream.generate(new someIntNatural());

        Stream<Integer> stream2 = stream1.map(res -> res * 2);    // 这里使用了stream1，所有流只能使用一次，导致下面无法使用或打印

        //stream1.limit(10).forEach(System.out::println);         // 无法打印，所有流只能使用一次
        stream2.limit(10).forEach(System.out::println);
    }



    @Test
    public void func2() {
        // 利用map，能完成任何操作元素操作
        Stream<String> stream1 = Stream.of("apPLE ", "    aA  FFf  ", " lkj ");

        Stream<String> stream2 = stream1.map(String::trim)           // 去空格
                                        .map(String::toLowerCase);   // 小写

        stream2.forEach(System.out::println);
    }
}



class someIntNatural implements Supplier<Integer> {
    Integer n = 0;

    @Override
    public Integer get() {
        n++;
        return n;
    }
}
