import java.util.concurrent.SynchronousQueue;

/**
 * Created by Anthony on 2/2/2016.
 */
// Edited by Clayton Barham on 2/12/16 Note: My edtis have produced no build errors in elicpse, but have not been properly debugged yet.
public class Balancer {
    public static final int ELIMINATIONARRAYSIZE = 10;

    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    Balancer leftChild, rightChild;
    ThreadLocal<Integer> lastSlotRange;
    // the left and right children of thsi balancer, which will either be balancers or Concurrent Queues
    Balancer left,right;
    SynchronousQueue Qleft, Qright;
    int Bdepth;

    //TODO : Figure out what order to do the linking for the left and right child
    public Balancer(){
        producerToggle = new ToggleBit();
        consumerToggle = new ToggleBit();
        eliminationArray = new Exchanger[ELIMINATIONARRAYSIZE];
        lastSlotRange = new ThreadLocal<>();
        
        // Recursively create children, if this is not the last layer of the ED-Tree, other wise add the queues
        if(depth>1)
        {
        	left = new Balancer(--depth);
        	right = new Balancer(--depth);
        	Qleft = NULL;
        	Qright = NULL;
        }
        else
        {
        	left = NULL;
        	right = NULL;
        	Qleft = new SynchronousQueue();
        	Qright = new SynchronousQueue();
        }
    }
    
    public Balancer Get_Left()
    {
    	return left;
    }
    
    public Balancer Get_Right()
    {
    	return right;
    }
    
    public int get_Depth()
    {
    	return Bdepth;
    }
}
