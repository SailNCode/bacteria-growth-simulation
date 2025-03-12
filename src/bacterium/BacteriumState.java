package bacterium;

import internationalization.Bundle;
import internationalization.TranslatableEnum;

import java.util.Arrays;

public enum BacteriumState implements TranslatableEnum {
    UNBORN, ALIVE, KILLED, DIED_PEACEFULLY;
    public static String[] translateAll() {
        return Arrays.stream(BacteriumState.values())
                .map(BacteriumState::toString)
                .map(n -> Bundle.get().getString(n))
                .toArray(String[]::new);
    }
}
