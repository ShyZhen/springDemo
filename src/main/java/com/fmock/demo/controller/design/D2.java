package com.fmock.demo.controller.design;

/**
 * @Author zhenhuaixiu
 * @Date 2022/12/15 16:22
 * @Version 1.0
 */

// 单例模式 为了保证一个进程中，某个类有且仅有一个实例
// 因为这个类只有一个实例，因此，不能让调用方通过`new D2()`来创建实例，则它的构造方法必须是private
// 但是在类的内部，需要一个静态字段来引用唯一创建的实例, 并提供一个静态方法，直接返回实例
public class D2 {

    // 构造方法必须private，防止被实例化
    private D2() {}

    // 需要一个静态字段引用唯一的实例
    private static final D2 INSTANCE = new D2();

    // 提供一个静态方法，直接返回实例
    public static D2 getInstance() {
        return INSTANCE;
    }
}

// 或者直接把static变量暴露给外部
class D2Bak {

    private D2Bak() {}

    // 改成public
    public static final D2Bak INSTANCE = new D2Bak();
}


// 总结 单例模式的实现很简单：
// 1 私有化的构造方法
// 2 通过static变量引用唯一实例，保证全局唯一性
// 3 通过public static方法获取返回这个实例 / 把这个实例引用变成public


// 关于 单例模式的延迟加载（也就是懒汉式）
// 像我们上面的代码，一初始化就实例化了的是饿汉式，有人说占用内存，但是其实并没多少，保证代码的简单简洁，是推荐使用饿汉式的。
// 而懒汉式，也就是延迟加载，也就是说，上来定义的static变量=null，而下面调用的时候判断，如果是null再实例化。饿汉式在多线程下是错误的，在竞争条件下回创建多个实例，必须加同步锁才安全，但是加锁严重影响并发行能。


/* // 饿汉式示例，在调用方第一次调用getInstance()时才初始化全局唯一实例
class Singleton {
    private static Singleton INSTANCE = null;

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    private Singleton() {
    }
}
 */