package bacterium;

import internationalization.Bundle;
import internationalization.Translatable;

import java.util.Arrays;

public enum BacteriumType implements Translatable {
    CHILL(1), ORDINARY(2), AGGRESSIVE(3), DEMON(5);
    private final int speedFactor;
    BacteriumType(int speedFactor) {
        this.speedFactor = speedFactor;
    }
    public int getSpeedFactor() {
        return speedFactor;
    }

    public static String[] translateAll() {
        return Arrays.stream(BacteriumType.values())
                .map(BacteriumType::toString)
                .map(n -> Bundle.get().getString(n))
                .toArray(String[]::new);
    }

    @Override
    public String translate() {
        return Bundle.get().getString(this.toString());
    }
}
