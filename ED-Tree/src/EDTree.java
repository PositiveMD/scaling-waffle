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
	
	public int get_depth()
	{
		return root.get_Depth();
	}
}
