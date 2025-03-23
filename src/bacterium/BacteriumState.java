package bacterium;

import internationalization.Bundle;
import internationalization.Translatable;

public enum BacteriumState implements Translatable {
    UNBORN, ALIVE, KILLED, DIED_PEACEFULLY;

    @Override
    public String translate() {
        return Bundle.get().getString(this.toString());
    }
}
