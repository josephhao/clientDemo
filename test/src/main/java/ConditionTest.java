import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ConditionTest {
    private int n;
    AtomicInteger ot = new AtomicInteger(0);
    AtomicInteger i = new AtomicInteger(0);
    ReentrantLock lock = new ReentrantLock();
    Condition z = lock.newCondition();
    Condition o = lock.newCondition();
    Condition t = lock.newCondition();

    public ConditionTest(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(Integer printNumber) throws InterruptedException {
        lock.lock();

        try {
            while(ot.get() < n) {

                System.out.println("0");
                if (i.incrementAndGet() % 4 == 1) {
                    o.signal();
                } else {
                    t.signal();
                }
                z.await();
                if (ot.get() >= n) {
                    o.signalAll();
                    t.signalAll();
                    return;
                }
            }
        }finally {
            lock.unlock();
        }


    }

    public void even(Integer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while(ot.get() < n) {
                t.await();
                if (ot.get()>= n) {
                    z.signal();
                    o.signal();
                    return;
                }
                System.out.println(ot.incrementAndGet());
                i.incrementAndGet();
                z.signal();

            }
        }finally {
            lock.unlock();
        }

    }

    public void odd(Integer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while(ot.get() < n) {
                o.await();
                if (ot.get() >= n) {
                    z.signal();
                    t.signal();
                    return;
                }
                System.out.println(ot.incrementAndGet());
                i.incrementAndGet();
                z.signal();
            }
        } finally {
            lock.unlock();
        }
    }


    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        synchronized (ConditionTest.this) {
            while(ot.get() < n) {
                try {
                    while(i.get() % 2 !=0) {
                        ConditionTest.this.wait();
                    }
                    printNumber.accept(0);
                    i.incrementAndGet() ;
                    ConditionTest.this.notifyAll();
                } catch (InterruptedException e) {}


            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        synchronized (ConditionTest.this) {
            while(ot.get() < n) {
                try {
                    while(i.get() % 4 !=3) {
                        ConditionTest.this.wait();
                    }
                    printNumber.accept(ot.incrementAndGet());
                    i.incrementAndGet();
                    ConditionTest.this.notifyAll();
                } catch (InterruptedException e) {}

            }
        }

    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        synchronized (ConditionTest.this) {
            while(ot.get() < n) {
                try {
                    while(i.get() % 4 !=1) {
                        ConditionTest.this.wait();
                    }
                    printNumber.accept(ot.incrementAndGet());
                    i.incrementAndGet();
                    ConditionTest.this.notifyAll();
                } catch (InterruptedException e) {}

            }
        }
    }

    public static void main(String[] args) {
        ConditionTest test = new ConditionTest(10);
        IntConsumer intConsumer = new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.println(value);
            }
        };
        CountDownLatch latch = new CountDownLatch(3);
        Thread[] runnables = new Thread[] {
            new Thread(){
                @Override
                public void run() {
                    for (int i = 0; i < test.n; i++) {
                        try {
                            test.even(intConsumer);
                        } catch (Exception e) { e.printStackTrace();}
                    }
                    latch.countDown();
                }
            },new Thread(){
                @Override
                public void run() {
                    for (int i = 0; i < test.n; i++) {
                        try {
                            test.zero(intConsumer);
                        } catch (Exception e) { e.printStackTrace();}
                    }
                     latch.countDown();
                }
            },new Thread(){
                @Override
                public void run() {
                    for (int i = 0; i < test.n; i++) {
                        try {
                            test.odd(intConsumer);
                        } catch (Exception e) { e.printStackTrace();}
                    }
                    latch.countDown();
                }
            },
        };

        long start  = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            runnables[i].start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();

        }finally {
            long end = System.currentTimeMillis();
            System.out.println( (end-start) + "ms");
        }


    }
}
