import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Anthony Dinh
 * Clay Barham
 */
public class Balancer {
    public static final int EMPTY = Integer.MAX_VALUE;
    public static final int WAITING = Integer.MAX_VALUE - 1;
    public static final int TOGGLE = Integer.MAX_VALUE - 2;
    public static final int DIFFRACTED0 = Integer.MAX_VALUE - 3;
    public static final int DIFFRACTED1 = Integer.MAX_VALUE - 4;
    public static final int ELIMINATED = Integer.MAX_VALUE - 5;

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
			eliminationArray[i].slot = new AtomicStampedReference<>(null, EMPTY);
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
		ExchangerPackage popPackage = new ExchangerPackage(null, Type.POP);
        int currLocation = 0;
        int[] stampHolder = {EMPTY};

        for(lastSlotRange.set(ELIMINATIONARRAYSIZE);lastSlotRange.get()>0;lastSlotRange.set(lastSlotRange.get()/2))
        {
            //eliminationArray[currLocation].slot.set(null, EMPTY);
            currLocation = ThreadLocalRandom.current().nextInt(lastSlotRange.get());
            ExchangerPackage theirPackage = eliminationArray[currLocation].slot.get(stampHolder);
            int stamp = stampHolder[0];

            switch (stamp){
                case EMPTY :
                    if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, popPackage, EMPTY, WAITING))
                    {
                        for (int timer = 0; timer < 100; timer++)
                        {
                            theirPackage = eliminationArray[currLocation].slot.get(stampHolder);
                            stamp = stampHolder[0];

                            switch (stamp){
                                case WAITING:
                                    break;
                                case ELIMINATED:
                                    eliminationArray[currLocation].slot.set(null, EMPTY);
                                    return (Integer) theirPackage.value;
                                case DIFFRACTED0:
                                    eliminationArray[currLocation].slot.set(null, EMPTY);
                                    if (leftChild != null)
                                    {
                                        return leftChild.pop();
                                    }
                                    else
                                    {
                                        return Qleft.poll();
                                    }
                                case DIFFRACTED1:
                                    eliminationArray[currLocation].slot.set(null,EMPTY);
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
                    break;
                case WAITING:
                   if (theirPackage.value == null)
                   {
                       if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, popPackage, WAITING, DIFFRACTED0))
                       {
                           if (rightChild != null)
                           {
                               return rightChild.pop();
                           }
                           else
                           {
                               return Qright.poll();
                           }
                       }
                       break;
                   }
                    else
                   {
                       if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, popPackage, WAITING, ELIMINATED))
                       {
                           return (Integer) theirPackage.value;
                       }
                       break;
                   }
                case TOGGLE:
                    break;
                case DIFFRACTED0:
                    break;
                case DIFFRACTED1:
                    break;

            }
        }
        // #TODO: Set the state to TOGGLE, but not sure what to do if it fails.
        if (consumerToggle.toogle())
        {
            eliminationArray[currLocation].slot.set(null, EMPTY);
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
            eliminationArray[currLocation].slot.set(null, EMPTY);
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
    	ExchangerPackage payload = new ExchangerPackage(n,Type.PUSH);
        int currLocation = 0;
        int[] stampHolder = {EMPTY};
    	
    	// attempt to access slots in the elimination array, spinning on a timer and 
    	// trying successively smaller ranges of the array until the range hits size 
    	// zero, at which point it toggles the bit and moves to a child balancer
    	for(lastSlotRange.set(ELIMINATIONARRAYSIZE);lastSlotRange.get()>0;lastSlotRange.set(lastSlotRange.get()/2))
    	{
            // eliminationArray[currLocation].slot.set(null);
    		// pick a random currLocation in the currLocation range
    		currLocation = ThreadLocalRandom.current().nextInt(lastSlotRange.get());
    		ExchangerPackage theirPackage = eliminationArray[currLocation].slot.get(stampHolder);
            int stamp = stampHolder[0];
            switch (stamp){
            case EMPTY :
                if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, payload, EMPTY, WAITING))
                {
                    for (int timer = 0; timer < 100; timer++)
                    {
                        theirPackage = eliminationArray[currLocation].slot.get(stampHolder);
                        stamp = stampHolder[0];

                        switch (stamp){
                            case WAITING:
                                break;
                            case ELIMINATED:
                                eliminationArray[currLocation].slot.set(null, EMPTY);
                                return true;
                            case DIFFRACTED0:
                                eliminationArray[currLocation].slot.set(null, EMPTY);
                                if (leftChild != null)
                                {
                                    return leftChild.push((Integer) payload.value);
                                }
                                else
                                {
                                    return Qleft.add((Integer) payload.value);
                                }
                            case DIFFRACTED1:
                                eliminationArray[currLocation].slot.set(null,EMPTY);
                                if (rightChild != null)
                                {
                                    return rightChild.push((Integer) payload.value);
                                }
                                else
                                {
                                    return Qright.add((Integer) payload.value);
                                }
                        }
                    }
                }
                break;
            case WAITING:
               if (theirPackage.value != null)
               {
                   if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, payload, WAITING, DIFFRACTED0))
                   {
                       if (rightChild != null)
                       {
                           return rightChild.push((Integer) payload.value);
                       }
                       else
                       {
                           return Qright.add((Integer) payload.value);
                       }
                   }
                   break;
               }
                else
               {
                   if (eliminationArray[currLocation].slot.compareAndSet(theirPackage, payload, WAITING, ELIMINATED))
                   {
                       return true;
                   }
                   break;
               }
            case TOGGLE:
                break;
            case DIFFRACTED0:
                break;
            case DIFFRACTED1:
                break;
            } 
    	}
    	
    	// If the package exhausts the entire elimination range, toggle the bit and 
    	// try one of the child balancers
    	if(producerToggle.toogle())
    	{
    		// send to right child
    		eliminationArray[currLocation].slot.set(null,EMPTY);
            if (rightChild != null)
            {
                return rightChild.push((Integer) payload.value);
            }
            else
            {
                return Qright.add((Integer) payload.value);
            }
    	}
    	else
    	{
    		// send to left child
    		eliminationArray[currLocation].slot.set(null,EMPTY);
            if (leftChild != null)
            {
                return leftChild.push((Integer) payload.value);
            }
            else
            {
                return Qleft.add((Integer) payload.value);
            }
    	}
    }
}
