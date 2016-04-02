import java.util.concurrent.ThreadLocalRandom;

/**
 * Anthony Dinh
 * Clay Barham
 */
public class EDTree {

  // The root of the ED Tree
	Balancer root;
    static final int TIMEOUT = 100;
	
	// A constructor
	public EDTree(int depth)
	{
		root = new Balancer(depth);
	}

	/**
	 * Method used to determine if the tree was instantiated correctly.
	 */
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

	
	public int get_depth()
	{
		return root.get_Depth();
	}
}
