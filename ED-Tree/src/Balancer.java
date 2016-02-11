/**
 * Created by Anthony on 2/2/2016.
 */
public class Balancer {
    public static final int ELIMINATIONARRAYSIZE = 10;

    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    Balancer leftChild, rightChild;
    ThreadLocal<Integer> lastSlotRange;

    //TODO : Figure out what order to do the linking for the left and right child
    public Balancer(){
        producerToggle = new ToggleBit();
        consumerToggle = new ToggleBit();
        eliminationArray = new Exchanger[ELIMINATIONARRAYSIZE];
        lastSlotRange = new ThreadLocal<>();
    }
}
