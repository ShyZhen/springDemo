package com.fmock.demo.controller.thread;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @Author zhenhuaixiu
 * @Date 2022/10/13 16:52
 * @Version 1.0
 */
public class ThreadFuture {
    @Test
    public void test() {
        Task4 t = new Task4("hellllo");
        try {
            String result = t.call();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// 在执行多个任务的时候，使用Java标准库提供的线程池是非常方便的。我们提交的任务只需要实现Runnable接口，就可以让线程池去执行
// Runnable接口有个问题，它的方法没有返回值。如果任务需要一个返回结果，那么只能保存到变量，还要提供额外的方法读取，非常不便
// 所以，Java标准库还提供了一个Callable接口，和Runnable接口比，它多了一个返回值
class Task3 implements Runnable {
    public String result;

    public void run() {
        this.result = "Runnable 的方法没有返回值";
    }

    public String getResult() {
        return this.result;
    }
}

class Task4 implements Callable<String> {
    public String result;

    public Task4(String name) {
        this.result = name;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(3000);
        return result;
    }
}






// 如果仔细看ExecutorService.submit()方法，可以看到，它返回了一个Future类型，一个Future类型的实例代表一个未来能获取结果的对象
// 当我们提交一个Callable任务后，我们会同时获得一个Future对象，然后，我们在主线程某个时刻调用Future对象的get()方法，就可以获得异步执行的结果。
// 在调用get()时，如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么get()会阻塞，直到任务完成后才返回结果。
//
// 一个Future<V>接口表示一个未来可能会返回的结果，它定义的方法有:
//get()：获取结果（可能会等待）
//get(long timeout, TimeUnit unit)：获取结果，但只等待指定的时间；
//cancel(boolean mayInterruptIfRunning)：取消当前任务；
//isDone()：判断任务是否已完成。
class test1 {
    @SneakyThrows
    @Test
    public void test() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 定义任务
        Callable<String> task = new Task4("某个任务");

        // 提交任务并活动Future
        Future<String> future = executor.submit(task);

        // 从Future中获取异步执行结果
        String result = future.get();  // 可能阻塞 直到任务完成后才返回结果

        System.out.println(result);
    }
}




// 使用CompletableFuture
// 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否位true，这两种方法都不很好，因为主线程会被迫等待。
// 从JAVA8开始引入了CompletableFuture，针对Future进行了改进，可以传回调对象，当异步任务完成或者发生异常时，自动调用对象的回调方法。

// 可见CompletableFuture的优点是：
// 异步任务结束时，会自动回调某个对象的方法；
// 异步任务出错时，会自动回调某个对象的方法；
// 主线程设置好回调后，不再关心异步任务的执行。
class Test2 {
    @SneakyThrows
    public static void main(String[] args) {
        // 创建异步执行任务
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(Test2::fetchPrice);
        // CompletableFuture<String> cf = new CompletableFuture<String>();

        // 如果执行成功
        cf.thenAccept((result) -> {
            System.out.println("回调结果："+result);
        });

        cf.thenAcceptAsync((result) -> {
            System.out.println("异步回调结果："+result);
        });

        // 执行异常
        cf.exceptionally((e) -> {
           e.printStackTrace();
           return null;
        });

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        // new出来的不是守护线程，JVM必须等它执行完毕了才会结束。
        // 而completablefuture的async类方法默认使用的是forkjoinpool.commonpool，里面的线程都是守护线程，所以主线程一结束，不会等completablefuture的任务完成了。
        // 你可以试一下在completablefuture的async类方法第二个参数里传入自己定义的线程池，就会发现一定要等到completablefuture任务完成，JVM才会退出
        // 链接：https://www.zhihu.com/question/318472639/answer/1081382006
        Thread.sleep(300);
    }


    // 如果只是实现了异步回调机制，还不足以展示completableFuture的优势，更强大的是：多个CompletableFuture可以串行：
    // supplyAsync是java8引入的CompletableFuture静态方法，它在完成在ForkJoinPool.commonPool()或给定的Executor中异步运行的任务
    // 默认情况下，该任务将在ForkJoinPool.commonPool()中异步完成运行，最后，supplyAsync()将返回新的CompletableFuture，其值是通过调用给定的Supplier所获得的值。
    // 之后，与thenApply()一起使用 / thenApply，thenAccept，thenRun，thenCompose
    // supplyAsync是创建异步操作，thenApply等方法是串行两个线程/消费处理线程等，差别为有无入参和返回值。参考 https://www.jianshu.com/p/6bac52527ca4  https://blog.csdn.net/winterking3/article/details/116025829
    @SneakyThrows
    @Test
    public void test() {
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
           return fetchPrice();
        });

        // cfQuery成功后继续执行下一个任务
        CompletableFuture<Integer> cf2 = cfQuery.thenApply((res) -> {
            System.out.println(res);
            return fetchCount();
        });

        cf2.thenAccept((res) -> {
            System.out.println(res);
        });

        Thread.sleep(800);
    }

    @SneakyThrows
    public static String fetchPrice() {
        System.out.println("fetchPrice");
        Thread.sleep(200);
        return "回调函数callback";
    }

    @SneakyThrows
    public static Integer fetchCount() {
        System.out.println("fetchCount");
        Thread.sleep(200);
        return 999;
    }
}






//┌─────────────┐ ┌─────────────┐
//│ Query Code  │ │ Query Code  │
//│  from sina  │ │  from 163   │
//└─────────────┘ └─────────────┘
//       │               │
//       └───────┬───────┘
//               ▼
//        ┌─────────────┐
//        │    anyOf    │
//        └─────────────┘
//               │
//       ┌───────┴────────┐
//       ▼                ▼
//┌─────────────┐  ┌─────────────┐
//│ Query Price │  │ Query Price │
//│  from sina  │  │  from 163   │
//└─────────────┘  └─────────────┘
//       │                │
//       └────────┬───────┘
//                ▼
//         ┌─────────────┐
//         │    anyOf    │
//         └─────────────┘
//                │
//                ▼
//         ┌─────────────┐
//         │Display Price│
//         └─────────────┘
// 除了anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”，这些组合操作可以实现非常复杂的异步流程控制
class Main {
    public static void main(String[] args) throws Exception {
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://finance.sina.com.cn/code/");
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            return queryCode("中国石油", "https://money.163.com/code/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
        });
        CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice((String) code, "https://money.163.com/price/");
        });

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(200);
    }

    static String queryCode(String name, String url) {
        System.out.println("query code from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return "601857";
    }

    static Double fetchPrice(String code, String url) {
        System.out.println("query price from " + url + "...");
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
        }
        return 5 + Math.random() * 20;
    }
}



