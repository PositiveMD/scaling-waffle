// Created by Clayton Barham on 2/12/2016

public class Test
{
  public static void main(String[] args)
  {
    EDTree tree = new EDTree(3);
    tree.Check_Tree();
    tree.root.push(5);
    System.out.println();
    // Instantiate ED-Tree
    // Spawn threads to make push and pop requests
    // track performance
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
