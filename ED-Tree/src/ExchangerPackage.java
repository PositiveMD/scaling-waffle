/**
 * Anthony Dinh
 * Clay Barham
 */
public class ExchangerPackage {
    Object value;
    Type type;

    // Used for pushing threads
    public ExchangerPackage(Object value, Type type)
    {
        this.value = value;
        this.type = type;
    }
}
