import java.util.concurrent.SynchronousQueue;

/**
 * Created by Anthony on 2/2/2016.
 */
// Edited by Clayton Barham on 2/12/16 Note: My edtis have produced no build errors in elicpse, but have not been properly debugged yet.
public class Balancer {
    public static final int ELIMINATIONARRAYSIZE = 10;

    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    ThreadLocal<Integer> lastSlotRange;
    // the leftChild and rightChild children of thsi balancer, which will either be balancers or Concurrent Queues
    Balancer leftChild, rightChild;
    SynchronousQueue Qleft, Qright;
    int Bdepth;

    //TODO : Figure out what order to do the linking for the leftChild and rightChild child
    public Balancer(int depth){
        producerToggle = new ToggleBit();
        consumerToggle = new ToggleBit();
        eliminationArray = new Exchanger[ELIMINATIONARRAYSIZE];
        lastSlotRange = new ThreadLocal<>();
        
        // Recursively create children, if this is not the last layer of the ED-Tree, other wise add the queues
        if(depth>1)
        {
        	leftChild = new Balancer(--depth);
        	rightChild = new Balancer(--depth);
        	Qleft = null;
        	Qright = null;
        }
        else
        {
        	leftChild = null;
        	rightChild = null;
        	Qleft = new SynchronousQueue();
        	Qright = new SynchronousQueue();
        }
    }
    
    public Balancer Get_Left()
    {
    	return leftChild;
    }
    
    public Balancer Get_Right()
    {
    	return rightChild;
    }
    
    public int get_Depth()
    {
    	return Bdepth;
    }
}
