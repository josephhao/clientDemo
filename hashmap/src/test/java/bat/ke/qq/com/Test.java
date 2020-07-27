package bat.ke.qq.com;

import java.util.concurrent.ForkJoinPool;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        AtomicInteger atomicInteger = new AtomicInteger();

        forkJoinPool.submit(new RecursiveTask<AtomicInteger>() {
            @Override
            protected AtomicInteger compute() {
                atomicInteger.incrementAndGet();
                return atomicInteger;
            }
        });

    }
}
