// Created by Clayton Barham on 2/12/2016

public class Test
{
  public static void main(String[] args)
  {
    EDTree tree = new EDTree(3);
	  tree.Check_Tree();
      tree.root.push(5);
    System.out.println();
    // Instantiate ED-Tree
    // Spawn threads to make push and pop requests
    // track performance
  }
}
