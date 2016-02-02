import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Anthony on 2/2/2016.
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
