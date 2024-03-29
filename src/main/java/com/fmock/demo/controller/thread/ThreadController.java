package com.fmock.demo.controller.thread;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * @Author zhenhuaixiu
 * @Date 2022/5/18 11:03
 * @Version 1.0
 */
public class ThreadController {

    /**
     * 【多线程】基础介绍
     * 进程：计算机中，我们把一个任务成为一个进程，比如浏览器是一个进程，视频播放器是另一个进程。
     * 线程：某些进程内部还需要同时执行多个子任务，比如使用word时，可以一边打字一边拼写检查，还可以后台进行打印，这些子任务称为线程。
     * 关系：一个进程可以包含一个或多个线程，至少一个线程。
     * 操作系统调度的最小任务单位不是进程，而是线程。
     *
     * 多进程的缺点：
     * 1.创建进程比线程的开销大，尤其win系统。
     * 2.进程间通信比线程慢，因为线程间通信是读写同一个变量，内存共享。
     *
     * 多进程的优点：
     * 1.多进程稳定性高。因为多进程情况下，一个进程崩溃不会影响其他进程，而多线程情况下，任何一个线程崩溃会直接导致整个进程崩溃。
     *
     * Java多线程特点：
     * 1.java语言内置了多线程支持，一个java程序实际上是一个JVM进程，用一个主线程来执行main()方法，在main()内部我们又可以启动多个线程。（当main方法结束，主线程也结束了）
     * 2.JVM又负责垃圾回收的其他工作线程等。
     * 3.和单线程相比，多线程编程的特点在于，多线程经常需要读写共享数据，并且需要同步。比如播放电影，一个线程播放视频，一个线程播放音频，两个线程还要协调同步。
     * 4.多线程模型是java程序最基本的并发模型。
     */
    @Test
    public void test1() {

        System.out.println("test1 start...");

        // 创建新线程 方法一：覆写run
        Thread t1 = new MyThread();
        t1.start();    // start方法会自动调用run方法


        // 方法二：传入一个runnable
        Thread t2 = new Thread(new MyRunnable());
        t2.start();


        // 方法三：java8引入的lambda语法简写
        Thread t3 = new Thread(() -> {
            System.out.println("方法三，start a new thread");
        });
        t3.start();

        System.out.println("test1 end...");
    }

    /**
     * 执行顺序
     * 线程调度由操作系统决定，程序本身无法决定调度顺序
     * main就是主线程，如果main方法结束，主线程也就结束了
     * 想保证线程全部执行，必须保证main仍在执行 （20220621修正：其实不会退出的，跟go不一样，只是@Test里最终调用了exit，导致退出，不然仍然会继续执行其他线程）
     *
     * Thread.sleep()对当前线程休眠，当前线程!
     */
    @Test
    public void test2() throws InterruptedException {
        System.out.println("test2 start...");

        Thread t1 = new Thread(() -> {
            System.out.println("线程开始");
            System.out.println("线程结束");

            try {
                // 对当前线程休眠，当前线程t1
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程睡眠后1");
            System.out.println("线程睡眠后2");
            System.out.println("线程睡眠结束");

        });
        t1.start();


        // 要比t1线程晚结束才能完全执行，否则可能导致t1线程没执行完就退出了（main执行结束）（20220621修正：其实不会退出的，跟go不一样，只是@Test里最终调用了exit，导致退出，不然仍然会继续执行其他线程）
        // 或者join等待 t1.join()
        Thread.sleep(2000);  // 2000

        System.out.println("test2 end...");
    }

    /**
     * 线程状态
     * 一个线程对象只能调用一次start启动，并在新线程中执行run。一旦run执行结束，线程就结束了。因此线程的状态有以下几种：
     *
     * New：新创建的线程，尚未执行；
     * Runnable：运行中的线程，正在执行run()方法的Java代码；
     * Blocked：运行中的线程，因为某些操作被阻塞而挂起；
     * Waiting：运行中的线程，因为某些操作在等待中；
     * Timed Waiting：运行中的线程，因为执行sleep()方法正在计时等待；
     * Terminated：线程已终止，因为run()方法执行完毕。
     *
     * 线程终止原因：正常终止、意外终止未捕获异常、对线程实例调用stop强制终止。
     */
    @Test
    public void test3() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("线程开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程结束");
        });
        t.start();

        // 一个线程还可以等待另一个线程直到其运行结束
        // join就是指等待该线程结束，然后才继续往下执行自身线程
        // test3线程在启动t1线程后，可以通过t.join()等待t线程结束后再继续运行
        // join(long)的重载方法也可以指定一个等待时间，超过等待时间后就不再继续等待。
        t.join();

        System.out.println("test3 end");
    }

    /**
     * 连续join
     */
    @Test
    public void test4() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 start");
            System.out.println("t1 start");
            Thread t2 = new Thread(()->{
                System.out.println("t2 start");
                System.out.println("t2 start");
                Thread t3 = new Thread(()-> {
                    System.out.println("t3 start");
                    System.out.println("t3 start");
                });
                t3.start();
                try {t3.join();} catch (InterruptedException e) {e.printStackTrace();}
            });
            t2.start();
            try {t2.join();} catch (InterruptedException e) {e.printStackTrace();}
        });
        t1.start();
        t1.join();

        // 中断线程
        t1.interrupt();

        System.out.println("test4");
    }

    @Test
    public void test5() throws InterruptedException {
//        Thread t1 = new MyThread2();
        Thread t1 = new MyThread3();
        t1.start();

        Thread.sleep(2000);  // 当前线程睡眠时间越长，打印出的t1.run()越多，睡眠之后才中断
        t1.interrupt();  // 中断t线程
        t1.join();       // 等待t线程结束
        System.out.println("test5 end");
    }

    /**
     * // 守护线程  比如GC垃圾回收，可以随时退出的影响不大的，使用守护线程，当jvm退出，他也自己退出
     * // 一个无线循环的线程，一般要在start之前设置为守护线程（达到自动关闭的功能，而不是守护好不让他结束）
     * // 正常情况下，JAVA程序入口就是JVM启动的main线程，main线程又可以启动其他线程，当【所有】线程都运行结束时，JVM退出，进程结束。
     * // 但是，有一种线程就是无限循环的，如果他不结束，JVM就无法结束，那么，这种无限循环的线程怎么结束呢？答案就是守护线程。
     * // 守护线程指为其他线程服务的线程，在JVM中，所有非守护线程执行完毕后，无论有没有守护线程，都会自动退出，以此结束守护线程。
     * // 因此，JVM退出时，不关心守护线程是否已经结束。
     * //
     * // 注意：守护线程不能持有任何需要关闭的资源，比如打开文件，因为虚拟机退出时，守护线程没机会关闭文件，这会导致数据丢失
     */
    @Test
    public void test6() {
        Thread t = new TimerThread();
        t.setDaemon(true);  // 如何创建守护线程呢？方法和普通线程一样，只是在调用start()方法前，调用setDaemon(true)把该线程标记为守护线程
        t.start();
    }
}

// 跟go不一样，go的main结束全部结束。
// java的main会等所有线程结束才退出jvm。除非那个被设置了守护线程，即不再被等待，当main结束，jvm就退出
class ThreadTest {
    /**
     * Java程序入口就是由JVM启动main线程，main线程又可以启动其他线程。当所有线程都运行结束时，JVM退出，进程结束。
     *
     * 守护线程
     * 设置t1为守护线程，即不再被等待，当main结束，jvm就退出
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start");

        Thread t1 = new Thread(() -> {
            System.out.println("t1 start");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t1 end");
        });

        t1.setDaemon(true);   // 设置t1为守护进程，即不再被等待，当main结束，jvm就退出。  `ti end 打不出来`
        t1.start();

        // t1.join();
        // Thread.sleep(2000);

        System.out.println("main end");
    }
}







// 创建新线程 方法一：覆写run
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("方法一，start a new thread");
    }
}

// 方法二：传入一个runnable
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("方法二，start a new thread");
    }
}



// interrupt 线程中断
class MyThread2 extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n ++;

//            没中断线程需要手动设置退出
//            if (n > 10) {
//                break;
//            }

            // 睡眠300毫秒
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                break;
            }

            System.out.println(n + " hello!");
        }
    }
}
class MyThread3 extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start();            // 启动hello线程
        try {
            hello.join();         // 等待hello线程结束
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
        hello.interrupt();
    }
}
class HelloThread extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}


/**
 // 守护线程示例
 // 一个无线循环的线程，一般要在start之前设置为守护线程（达到自动关闭的功能，而不是守护好不让他结束）
 //
 // 实际的意思是把一个线程标记为“守护线程”，就是当他是一个“后台线程”or“内部线程”，类似于管理内存垃圾回收的线程一样。
 // 只要正常线程执行结束了，我主线程才不管你是不是在垃圾回收还是做哪些后台操作，只要正常线程结束，我JVM就结束了。
 */
class TimerThread extends Thread {
    public void run() {
        while (true) {
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}