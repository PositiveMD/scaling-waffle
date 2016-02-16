import java.util.concurrent.ThreadLocalRandom;

/**
 * Anthony Dinh
 * Clay Barham
 */
public class EDTree {
  
  // The root of the ED Tree
	Balancer root;
	
	// A constructor
	public EDTree(int depth)
	{
		root = new Balancer(depth);
	}
	
	public void Check_Tree()
	{
		Balancer check = root;
		for(int i = 1; i<=root.get_Depth();++i)
		{
			if(check!=null)
			{
				System.out.println(i);
				check = check.Get_Left();
			}
			else
			{
				System.out.println("Broke Early: ");
				System.out.println(i);
				break;
			}
		}
		System.out.println(root.get_Depth());
	}

    public Integer pop()
    {
        ExchangerPackage popPackage = new ExchangerPackage(State.WAITING, Type.POP);

        Balancer currBalancer = root;
        int currLocation = 0;

        for (int currLevel = 0; currLevel < root.Bdepth; currLevel++)
        {
            // TODO: There has to be a better way to do this for loop
            for (root.lastSlotRange.set(Balancer.ELIMINATIONARRAYSIZE);root.lastSlotRange.get() > 0; root.lastSlotRange.set(root.lastSlotRange.get()/2))
            {
                currLocation = ThreadLocalRandom.current().nextInt(root.lastSlotRange.get());

                // Check to see if there was another package in the location that we chose
                if(root.eliminationArray[currLocation].slot.compareAndSet(null, popPackage))
                {
                    for (int timer = 0; timer < 100; timer++)
                    {
                        // Someone collided with us
                        if (!root.eliminationArray[currLocation].slot.get().equals(popPackage))
                        {

                        }
                    }
                }
                // We collided with another package there.
                else
                {

                }
            }
        }



        return 5;
    }

	public boolean push(Integer pushValue)
	{
		// create the exchanger package to be pushed
		ExchangerPackage pushPackage = new ExchangerPackage(pushValue,State.WAITING,Type.PUSH);
        int currLocation = 0;

		// attempt to access slots in the elimination array, spinning on a timer and 
		// trying successively smaller ranges of the array until the range hits size 
		// zero, at which point it toggles the bit and moves to a child balancer
		for(root.lastSlotRange.set(Balancer.ELIMINATIONARRAYSIZE);root.lastSlotRange.get()>0;root.lastSlotRange.set(root.lastSlotRange.get()/2))
		{
			// pick a random currLocation in the currLocation range
			currLocation = ThreadLocalRandom.current().nextInt(root.lastSlotRange.get());

            root.eliminationArray[currLocation].slot.compareAndSet(null, pushPackage);

			if(root.eliminationArray[currLocation]==null)
			{
				// if the chosen currLocation is currently empty, publish the pushPackage there
				root.eliminationArray[currLocation].slot.set(pushPackage);

				// spin and wait for a collision
				for(int timer = 0; timer<100;++timer)
				{
					// check for collisions while we wait
					if(root.eliminationArray[currLocation]==null)
					{
						// if the currLocation is set back to null, then we collided with a pop
						// and we are done here.
						return true;
					}
					else if(root.eliminationArray[currLocation].slot.get().state == State.DIFFRACTED1)
					{
						// if the state is changed to diffracted1, then we collided with
						// another push, and we should move to the right child
						pushPackage = root.eliminationArray[currLocation].slot.get();
						root.eliminationArray[currLocation].slot.set(null);
						if(root.rightChild!=null)
						{
							return root.rightChild.push((Integer) pushPackage.value);
						}
						else
						{
							root.Qright.add((Integer) pushPackage.value);
							return true;
						}
					}
				}
			}
			else if(root.eliminationArray[currLocation].slot.get().type==Type.POP)
			{
				// if we collide with a pop, we replace it with our push package and let
				// pop pick it up
				root.eliminationArray[currLocation].slot.set(pushPackage);
				return true;
			}
			else if(root.eliminationArray[currLocation].slot.get().type==Type.PUSH)
			{
				// if we encounter another push operation at the currLocation, we diffract the
				// two packages to opposite children, and leave the toggle alone
				root.eliminationArray[currLocation].slot.get().state = State.DIFFRACTED1;
				if(root.leftChild!=null)
				{
					return root.leftChild.push((Integer) pushPackage.value);
				}
				else
				{
					root.Qleft.add((Integer) pushPackage.value);
					return true;
				}
			}
		}

		// If the package exhausts the entire elimination range, toggle the bit and 
		// try one of the child balancers
		if(root.producerToggle.toogle())
		{
			// send to right child
			Integer m = (Integer) root.eliminationArray[currLocation].slot.get().value;
			root.eliminationArray[currLocation].slot.set(null);
			if(root.rightChild!=null)
			{
				return root.rightChild.push(m);
			}
			else
			{
				root.Qright.add((Integer) pushPackage.value);
				return true;
			}
		}
		else
		{
			// send to left child
			Integer m = (Integer) root.eliminationArray[currLocation].slot.get().value;
			root.eliminationArray[currLocation].slot.set(null);
			if(root.leftChild!=null)
			{
				return root.leftChild.push(m);
			}
			else
			{
				root.Qleft.add((Integer) pushPackage.value);
				return true;
			}
		}
	}
	
	public int get_depth()
	{
		return root.get_Depth();
	}
}
