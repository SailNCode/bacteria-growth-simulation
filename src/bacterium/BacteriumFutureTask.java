package bacterium;

import java.awt.*;
import java.util.concurrent.FutureTask;

public class BacteriumFutureTask extends FutureTask<Integer> {
    private final Bacterium bacterium;
    private final String name;
    private final Color color;
    public BacteriumFutureTask(String name, Color color, Bacterium bacterium) {
        super( bacterium);
        this.bacterium = bacterium;
        this.color = color;
        this.name = name;
    }
    public BacteriumState getState() {
        if (!bacterium.isStarted() && !isDone())
            return BacteriumState.UNBORN;
        if (!isDone())
            return BacteriumState.ALIVE;
        if (isCancelled())
            return BacteriumState.KILLED;
        if (isDone() && !isCancelled())
            return BacteriumState.DIED_PEACEFULLY;
        throw new IllegalArgumentException("Unknown type!");
    }
    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public int getResult() {
        try {
            if (getState() == BacteriumState.DIED_PEACEFULLY) {
                return get();
            } else if (getState() == BacteriumState.KILLED) {
                synchronized (bacterium) {
                    return bacterium.getSquaresOccupied();
                }
            }
        } catch (Exception e) {
            System.err.println("Exception in BacteriumFutureTask: " + e);
        }
        return -1;
    }
}
