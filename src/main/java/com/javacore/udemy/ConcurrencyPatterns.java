package com.javacore.udemy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConcurrencyPatterns {
    public static void main(String[] args) {

        //StartingThreadsExample.startThreads();
        //VolatileExample.demonstrateVolatile();
        SynchronizedExample.demonstrateSynchronized();
        //LockObjectsExample.demonstrateLockObjects();
        //ThreadPoolsExample.demonstrateThreadPools();
        //CountdownLatchesExample.demonstrateCountdownLatches();
        //ProducerConsumerExample.demonstrateProducerConsumer();
        //WaitAndNotifyExample.demonstrateWaitAndNotify();
        //LowLevelProducerConsumerExample.demonstrateLowLevelProducerConsumer();
        //ReentrantLocksExample.demonstrateReentrantLocks();
        //DeadlockExample.demonstrateDeadlock();
        //SemaphoresExample.demonstrateSemaphores();
        //CallableAndFutureExample.demonstrateCallableAndFuture();
        //InterruptingThreadsExample.demonstrateInterruptingThreads();
    }

    /**
     * 1. Starting Threads
     */

    private static class StartingThreadsExample {
        public static void startThreads() {
            log.info("main is starting");
            Thread thread = new Thread(() -> {

                for (int i = 0; i < 7; i++) {
                    log.info(Thread.currentThread() + "," + i);
                }

                log.info("end thread.");
            }, "thread2");
            thread.start();


        }
    }

    /**
     * 2. Volatile - Basic Thread Communication | the Volatile Keyword
     */
    private static class VolatileExample {
        static class SharedResource {
            volatile int data;
        }

        public static void demonstrateVolatile() {
            SharedResource resource = new SharedResource();

            Thread volatileThread = new Thread(() -> {
                resource.data = 42;
                log.info("Volatile Thread: Data is set to 42.");
                synchronized (resource) {
                    resource.notifyAll(); // Notifica a otros hilos que pueden continuar
                }
            });
            volatileThread.start();

            synchronized (resource) {
                while (resource.data == 0) {
                    // Espera hasta que resource.data se actualice
                    log.info("Volatile Thread: Waiting for data to be updated...");
                    try {
                        resource.wait(); // El hilo entra en estado de espera
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Thread interrupted while waiting for data update.", e);
                    }
                }
            }

            log.info("Volatile Thread: Data is now " + resource.data);
        }

    }

    /**
     * 3. Synchronized
     */
    private static class SynchronizedExample {
        static class Counter {
            private int count = 0;

            public synchronized void increment() {
                count++;
            }
        }

        public static void demonstrateSynchronized() {
            Counter counter = new Counter();
            counter.increment();
            log.info("Synchronized Counter: " + counter.count);
        }
    }

    /**
     * 4. Lock Objects
     */
    private static class LockObjectsExample {
        static class Counter {
            private int count = 0;
            private final Lock lock = new ReentrantLock();

            public void increment() {
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
            }
        }

        public static void demonstrateLockObjects() {
            Counter counter = new Counter();
            counter.increment();
            log.info("Locked Counter: " + counter.count);
        }
    }

    /**
     * 5. Thread Pools
     */
    private static class ThreadPoolsExample {
        public static void demonstrateThreadPools() {
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(() -> log.info("Thread from a pool."));
            executor.shutdown();
        }
    }

    /**
     * 6. Countdown Latches
     */
    private static class CountdownLatchesExample {
        public static void demonstrateCountdownLatches() {
            CountDownLatch latch = new CountDownLatch(2);
            Thread thread1 = new Thread(() -> {
                log.info("Thread 1 is done.");
                latch.countDown();
            });
            Thread thread2 = new Thread(() -> {
                log.info("Thread 2 is done.");
                latch.countDown();
            });
            thread1.start();
            thread2.start();
            try {
                latch.await();
                log.info("Both threads are done.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 7. Producer-Consumer
     */
    private static class ProducerConsumerExample {
        public static void demonstrateProducerConsumer() {
            // Código de productor-consumidor
        }
    }

    /**
     * 8. Wait and Notify
     */
    private static class WaitAndNotifyExample {
        public static void demonstrateWaitAndNotify() {
            // Código de wait y notify
        }
    }

    /**
     * 9. Low-Level Producer-Consumer
     */
    private static class LowLevelProducerConsumerExample {
        public static void demonstrateLowLevelProducerConsumer() {
            // Código de productor-consumidor de bajo nivel
        }
    }

    /**
     * 10. Re-entrant Locks
     */
    private static class ReentrantLocksExample {
        public static void demonstrateReentrantLocks() {
            // Código de Reentrant Locks
        }
    }

    /**
     * 11. Deadlock
     */
    private static class DeadlockExample {
        public static void demonstrateDeadlock() {
            // Código para simular un deadlock
            Thread t1 = new Thread1();
            Thread t2 = new Thread2();
            t1.start();
            t2.start();
        }
    }

    public static final Object lock1 = new Object();
    public static final Object lock2 = new Object();

    private static class Thread1 extends Thread {

        @Override
        public void run() {
            synchronized (lock1) {
                log.info("Thread 1: Holding lock 1...");
                try {
                    lock1.wait(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Thread 1: Interrupted while waiting for lock 2.", e);
                    return;
                }
                log.info("Thread 1: Waiting for lock 2...");
                synchronized (lock2) {
                    log.info("Thread 1: Holding lock 1 & 2...");
                }
            }
        }
    }

    private static class Thread2 extends Thread {

        @Override
        public void run() {
            synchronized (lock2) {
                log.info("Thread 2: Holding lock 2...");
                try {
                    lock2.wait(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Thread 2: Interrupted while waiting for lock 1.", e);
                    return;
                }
                log.info("Thread 2: Waiting for lock 1...");
                synchronized (lock1) {
                    log.info("Thread 2: Holding lock 2 & 1...");
                }
            }
        }
    }


    /**
     * 12. Semaphores
     */
    private static class SemaphoresExample {
        public static void demonstrateSemaphores() {
            // Código de Semaphores
        }
    }

    /**
     * 13. Callable and Future
     */
    private static class CallableAndFutureExample {
        public static void demonstrateCallableAndFuture() {
            // Código de Callable y Future
        }
    }

    /**
     * 14. Interrupting Threads
     */

    private static class InterruptingThreadsExample {
        public static void demonstrateInterruptingThreads() {
            // Código para simular la interrupción de hilos
        }
    }
}
