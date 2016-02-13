import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Anthony on 2/2/2016.
 */
// Edited by Clayton Barham on 2/12/16 Note: My edtis have produced no build errors in elicpse, but have not been properly debugged yet.
public class Balancer {
    public static final int ELIMINATIONARRAYSIZE = 10;

    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    ThreadLocal<Integer> lastSlotRange;
    Balancer leftChild, rightChild;
    ConcurrentLinkedQueue Qleft, Qright;
    int Bdepth;

    //TODO : Figure out what order to do the linking for the leftChild and rightChild child
    public Balancer(int depth){
        producerToggle = new ToggleBit();
        consumerToggle = new ToggleBit();
        eliminationArray = new Exchanger[ELIMINATIONARRAYSIZE];
        lastSlotRange = new ThreadLocal<>();
        Bdepth = depth;
        
        // Recursively create children, if this is not the last layer of the ED-Tree, other wise add the queues
        if(depth > 1)
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
        	Qleft = new ConcurrentLinkedQueue();
        	Qright = new ConcurrentLinkedQueue();
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
