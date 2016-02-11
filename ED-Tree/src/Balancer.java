/**
 * Created by Anthony on 2/2/2016.
 */
public class Balancer {
    ToggleBit producerToggle, consumerToggle;
    Exchanger[] eliminationArray;
    Balancer leftChild, rightChild;
    ThreadLocal<Integer> lastSlotRange;

    /**
     *  Creates a tree of N/2 levels of balancers that contain N queues where N is the number inputted.
     */
    public Balancer(int numQueues)
    {

    }
}
