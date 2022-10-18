package com.fmock.demo.controller.thread;

/**
 * @Author zhenhuaixiu
 * @Date 2022/10/18 15:04
 * @Version 1.0
 */
public class ThreadLocalTest {
    public static void main(String[] args) throws Exception {
        log("start main...");
        new Thread(() -> {
            log("run task...");
        }).start();
        new Thread(() -> {
            log("print...");
        }).start();
        log("end main.");
    }

    // Thread.currentThread()可以获取当前线程
    public static void log(String str) {
        System.out.println("线程名"+Thread.currentThread().getName() + ": " + str);
    }
}


// 在一个线程中，横跨若干个方法调用，需要传递的对象，我们通常称之为上下文（Context），它是一种状态，可以是用户身份、任务信息等
// 给每个方法增加一个context参数非常麻烦，而且有些时候，如果调用链有无法修改源码的第三方库，参数对象就传不进去了
// Java标准库提供了一个特殊的ThreadLocal，它可以在一个线程中传递同一个对象
class ThreadLocal2 {
    // ThreadLocal实例通常总是以静态字段初始化
    // ThreadLocal表示线程的“局部变量”，它确保每个线程的ThreadLocal变量都是各自独立的
    // ThreadLocal相当于给每个线程都开辟了一个独立的存储空间，各个线程的ThreadLocal关联的实例互不干扰
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static String uu = "";

    public static void main(String[] args) {
        try {
            threadLocal.set("322323user");
            uu = "hello";
            t1();
            t2();
            t3();
            t4();
        } finally {
            // 使用ThreadLocal要用try ... finally结构，并在finally中清除
            threadLocal.remove();
        }

    }

    public static void t1() {

        Thread t1 = new Thread(() -> {
            // 各个线程的threadLocal互不干扰，但是static遍量是共用的
            String user = threadLocal.get();  // null
            System.out.println("t1"+user + uu);
        });
        t1.start();
    }

    public static void t2() {
        String user = threadLocal.get();
        System.out.println("t2"+user + uu);
    }

    public static void t3() {
        threadLocal.set("update user");
        uu = "word";

        String user = threadLocal.get();
        System.out.println("t3"+user + uu);
    }

    public static void t4() {
        String user = threadLocal.get();
        System.out.println("t4"+user + uu);
    }
}
