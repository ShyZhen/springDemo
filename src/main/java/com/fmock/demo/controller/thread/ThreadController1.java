package com.fmock.demo.controller.thread;

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
     * 通过加锁和解锁的操作，就能保证逻辑总是在一个线程执行期间，不会有其他线程进入此指令区间。就算被操作系统中断执行，其他线程也无法获得锁，导致无法进入指令区间。
     * 只有执行线程释放锁后，其他线程才有机会获得锁并执行。
     * synchronized保证了代码块在任意时刻最多只有一个线程能执行
     *
     * 我们来概括一下如何使用synchronized：
     * 1.找出修改共享变量的线程代码
     * 2.选择一个共享实例作为锁lockObject
     * 3.使用synchronized(lockObject) { ... }
     * 不用害怕synchronized中抛出异常，因为在结束处都会释放锁
     */
    public static void func2() {}

}





class Counter {
    /**
     * 用Counter.lock实例作为锁，两个线程在执行各自的synchronized(Counter.lock) { ... }代码块时，必须先获得锁，才能进入代码块进行。
     * 执行结束后，在synchronized语句块结束会自动释放锁。这样一来，对Counter.count变量进行读写就不可能同时进行.
     * 使用synchronized解决了多线程同步访问共享变量的正确性问题。但是，它的缺点是带来了性能下降。
     * 因为synchronized代码块无法并发执行。此外，加锁和解锁需要消耗一定的时间，所以，synchronized会降低程序的执行效率。
     */
    public static final Object lock = new Object();

    public static int count = 0;  // volatile 该字段有局限性，不能用于依赖以前的值，此处为 `+=`,依赖之前的其他值
}

class AddThread extends Thread {
    @Override
    public void  run() {
        for (int i =0; i<10000; i++) {
            synchronized(Counter.lock) {  // 获取锁
                Counter.count += 1;
            }                             // 释放锁
        }
    }
}

class DecThread extends Thread {
    @Override
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized (Counter.lock) {
                Counter.count -= 1;
            }
        }
    }
}
