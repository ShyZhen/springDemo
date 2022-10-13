package com.fmock.demo.controller.thread;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhenhuaixiu
 * @Date 2022/10/11 14:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/thread")
public class ThreadReentrantLockController {
    private int number;

    @GetMapping(value = "test")
    public String test() {
        return "hello thread controller";
    }

    public synchronized void add(int n) {
        this.number += n;
    }
}


class Counter123 {
    private int number;

    public void add(int n) {
        synchronized(this) {
            this.number += n;
        }
    }
}
// 如果用ReentrantLock替代，可以把代码改造为：
// ReentrantLock可以替代synchronized进行同步；
// ReentrantLock获取锁更安全；
// 必须先获取到锁，再进入try {...}代码块，最后使用finally保证释放锁；
// 可以使用tryLock()尝试获取锁。
class Counter124 {
    private final Lock lock = new ReentrantLock();
    private int count;

    public void add(int n) {
        lock.lock();
        try {
            count += n;
        } finally {
            lock.unlock();
        }
    }
}

// 使用ReentrantLock比直接使用synchronized更安全，可以替代它进行线程同步
// 但是，synchronized可以配合wait和notify实现线程在条件不满足时等待，条件满足时唤醒，用ReentrantLock我们怎么编写wait和notify的功能呢？
// 答案是使用Condition对象来实现wait和notify的功能。
// Condition提供的await()、signal()、signalAll()原理和synchronized锁对象的wait()、notify()、notifyAll()是一致的，并且其行为也是一样的
// await()会释放当前锁，进入等待状态；
// signal()会唤醒某个等待线程；
// signalAll()会唤醒所有等待线程；
class TaskQueue1 {
    private final Lock lock = new ReentrantLock();

    // 使用Condition时，引用的Condition对象必须从Lock实例的newCondition()返回
    // 这样才能获得一个绑定了Lock实例的Condition实例。
    private final Condition condition = lock.newCondition();

    private Queue<String> queue = new LinkedList<>();

    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}


class AccessLimitControl {
    // 任意时刻仅允许最多3个线程获取许可:
    final Semaphore semaphore = new Semaphore(3);

    public String access() throws Exception {
        // 如果超过了许可数量,其他线程将在此等待:
        semaphore.acquire();
        try {
            // TODO:
            return UUID.randomUUID().toString();
        } finally {
            semaphore.release();
        }
    }
}



// 线程池
// Java语言虽然内置了多线程支持，启动一个新线程非常方便，但是，创建线程需要操作系统资源（线程资源，栈空间等），频繁创建和销毁大量线程需要消耗大量时间。
// 如果可以复用一组线程,那么我们就可以把很多小任务让一组线程来执行，而不是一个任务对应一个新线程。这种能接收大量小任务并进行分发处理的就是线程池。
// 简单来说，线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。
// 如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。
// Java标准库提供了ExecutorService接口表示线程池，它的典型用法如下
class TestExecutorService {
    @Test
    public void test() {
        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 提交任务
//        executor.submit(task1);
//        executor.submit(task2);
//        executor.submit(task3);
//        executor.submit(task4);
    }
}
class TestExecutorService2 {

    @Test
    public void test() {
        // 创建
        ExecutorService es = Executors.newFixedThreadPool(4);
        // ExecutorService es = Executors.newCachedThreadPool();  // 可变数量线程池

        // 提交任务（线程执行的）
        for (int i = 0; i < 10; i++) {
            es.submit(new Task("任务"+i));
        }

        // 关闭线程池 使用shutdown()方法关闭线程池的时候，它会等待正在执行的任务先完成，然后再关闭。
        // shutdownNow()会立刻停止正在执行的任务，awaitTermination()则会等待指定的时间让线程池关闭
        es.shutdown();
    }
}
class Task implements Runnable {
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println("开始任务:" + this.name);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end：" + this.name);
    }
}
// 我们观察执行结果，一次性放入10个任务，由于线程池只有固定的4个线程，因此，前4个任务会同时执行，等到有线程空闲后，才会执行后面的两个任务。
// 如果我们把线程池改为CachedThreadPool，由于这个线程池的实现会根据任务数量动态调整线程池的大小，所以6个任务可一次性全部同时执行


class TestExecutorService3 {

    @Test
    public void test() {
        // 创建
        ScheduledExecutorService es = Executors.newScheduledThreadPool(4);

        // 提交任务（线程执行的）
        es.scheduleAtFixedRate(new Task("fixed-rate"), 2, 3, TimeUnit.SECONDS);

        es.shutdown();
    }
}

//JDK提供了ExecutorService实现了线程池功能：
//线程池内部维护一组线程，可以高效执行大量小任务；
//Executors提供了静态方法创建不同类型的ExecutorService；
//必须调用shutdown()关闭ExecutorService；
//ScheduledThreadPool可以定期调度多个任务。