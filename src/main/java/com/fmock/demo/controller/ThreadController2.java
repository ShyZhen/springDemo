package com.fmock.demo.controller;

import java.util.List;

/**
 * @Author zhenhuaixiu
 * @Date 2022/6/23 16:22
 * @Version 1.0
 */
public class ThreadController2 {

    /**
     * 下面代码，4个线程对两个变量进行读写操作，但是使用的都是CounterNew.lock这一对象，导致明明可以并发的两个变量都无法并发执行了，效率大大降低。
     * 因此，应该使用两个锁，每个变量用一个。 AddStudentThread和DecStudentThread使用lockStu锁，AddTeacherThread和DecTeacherThread使用lockTea锁
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[] {new AddStudentThread(), new AddTeacherThread(), new DecStudentThread(), new DecTeacherThread()};

        for (Thread t : ts) {
            t.start();
        }

        for (Thread t : ts) {
            t.join();
        }

        System.out.println(CounterNew.studentCount);
        System.out.println(CounterNew.teacherCount);
    }

    /**
     * 不需要synchronized的操作
     */
    public static void func1() {
        // JVM规范定义了几种原子操作：
        // 1.基本类型的赋值，如 `int n = 100` (long和double除外)
        // 2.引用类型的赋值，如 `List<String> list = anotherList`
        // long 和 double 是64位数据，JVM没有明确规定64位赋值操作是不是一个原子操作。不过在x64平台的JVM是把long和double的赋值作为原子操作实现的

        // 单条原子操作不需要加同步锁
        /*
        public void set (int m) {
            synchronized (lock) {
                this.n = m;
            }
        }
        // 引用类型也不需要
        public void set(String s) {
            this.value = s;
        }
         */

        // 但是，如果是多行赋值语句，就必须保证是同步操作
        class Pair {
            public int first;
            public int last;
            public void set(int first, int last) {
                synchronized(this) {
                    this.first = first;
                    this.last = last;
                }
            }
        }

        // 有时候通过一些巧妙的转换，可以把非原子操作变成原子操作
        class Pair2 {
            public volatile int[] pair;
            public void set(int first, int last) {
                // 引用赋值的原子操作，不需要加锁
                this.pair = new int[] { first, last };
            }
        }



    }
}








class CounterNew {
    public static final Object lockStu = new Object();
    public static final Object lockTea = new Object();
    public static int studentCount = 0;
    public static int teacherCount = 0;
}

class AddStudentThread extends Thread {
    public void run() {
        for (int i=0; i<5000; i++) {
            synchronized(CounterNew.lockStu) {
                CounterNew.studentCount += 1;
            }
        }
    }
}

class DecStudentThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(CounterNew.lockStu) {
                CounterNew.studentCount -= 1;
            }
        }
    }
}

class AddTeacherThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) {
            synchronized(CounterNew.lockTea) {
                CounterNew.teacherCount += 1;
            }
        }
    }
}

class DecTeacherThread extends Thread {
    public void run() {
        for (int i=0; i<5000; i++) {
            synchronized(CounterNew.lockTea) {
                CounterNew.teacherCount -= 1;
            }
        }
    }
}