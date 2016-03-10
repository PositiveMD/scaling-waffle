/**
Anthony Dinh
Clay Barham
*/

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class uses a Reentrant lock to restrict access to the EDTree
 * There are 3 runnables used to create the threads to test the EDTree,
 * one that has a distribution of 50% pops 50% pushes, one with 25%/75%, and etc.
 *
 * 
*/
public class Test
{
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        EDTree tree = new EDTree(3);
        int numOperations = 500000;
        int sampleSize = 30;
        int milliConversion = 1000000;
        long results[] = new long[30];
        long startTime;
        long endTime;
        long duration;
        List<Thread> threadList = new ArrayList<>();
        ConcurrentLinkedQueue<Integer> controlQueue = new ConcurrentLinkedQueue<Integer>();
        ExecutorService ex;
        int n = 0;
        int mean = 0;
        int dev = 0;
        List<Future<Object>> futures = new ArrayList<Future<Object>>();

        Runnable test5050 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < numOperations * .5; i++)
                {
                    //myLock.lock();
                    tree.root.push(5);
                    //myLock.unlock();
                }

                for (int i = 0; i < numOperations * .5; i++)
                {
                    //myLock.lock();
                    tree.root.pop();
                    //myLock.unlock();
                }

            }
        };

        Runnable test2575 = new Runnable() {
            @Override
            public void run() {
                    for (int i = 0 ; i < numOperations * .25; i++)
                    {
                        //myLock.lock();
                        tree.root.push(5);
                        //myLock.unlock();
                    }

                    for (int i = 0; i < numOperations * .75; i++)
                    {
                        //myLock.lock();
                        tree.root.pop();
                        //myLock.unlock();
                    }
                }
        };

        Runnable test7525 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ; i < numOperations * .75; i++)
                {
                    //myLock.lock();
                    tree.root.push(5);
                    //myLock.unlock();
                }

                for (int i = 0; i < numOperations * .25; i++)
                {
                    //myLock.lock();
                    tree.root.pop();
                    //myLock.unlock();
                }

            }
        };
        
        Runnable control5050 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .5; i++)
                {
                    //myLock.lock();
                    controlQueue.add(5);
                    //myLock.unlock();
                }

                for (int i = 0; i < numOperations * .5; i++)
                {
                    //myLock.lock();
                    controlQueue.poll();
                    //myLock.unlock();

                }

            }
        };

        Runnable control2575 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .25; i++)
                {
                    //myLock.lock();
                    controlQueue.add(5);
                    //myLock.unlock();
                }

                for (int i = 0; i < numOperations * .75; i++)
                {
                    //myLock.lock();
                    controlQueue.poll();
                    //myLock.unlock();

                }

            }
        };

        Runnable control7525 = new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i < numOperations * .75; i++)
                {
                    //myLock.lock();
                    controlQueue.add(5);
                    //myLock.unlock();
                }

                for (int i = 0; i < numOperations * .25; i++)
                {
                    //myLock.lock();
                    controlQueue.poll();
                    //myLock.unlock();

                }

            }
        };
        
        
        // set n equal to number of threads, and initialize the thread pool
        n = 1;
        ex = Executors.newFixedThreadPool(n);
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 75 / 25 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 75 / 25 " + mean);
        System.out.println();
        
        
        // set n equal to number of threads, and initialize the thread pool
        n = 2;
        ex = Executors.newFixedThreadPool(n);
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 75 / 25 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 75 / 25 " + mean);
        System.out.println();
        
        // set n equal to number of threads, and initialize the thread pool
        n = 4;
        ex = Executors.newFixedThreadPool(n);
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 75 / 25 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 75 / 25 " + mean);
        System.out.println();
        
        // set n equal to number of threads, and initialize the thread pool
        n = 8;
        ex = Executors.newFixedThreadPool(n);
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(test7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) 75 / 25 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control5050,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 50 / 50 " + mean);
        System.out.println();
        
        
        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control2575,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 25 / 75 " + mean);
        System.out.println();


        // initialize that list that will hold the future results
        //futures = new ArrayList<Future<Object>>();
        
        // loop enough times to obtain a statistically significant sample size, letting n threads
        // operate on the data structure, timing each trial
        for(int i=0;i<sampleSize;++i){
        	startTime = System.nanoTime();
        	// submit all tasks
        	for(int j=0;j<n;++j){
        		futures.add(ex.submit(control7525,(Object)null));
        	}
        	// This will block the thread until all submitted tasks are complete
        	for(Future<Object> future: futures){
        		future.get();
        	}
        	endTime = System.nanoTime();
        	duration = (endTime- startTime) / milliConversion;
        	results[i] = duration;
        	System.out.print(duration + " ");
        	futures = new ArrayList<Future<Object>>();
        }
        // reinitialize mean
        mean = 0;
        // sum all elements of results
        for(int i=0;i<sampleSize;++i)
        {
        	mean += results[i];
        }
        // divide total by sample size
        mean = mean/sampleSize;
        System.out.println();
        System.out.println(n+ " thread(s) control 75 / 25 " + mean);
        System.out.println();






  }

}
