package com.fmock.demo.controller.thread;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 多线程的协调的问题 使用wait和notify
 *
 * @Author zhenhuaixiu
 * @Date 2022/7/25 14:51
 * @Version 1.0
 */
public class ThreadWaitNotifyController {

    @Test
    public void test1() throws InterruptedException {
        var taskQueue = new TaskQueue();
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 100; i++) {
                taskQueue.addTask("task" +i);
            }
        });

        t1.start();
        t1.join();


        System.out.println(taskQueue.getTask());
        System.out.println(taskQueue.getTask());
        System.out.println(taskQueue.getTask());
        System.out.println(taskQueue.getTask());
        System.out.println(taskQueue.getCount());
    }
}




class TaskQueue {
    Queue<String> queue = new LinkedList<>();

    /**
     * synchronized解决了多线程竞争的问题，多个线程同时往队列中添加任务，可以用sync加锁
     * 但是，synchronized并没有解决多线程协调的问题
     *
     * @param s
     */
    public synchronized void addTask(String s) {
        this.queue.add(s);                              // add,offer两者都是往队列尾部插入元素，不同的是，当超出队列界限的时候，add（）方法是抛出异常让你处理，offer（）方法是直接返回false
    }

    /**
     * 先判断队列中是否为空，为空就循环等待，直到其他线程调用addTask放入一个任务，while才会退出并返回队列元素。
     * 但是实际上，如果为空，while循环永远不会退出，因为执行while循环的时候，已经获取了this锁，其他线程无法调用addTask（都在等待），因为addTask也需要获取this锁
     */
    public synchronized String getTask() {
        while (this.queue.isEmpty()) {

        }
        return this.queue.remove();                    // poll()和remove(): 都是返回第一个元素，并在队列中删除返回的对象; 区别在于，如果没有元素 poll()会返回 null，而remove()会直接抛出 NoSuchElementException 异常。
    }

    public int getCount() {
        return this.queue.size();
    }
}
