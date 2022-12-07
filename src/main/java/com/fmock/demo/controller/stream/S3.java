package com.fmock.demo.controller.stream;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @Author zhenhuaixiu
 * @Date 2022/12/6 17:23
 * @Version 1.0
 */
public class S3 {

    // Stream.filter()是Stream的另一个常用转换方法
    // 就是对Stream的所有元素一一进行测试，不满足就被过滤掉，用剩下满足条件的元素构成了一个新的Stream
    @Test
    public void test1() {
        Stream<String> stream1 = Stream.of("aaa", "bb", "cc", "aaafsd", "vaser");

        Stream<String> s2 = stream1.filter(str -> str.length() <= 3);
        s2.forEach(System.out::println);
    }

}


class MainTest {

    public static void main(String[] args) {
        List<Person> persons = List.of(new Person("小明", 88), new Person("小黑", 62), new Person("小白", 45),
                new Person("小黄", 78), new Person("小红", 99), new Person("小林", 58));

        // 请使用filter过滤出及格的同学，然后打印名字:
        persons.stream()
                .filter(x -> x.score > 59)
                .map(x -> x.name)
                .forEach(System.out::println);
    }
}

class Person {
    String name;
    int score;

    Person(String name, int score) {
        this.name = name;
        this.score = score;
    }
}

