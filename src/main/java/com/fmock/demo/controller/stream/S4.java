package com.fmock.demo.controller.stream;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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


    // 我们把Stream提供的操作分为两类，转换操作和聚合操作。
    @Test
    public void func3() {

        // 排序，sorted()方法    注意sorted()只是一个转换操作，它会返回一个新的Stream
        List<String> list = List.of("aaa", "vv", "ss", "eee")
                .stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(list);




        // 去重, distinct()
        List<String> list2 = List.of("aaa", "vv", "ss", "aaa", "ss", "aa")
                .stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println(list2);



        // 截取    skip()用于跳过当前Stream的前N个元素，limit()用于截取当前Stream最多前N个元素
        List.of("A", "B", "C", "D", "E", "F")
                .stream()
                .skip(2)  // 跳过A, B
                .limit(3) // 截取C, D, E
                .collect(Collectors.toList()); // [C, D, E]




        // 合并    使用Stream的静态方法concat()
        Stream<String> s1 = List.of("A", "B", "C").stream();
        Stream<String> s2 = List.of("D", "E").stream();
        Stream<String> s = Stream.concat(s1, s2);
        System.out.println(s.collect(Collectors.toList())); // [A, B, C, D, E]




        // flatMap
        Stream<List<Integer>> sl = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));
        // 这里是stream<List>，我们想脱一层结构，即变成Stream<Integer>,就可以使用flatMap()
        Stream<Integer> si = sl.flatMap(listtt -> listtt.stream());
        si.forEach(System.out::println);




        // 其他聚合方法
        // 除了reduce()和collect()外，Stream还有一些常用的聚合方法
        // count()：用于返回元素个数；
        // max(Comparator<? super T> cp)：找出最大元素；
        // min(Comparator<? super T> cp)：找出最小元素。
        //System.out.println(si.count());

        // 最后一个常用的方法是forEach()，它可以循环处理Stream的每个元素，我们经常传入System.out::println来打印Stream的元素：
        //
        Stream<String> ss = Stream.of("aaa","vvv","aasdf");
        ss.forEach(str -> {
            System.out.println("Hello, " + str);
        });
    }

    // 小结
    // Stream提供的常用操作有：
    //
    // 转换操作：map()，filter()，sorted()，distinct()；
    //
    // 合并操作：concat()，flatMap()；
    //
    // 并行处理：parallel()；
    //
    // 聚合操作：reduce()，collect()，count()，max()，min()，sum()，average()；
    //
    // 其他操作：allMatch(), anyMatch(), forEach()。
}
