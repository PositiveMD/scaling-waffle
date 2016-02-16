/**
 * Anthony Dinh
 * Clay Barham
 */
public class ExchangerPackage {
    Object value;
    State state;
    Type type;

    // Used for pushing threads
    public ExchangerPackage(Object value, State state, Type type)
    {
        this.value = value;
        this.state = state;
        this.type = type;
    }

    // Used for popping threads
    public ExchangerPackage(State state, Type type)
    {
        this.state = state;
        this.type = type;
    }

    // Used to initialize elimination array
    public  ExchangerPackage()
    {

    }
}
