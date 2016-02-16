/**
Anthony DInh
 Clay Barham
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test
{
    public static void main(String[] args) throws InterruptedException {
        Lock myLock = new ReentrantLock();
        EDTree tree = new EDTree(3);
        tree.root.push(5);
        int numOperations = 500000;
        int milliConversion = 1000000;

        Runnable test5050 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < numOperations * .5; i++)
                {
                    myLock.lock();
                    tree.root.push(5);
                    myLock.unlock();
                }

                for (int i = 0; i < numOperations * .5; i++)
                {
                    myLock.lock();
                    tree.root.pop();
                    myLock.unlock();
                }

            }
        };

        Runnable test2575 = new Runnable() {
            @Override
            public void run() {
                    for (int i = 0 ; i < numOperations * .25; i++)
                    {
                        myLock.lock();
                        tree.root.push(5);
                        myLock.unlock();
                    }

                    for (int i = 0; i < numOperations * .75; i++)
                    {
                        myLock.lock();
                        tree.root.pop();
                        myLock.unlock();
                    }
                }
        };

        Runnable test7525 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < numOperations * .75; i++)
                {
                    myLock.lock();
                    tree.root.push(5);
                    myLock.unlock();
                }

                for (int i = 0; i < numOperations * .25; i++)
                {
                    myLock.lock();
                    tree.root.pop();
                    myLock.unlock();
                }

            }
        };


        Thread one = new Thread(test5050);

        Thread two = new Thread(test2575);

        Thread three = new Thread(test7525);



        long startTime = System.nanoTime();
        one.start();
        one.join();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / milliConversion;
        System.out.println("1 thread 50 /50 " + duration);

        startTime = System.nanoTime();
        two.start();
        two.join();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / milliConversion;
        System.out.println("1 thread 25 /75 " + duration);

        startTime = System.nanoTime();
        three.start();
        three.join();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / milliConversion;
        System.out.println("1 thread 75 /25 " + duration);

        List<Thread> threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(test5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("2 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(test2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("2 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(test7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("2 thread 75 /25 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(test5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("4 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(test2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("4 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(test7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("4 thread 75 /25 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(test5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("8 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(test2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("8 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(test7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("8 thread 75 /25 " + duration);

        ConcurrentLinkedQueue controlQueue = new ConcurrentLinkedQueue();

        Runnable control5050 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .5; i++)
                {
                    myLock.lock();
                    controlQueue.add(5);
                    myLock.unlock();
                }

                for (int i = 0; i < numOperations * .5; i++)
                {
                    myLock.lock();
                    controlQueue.poll();
                    myLock.unlock();

                }

            }
        };

        Runnable control2575 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .25; i++)
                {
                    myLock.lock();
                    controlQueue.add(5);
                    myLock.unlock();
                }

                for (int i = 0; i < numOperations * .75; i++)
                {
                    myLock.lock();
                    controlQueue.poll();
                    myLock.unlock();

                }

            }
        };

        Runnable control7525 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .75; i++)
                {
                    myLock.lock();
                    controlQueue.add(5);
                    myLock.unlock();
                }

                for (int i = 0; i < numOperations * .25; i++)
                {
                    myLock.lock();
                    controlQueue.poll();
                    myLock.unlock();

                }

            }
        };

        Thread four = new Thread(control5050);
        Thread five = new Thread(control2575);
        Thread six = new Thread(control7525);

        startTime = System.nanoTime();
        four.start();
        four.join();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / milliConversion;
        System.out.println("control 1 thread 50 /50 " + duration);

        startTime = System.nanoTime();
        five.start();
        five.join();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / milliConversion;
        System.out.println("control 1 thread 25 /75 " + duration);

        startTime = System.nanoTime();
        six.start();
        six.join();
        endTime = System.nanoTime();
        duration = (endTime - startTime) / milliConversion;
        System.out.println("control 1 thread 75 /25 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(control5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 2 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(control2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 2 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 2; i++)
        {
            Thread th = new Thread(control7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 2 thread 75 /25 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(control5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 4 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(control2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 4 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 4; i++)
        {
            Thread th = new Thread(control7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 4 thread 75 /25 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(control5050);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 8 thread 50 /50 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(control2575);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 8 thread 25 /75 " + duration);

        threadList = new ArrayList<>();
        startTime = System.nanoTime();
        for (int i =0 ; i < 8; i++)
        {
            Thread th = new Thread(control7525);
            threadList.add(th);
            th.start();
        }
        for (Thread th : threadList)
        {
            th.join();
        }
        endTime = System.nanoTime();
        duration = (endTime- startTime) / milliConversion;
        System.out.println("control 8 thread 75 /25 " + duration);














  }

}

