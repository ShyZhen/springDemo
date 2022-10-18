package com.fmock.demo.controller.thread;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @Author zhenhuaixiu
 * @Date 2022/10/17 16:51
 * @Version 1.0
 */
public class ThreadForkJoin {
    // Java7 开始引入了一种新的Fork/Join线程池，它可以执行一种特殊的任务，把一个大任务拆分成多个小任务并行执行。
    // 这就是Fork/Join任务的原理：判断一个任务是否足够小，如果是，直接计算，否则，就分拆成几个小任务分别计算。这个过程可以反复“裂变”成一系列小任务。
    public static void main(String[] args) {

        // 创建一个2000个随机数组成的数组
        Integer[] arr = new Integer[200];
        Integer expectedSum = 0;
        for (int i = 0; i< arr.length; i ++) {
            arr[i] = random();
            expectedSum += arr[i];
        }

        // System.out.println(Arrays.toString(arr));
        System.out.println("sum和为："+expectedSum);

        // fork/join
        ForkJoinTask<Integer> task = new SumTask(arr, 0, arr.length);
        long startTime = System.currentTimeMillis();
        long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }

    // 生成一个随机数
    public static Integer random() {
        Random r = new Random();
        return r.nextInt(10000);
    }
}




class SumTask extends RecursiveTask<Integer> {
    static final int THRESHOLD = 500;
    Integer[] array;
    int start;
    int end;

    SumTask(Integer[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            // 如果任务足够小,直接计算:
            Integer sum = 0;
            for (int i = start; i < end; i++) {
                sum += this.array[i];
                // 故意放慢计算速度:
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }
            return sum;
        }
        // 任务太大,一分为二:
        int middle = (end + start) / 2;
        System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
        SumTask subtask1 = new SumTask(this.array, start, middle);
        SumTask subtask2 = new SumTask(this.array, middle, end);
        invokeAll(subtask1, subtask2);
        Integer subresult1 = subtask1.join();
        Integer subresult2 = subtask2.join();
        Integer result = subresult1 + subresult2;
        System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result);
        return result;
    }
}