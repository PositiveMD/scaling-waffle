/**
 * Created by Anthony on 2/2/2016.
 */
public class ExchangerPackage {
    Object value;
    State state;
    Type type;

    public ExchangerPackage(Object value, State state, Type type)
    {
        this.value = value;
        this.state = state;
        this.type = type;
    }

    public ExchangerPackage(State state, Type type)
    {
        this.state = state;
        this.type = type;
    }

    public  ExchangerPackage()
    {

    }
}
