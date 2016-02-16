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
        	Qleft = new ConcurrentLinkedQueue<>();
        	Qright = new ConcurrentLinkedQueue<>();
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

    public Integer pop()
    {
		ExchangerPackage popPackage = new ExchangerPackage(State.WAITING, Type.POP);
        int currLocation = 0;

        for(lastSlotRange.set(ELIMINATIONARRAYSIZE);lastSlotRange.get()>0;lastSlotRange.set(lastSlotRange.get()/2))
        {
            eliminationArray[currLocation].slot.set(null);
            currLocation = ThreadLocalRandom.current().nextInt(lastSlotRange.get());

            if (eliminationArray[currLocation].slot.get()==null)
            {
                eliminationArray[currLocation].slot.set(popPackage);

                for (int timer = 0; timer < 100; timer++)
                {
                    if (eliminationArray[currLocation] == null)
                    {
                        return null;
                    }
                    else if (eliminationArray[currLocation].slot.get().state == State.DIFFRACTED1)
                    {
                        eliminationArray[currLocation].slot.set(null);
                        if (rightChild != null)
                        {
                            return rightChild.pop();
                        }
                        else
                        {
                            return Qright.poll();
                        }
                    }
                }
            }
            else if(eliminationArray[currLocation].slot.get().type==Type.POP)
            {
                // colliding with a pop means we diffract to different children

                eliminationArray[currLocation].slot.get().state = State.DIFFRACTED1;
                if(leftChild!=null)
                {
                    return leftChild.pop();
                }
                else
                {
                    return Qleft.poll();
                }
            }
            else if(eliminationArray[currLocation].slot.get().type==Type.PUSH)
            {

                int target =(Integer) eliminationArray[currLocation].slot.get().value;

                eliminationArray[currLocation].slot.set(null);
                return target;

            }

        }

        if (consumerToggle.toogle())
        {
            eliminationArray[currLocation].slot.set(null);
            if (rightChild != null)
            {
                return rightChild.pop();
            }
            else
            {
                return Qright.poll();
            }
        }
        else
        {
            eliminationArray[currLocation].slot.set(null);
            if (leftChild != null)
            {
                return rightChild.pop();
            }
            else
            {
                return Qleft.poll();
            }
        }




    }
    public boolean push(Integer n)
    {
    	// create the exchanger package to be pushed
    	ExchangerPackage payload = new ExchangerPackage(n,State.WAITING,Type.PUSH);
        int currLocation = 0;
    	
    	// attempt to access slots in the elimination array, spinning on a timer and 
    	// trying successively smaller ranges of the array until the range hits size 
    	// zero, at which point it toggles the bit and moves to a child balancer
    	for(lastSlotRange.set(ELIMINATIONARRAYSIZE);lastSlotRange.get()>0;lastSlotRange.set(lastSlotRange.get()/2))
    	{
            eliminationArray[currLocation].slot.set(null);
    		// pick a random currLocation in the currLocation range
    		currLocation = ThreadLocalRandom.current().nextInt(lastSlotRange.get());
    		
    		if(eliminationArray[currLocation].slot.get()==null)
    		{
    			// if the chosen currLocation is currently empty, publish the payload there
    			eliminationArray[currLocation].slot.set(payload);
    			
    			// spin and wait for a collision
    			for(int timer = 0; timer<100;++timer)
    			{
    				// check for collisions while we wait
    				if(eliminationArray[currLocation]==null)
    				{
    					// if the currLocation is set back to null, then we collided with a pop
        				// and we are done here.
    					return true;
    				}
    				else if(eliminationArray[currLocation].slot.get().state == State.DIFFRACTED1)
    				{
    					// if the state is changed to diffracted1, then we collided with
    					// another push, and we should move to the right child
    					payload = eliminationArray[currLocation].slot.get();
    					eliminationArray[currLocation].slot.set(null);
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
    		else if(eliminationArray[currLocation].slot.get().type==Type.POP)
    		{
    			// if we collide with a pop, we replace it with our push package and let
    			// pop pick it up
    			eliminationArray[currLocation].slot.set(payload);
    			return true;
    		}
    		else if(eliminationArray[currLocation].slot.get().type==Type.PUSH)
    		{
    			// if we encounter another push operation at the currLocation, we diffract the
    			// two packages to opposite children, and leave the toggle alone
    			eliminationArray[currLocation].slot.get().state = State.DIFFRACTED1;
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
    		Integer m = (Integer) eliminationArray[currLocation].slot.get().value;
    		eliminationArray[currLocation].slot.set(null);
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
    		Integer m = (Integer) eliminationArray[currLocation].slot.get().value;
    		eliminationArray[currLocation].slot.set(null);
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
