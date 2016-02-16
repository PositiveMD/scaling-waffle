// Created by Clayton Barham on 2/12/2016

import java.util.ArrayList;
import java.util.List;
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
        //System.out.println(tree.root.pop());
       // System.out.println(tree.root.pop());

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














  }

}

/*
// This code was informed by http://www.tutorialspoint.com/java/java_multithreading.htm
class test_thread implements Runnable
{
	private Thread t;
	private String threadName;
	
	test_thread(String name)
	{
		threadName = name;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			//
		}
		catch(InterruptedException e)
		{
			System.out.println("Thread " +  threadName + " interrupted.");
		}
		
	}
	
	public void start()
	{
		if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	}
	
}*/
