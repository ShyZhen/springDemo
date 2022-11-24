package com.fmock.demo.controller.stream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
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
    // 虽然这种方式基本上没啥实质性用途，但测试的时候很方便。
    @Test
    public void func1() {
        Stream<String> stream = Stream.of("aa", "ss", "c", "dfasdf");

//        String[] strArr = new String[]{"SF","C","A"};
//        for (String s: strArr) {
//            System.out.println(s);
//        }

        // 只能使用stream特有的foreach遍历
        stream.forEach(System.out::println);
    }

    // 方法二：基于数组或Collection
    // 基于一个数组或者Collection，这样该stream输出的元素就是数组或者collection持有的元素
    @Test
    public void func2() {

        // 把数组变成Stream使用Arrays.stream()方法。对于Collection（List、Set、Queue等），直接调用stream()方法就可以获得Stream。
        String[] strings = new String[] {"saa", "svv", "scc", "spp"};
        Stream<String> stream1 = Arrays.stream(strings);

        List<String> list = new ArrayList<>();
        list.add("la");
        list.add("lb");
        list.add("lc");
        Stream<String> stream2 = list.stream();


        // 遍历输出
        // 可以直接使用Lambert表达式：stream1.forEach(System.out::println);
        stream1.forEach(s -> {
            System.out.println(s);
        });

        stream2.forEach(str -> {
            System.out.println(str);
        });
    }



    // 方法三 基于Supplier
    // 上述创建Stream的方法都是把一个现有的序列变为Stream，它的元素是固定的。
    // 创建Stream还可以通过Stream.generate()方法，它需要传入一个Supplier对象
    // 基于Supplier创建的Stream会不断调用Supplier.get()方法来不断产生下一个元素，这种Stream保存的不是元素，而是算法，它可以用来表示无限序列
    @Test
    public void func3() {
        // 编写一个能不断生成自然数的Supplier，它的代码非常简单，每次调用get()方法，就生成下一个自然数
        Stream<Integer> natualStream = Stream.generate(new NatualSupplier());
        // 注意：无限序列必须先变成有限序列再打印:
        // 用limit()方法可以截取前面若干个元素，这样就变成了一个有限序列
        natualStream.limit(20).forEach(System.out::println);
    }



    // 其他方法
    // 通过一些api提供的接口，直接获得stream
    @SneakyThrows
    @Test
    public void func4() {
        // 例如 Files类的lines()方法可以把一个文件变成一个Stream，每个元素代表文件的一行内容
        // 此方法对于按行遍历文本文件十分有用
        Path path = Paths.get("C:\\Users\\DELL\\Downloads\\banner.sql");
        Stream<String> lines = Files.lines(path);
        lines.forEach(System.out::println);


        // 正则表达式的Pattern对象有一个splitAsStream()方法，可以直接把一个长字符串分割成Stream序列而不是数组
        Pattern p = Pattern.compile("\\s+");
        Stream<String> s = p.splitAsStream("The quick brown fox jumps over the lazy dog");
        s.forEach(System.out::println);
    }

}

// 如果用List表示，即便在int范围内，也会占用巨大的内存，而Stream几乎不占用空间，因为每个元素都是实时计算出来的，用的时候再算。
class NatualSupplier implements Supplier<Integer> {
    Integer n = 0;

    @Override
    public Integer get() {
        n++;
        return n;
    }
}
