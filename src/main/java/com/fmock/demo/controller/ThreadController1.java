package com.fmock.demo.controller;

import org.junit.jupiter.api.Test;

/**
 * @Author zhenhuaixiu
 * @Date 2022/6/21 16:18
 * @Version 1.0
 */
public class ThreadController1 {

    /**
     * 线程同步
     */
    public static void main (String[] args) throws InterruptedException {
        func1();
    }



    /**
     * 多线程模型下，要保证逻辑正确，对共享变量进行读写时，必须保证一组指令以原子方式执行：即某一个线程执行时，其他线程必须等待
     * 下面结果每次都不一样，因为 `+=`操作不是原子性的，
     */
    public static void func1() throws InterruptedException {
        Thread add = new AddThread();
        var dec = new DecThread();

        add.start();
        dec.start();

        // 线程的执行顺序，人为不可干涉
        // 当多个线程同时运行时，线程的调度由操作系统决定，程序本身无法决定。因此，任何一个线程都有可能在任何指令处被操作系统暂停，然后在某个时间段后继续执行。
        // 加join为了两个线程都跑完，不然下面直接输出了
        add.join();
        dec.join();

        System.out.println(Counter.count);
    }

    /**
     * Java程序使用synchronized关键字对一个对象进行加锁
     */
    public static void func2() {

    }

}





class Counter {
    public static int count = 0;  // volatile 该字段有局限性，不能用于依赖以前的值，此处为 `+=`,依赖之前的其他值
}

class AddThread extends Thread {
    @Override
    public void  run() {
        for (int i =0; i<10000; i++) {
            Counter.count += 1;
        }
    }
}

class DecThread extends Thread {
    @Override
    public void run() {
        for (int i=0; i<10000; i++) {
            Counter.count -= 1;
        }
    }
}
