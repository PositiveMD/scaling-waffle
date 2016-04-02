import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Anthony Dinh
 * Clay Barham
 */
public class ToggleBit {
    AtomicBoolean toogle = new AtomicBoolean(true);

    public boolean toogle(){
        boolean result;
        do{
            result = toogle.get();
        } while (!toogle.compareAndSet(result, !result));

        return result;
    }
}
