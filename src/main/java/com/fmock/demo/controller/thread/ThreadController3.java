package com.fmock.demo.controller.thread;

import org.junit.jupiter.api.Test;

/**
 * 死锁
 *
 * @Author zhenhuaixiu
 * @Date 2022/7/25 10:19
 * @Version 1.0
 */
public class ThreadController3 {
    public static void main(String[] args) {

    }

    @Test
    public void test1() throws InterruptedException {
        var c1 = new Counter3();

        Thread t1 = new Thread(() -> {
            for (int i = -5000; i <= 5000; i++) {
                c1.add(i);
            }
        });
        t1.start();


        Thread t2 = new Thread(() -> {
            for (int i = -5000; i <= 5000; i++) {
                c1.add(i);
            }
        });
        t2.start();


        t1.join();
        t2.join();

        System.out.println(c1.get());
    }
}


/**
 * （同一个锁：重复获取，记录+-次数，为0释放）
 * add方法需要获得this锁，add中调用了dec，dec方法也需要this锁，那么，同一个线程，能否获取到锁后继续获取同一个锁？
 * 答案是肯定的，JVM允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫【可重复锁】
 * 由于java的线程锁是可重复锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取锁。
 * 每获取一次锁，记录+1，每退出synchronized块，记录-1，减到0的时候，才会真正释放锁。
 */
class Counter3 {
    private int count = 0;

    public synchronized void add(int n) {
        if (n < 0) {
            dec(-n);
        } else {
            this.count += n;
        }
    }

    public synchronized void dec(int n) {
        this.count -= n;
    }

    public synchronized int get() {
        return this.count;
    }
}


/**
 * 死锁
 * 在获取多个锁的时候，不同线程获取不同对象的锁可能导致死锁，对于下面程序，线程1和线程2如果分别执行add和dec：
 * 线程1：进入add()，获得lockA；
 * 线程2：进入dec()，获得lockB。
 * 随后
 * 线程1：准备获得lockB，失败，等待中；
 * 线程2：准备获得lockA，失败，等待中。
 *
 * 此时,两个线程各自持有不同的锁，然后试图获取对方手里的锁，造成双方无限等待下去，这就是死锁。
 * 死锁发生后，没有任何机制能解除，只能强制结束JVM进程。
 *
 * 如何避免？
 * 在编写多线程应用时，线程获取锁的顺序要一致，即严格按照先获取lockA，再获取lockB的顺序，改写dec方法为dec2
 */
class Counter3_1 {
    private final Object lockA = new Object();
    private final Object lockB = new Object();

    private int val1 = 0;
    private int val2 = 0;

    public void add (int n) {
        synchronized (lockA) {         // 获得lockA的锁
            this.val1 += n;
            synchronized (lockB) {     // 获得lockB的锁
                this.val2 +=n;
            }                          // 释放lockB的锁
        }                              // 释放lockA的锁
    }

    public void dec(int n) {
        synchronized (lockB) {         // 获得lockB的锁
            this.val2 -= n;
            synchronized (lockA) {     // 获得lockA的锁
                this.val1 -=n;
            }                          // 释放lockA的锁
        }                              // 释放lockB的锁
    }

    // 想同顺序获得锁。避免死锁
    public void dec2(int n) {
        synchronized (lockA) {  // 获得lockA的锁
            this.val1 -=n;

            synchronized (lockB) {  // 获得lockB的锁
                this.val2 -= n;
            }  // 释放lockB的锁

        }  // 释放lockA的锁
    }
}
