import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Anthony Dinh on 2/11/2016.
 */
 // Edited by Clayton Barham on 2/12/2016
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
        return 5;
    }

	public boolean push(Integer n)
	{
		// create the exchanger package to be pushed
		ExchangerPackage payload = new ExchangerPackage(n,State.WAITING,Type.PUSH);

		// attempt to access slots in the elimination array, spinning on a timer and 
		// trying successively smaller ranges of the array until the range hits size 
		// zero, at which point it toggles the bit and moves to a child balancer
		for(root.lastSlotRange.set(Balancer.ELIMINATIONARRAYSIZE);root.lastSlotRange.get()>0;root.lastSlotRange.set(root.lastSlotRange.get()/2))
		{
			// pick a random slot in the slot range
			root.slot = ThreadLocalRandom.current().nextInt(root.lastSlotRange.get());

			if(root.eliminationArray[root.slot]==null)
			{
				// if the chosen slot is currently empty, publish the payload there
				root.eliminationArray[root.slot].slot.set(payload);

				// spin and wait for a collision
				for(int timer = 0; timer<100;++timer)
				{
					// check for collisions while we wait
					if(root.eliminationArray[root.slot]==null)
					{
						// if the slot is set back to null, then we collided with a pop
						// and we are done here.
						return true;
					}
					else if(root.eliminationArray[root.slot].slot.get().state == State.DIFFRACTED1)
					{
						// if the state is changed to diffracted1, then we collided with
						// another push, and we should move to the right child
						payload = root.eliminationArray[root.slot].slot.get();
						root.eliminationArray[root.slot].slot.set(null);
						if(root.rightChild!=null)
						{
							return root.rightChild.push((Integer) payload.value);
						}
						else
						{
							root.Qright.add((Integer) payload.value);
							return true;
						}
					}
				}
			}
			else if(root.eliminationArray[root.slot].slot.get().type==Type.POP)
			{
				// if we collide with a pop, we replace it with our push package and let
				// pop pick it up
				root.eliminationArray[root.slot].slot.set(payload);
				return true;
			}
			else if(root.eliminationArray[root.slot].slot.get().type==Type.PUSH)
			{
				// if we encounter another push operation at the slot, we diffract the 
				// two packages to opposite children, and leave the toggle alone
				root.eliminationArray[root.slot].slot.get().state = State.DIFFRACTED1;
				if(root.leftChild!=null)
				{
					return root.leftChild.push((Integer) payload.value);
				}
				else
				{
					root.Qleft.add((Integer) payload.value);
					return true;
				}
			}
		}

		// If the package exhausts the entire elimination range, toggle the bit and 
		// try one of the child balancers
		if(root.producerToggle.toogle())
		{
			// send to right child
			Integer m = (Integer) root.eliminationArray[root.slot].slot.get().value;
			root.eliminationArray[root.slot].slot.set(null);
			if(root.rightChild!=null)
			{
				return root.rightChild.push(m);
			}
			else
			{
				root.Qright.add((Integer) payload.value);
				return true;
			}
		}
		else
		{
			// send to left child
			Integer m = (Integer) root.eliminationArray[root.slot].slot.get().value;
			root.eliminationArray[root.slot].slot.set(null);
			if(root.leftChild!=null)
			{
				return root.leftChild.push(m);
			}
			else
			{
				root.Qleft.add((Integer) payload.value);
				return true;
			}
		}
	}
	
	public int get_depth()
	{
		return root.get_Depth();
	}
}
