package com.fmock.demo.controller.stream;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

/**
 * @Author zhenhuaixiu
 * @Date 2022/11/11 10:34
 * @Version 1.0
 */
public class S1 {

    // Stream

    // Java从8开始，不但引入了Lambda表达式，还引入了一个全新的流式API：Stream API。它位于java.util.stream包中。
    // 这个Stream不同于java.io的InputStream和OutputStream，它代表的是任意Java对象的序列。两者对比如下
    //
    //              java.io                java.util.stream
    //存储     顺序读写的byte或char         顺序输出的任意Java对象实例
    //用途     序列化至文件或网络            内存计算／业务逻辑


    // 一个顺序输出的java对象序列，不就是一个list容器么？
    // 其实，stream 和 list 也不一样，list存储的每个元素都已经存储在内存中，而stream输出的元素可能没有预先存储在内存中，而是实时计算的。
    // 也就是说，list的用途是操作一组已存在的java对象，而stream实现的是惰性计算。对比如下
    //
    //             java.util.List                  java.util.stream
    //元素          已分配并存储在内存                  可能未分配，实时计算
    //用途          操作一组已存在的Java对象            惰性计算
}


class S2 {
    // 创建Stream

    // 方法一：使用Stream.of()
    @Test
    public void func1() {
        Stream<String> stream = Stream.of("aa", "ss", "c", "dfasdf");

//        String[] strArr = new String[]{"SF","C","A"};
//        for (String s: strArr) {
//            System.out.println(s);
//        }

        stream.forEach(System.out::println);
    }

}
