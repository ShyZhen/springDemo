package com.fmock.demo.controller.thread;

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


/**
 * 线程安全
 */
// 从上面我们可以知道，java依靠synchronized对线程进行同步，锁住的是哪个对象非常重要，一般同一个变量使用同一个锁即可
// 让线程自己选择锁住的对象，往往会使得代码逻辑混乱，也不利于封装。更好的方法是把synchronized逻辑封装起来。
class CounterObj {
    private int count = 0;

    public void add(int n) {
        synchronized (this) {
            this.count += n;
        }
    }

    public void dec(int n) {
        synchronized (this) {
            this.count -= n;
        }
    }

    // 读一个int变量不需要同步
    public int get() {
        return this.count;
    }

    // 当我们锁住的是this实例时，实际上可以使用synchronized修饰方法，add和add2两种写法是等价的
    // 因此，用synchronized修饰的方法就是同步方法，表示整个方法必须用this实例加锁
    public synchronized void add2(int n) {
        this.count += n;
    }

    // 如果对一个静态方法添加synchronized修饰符，由于没有this实例，针对的是类，
    // 但是我们注意到任何一个类都有一个由JVM自动创建的Class实例，因此，对static方法添加synchronized，锁住的是该类的Class实例
    public synchronized static void add3(int n) {
        //...
    }

}

/**
 * 线程安全：如果一个类被设计为允许多线程正确访问，我们就说这个类是“线程安全”的，上面的CounterObj类就是线程安全的。
 * java标准库的java.lang.StringBuffer也是线程安全的，有一些不变类，例如String,Integer,localDate，他们的所有成员变量都是final，多线程同时访问能读不能写，这些不变类也是安全的。
 * 最后，类似Math这些只提供静态方法，没有成员变量，也是线程安全的。
 *
 * 除了上述几种少数情况，大部分类，例如ArrayList，都是非线程安全的，我们不能在多线程中修改他们。
 * 但是，如果所有线程都只读取，不写入，那么也可以安全的在线程中共享。
 *
 * 没有特殊说明时，一个类默认是非线程安全的。
 */
class test {
    public static void main(String[] args) throws InterruptedException {
        CounterObj c1 = new CounterObj();
        //CounterObj c2 = new CounterObj();


        Thread t1 = new Thread(() -> {
            for (int i = 0; i<10000; i++) {
                c1.add(1);
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            for (int i = 0; i<10000; i++) {
                c1.dec(1);
            }
        });
        t2.start();

        t1.join();
        t2.join();

        System.out.println(c1.get());
    }
}