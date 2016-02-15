import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Anthony on 2/2/2016.
 */
public class Balancer {
    public static final int ELIMINATIONARRAYSIZE = 10;

    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    ThreadLocal<Integer> lastSlotRange;
    Balancer leftChild, rightChild;
    ConcurrentLinkedQueue<Integer> Qleft, Qright;
    int Bdepth;
    int slot = 0;

    //TODO : Figure out what order to do the linking for the leftChild and rightChild child
    public Balancer(int depth){
        producerToggle = new ToggleBit();
        consumerToggle = new ToggleBit();
        eliminationArray = new Exchanger[ELIMINATIONARRAYSIZE];
        lastSlotRange = new ThreadLocal<>();
        Bdepth = depth;

		for (int i =0 ; i < ELIMINATIONARRAYSIZE; i++)
		{
			eliminationArray[i] = new Exchanger();
			eliminationArray[i].slot = new AtomicReference<>(null);
		}
        
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
        	Qleft = new ConcurrentLinkedQueue<Integer>();
        	Qright = new ConcurrentLinkedQueue<Integer>();
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
    
    public boolean push(Integer n)
    {
    	// create the exchanger package to be pushed
    	ExchangerPackage payload = new ExchangerPackage(n,State.WAITING,Type.PUSH);
    	
    	// attempt to access slots in the elimination array, spinning on a timer and 
    	// trying successively smaller ranges of the array until the range hits size 
    	// zero, at which point it toggles the bit and moves to a child balancer
    	for(lastSlotRange.set(ELIMINATIONARRAYSIZE);lastSlotRange.get()>0;lastSlotRange.set(lastSlotRange.get()/2))
    	{
    		// pick a random slot in the slot range
    		slot = ThreadLocalRandom.current().nextInt(lastSlotRange.get());
    		
    		if(eliminationArray[slot].slot.get()==null)
    		{
    			// if the chosen slot is currently empty, publish the payload there
    			eliminationArray[slot].slot.set(payload);
    			
    			// spin and wait for a collision
    			for(int timer = 0; timer<100;++timer)
    			{
    				// check for collisions while we wait
    				if(eliminationArray[slot]==null)
    				{
    					// if the slot is set back to null, then we collided with a pop
        				// and we are done here.
    					return true;
    				}
    				else if(eliminationArray[slot].slot.get().state == State.DIFFRACTED1)
    				{
    					// if the state is changed to diffracted1, then we collided with
    					// another push, and we should move to the right child
    					payload = eliminationArray[slot].slot.get();
    					eliminationArray[slot].slot.set(null);
    					if(rightChild!=null)
    					{
    						return rightChild.push((Integer) payload.value);
    					}
    					else
    					{
    						Qright.add((Integer) payload.value);
    						return true;
    					}
    				}
    			}
    		}
    		else if(eliminationArray[slot].slot.get().type==Type.POP)
    		{
    			// if we collide with a pop, we replace it with our push package and let
    			// pop pick it up
    			eliminationArray[slot].slot.set(payload);
    			return true;
    		}
    		else if(eliminationArray[slot].slot.get().type==Type.PUSH)
    		{
    			// if we encounter another push operation at the slot, we diffract the 
    			// two packages to opposite children, and leave the toggle alone
    			eliminationArray[slot].slot.get().state = State.DIFFRACTED1;
    			if(leftChild!=null)
    			{
    				return leftChild.push((Integer) payload.value);
    			}
    			else
    			{
    				Qleft.add((Integer) payload.value);
    				return true;
    			}
    		}
    	}
    	
    	// If the package exhausts the entire elimination range, toggle the bit and 
    	// try one of the child balancers
    	if(producerToggle.toogle())
    	{
    		// send to right child
    		Integer m = (Integer) eliminationArray[slot].slot.get().value;
    		eliminationArray[slot].slot.set(null);
    		if(rightChild!=null)
    		{
    			return rightChild.push(m);
    		}
    		else
    		{
    			Qright.add((Integer) payload.value);
				return true;
    		}
    	}
    	else
    	{
    		// send to left child
    		Integer m = (Integer) eliminationArray[slot].slot.get().value;
    		eliminationArray[slot].slot.set(null);
    		if(leftChild!=null)
    		{
    			return leftChild.push(m);
    		}
    		else
    		{
    			Qleft.add((Integer) payload.value);
				return true;
    		}
    	}
    }
}
