package bacterium;

import internationalization.Bundle;
import pres.PetriTableModel;
import java.awt.Color;
import java.util.*;
import java.util.List;

public class BacteriumGenerator {
    private static final List<String> colorNames = new LinkedList<>(Arrays.asList(
                    "BLUE", "CYAN", "GREEN", "LIGHT_GRAY",
                    "MAGENTA", "ORANGE", "PINK", "RED", "WHITE", "YELLOW"
    ));
    private static PetriTableModel petriTableModel = null;
    private static final Random random = new Random();
    public static Bacterium generateBacterium(BacteriumType bacteriumType) {
        ResourceBundle bundle = Bundle.get();
        if (petriTableModel == null)
            throw new RuntimeException(bundle.getString("modelNotLoaded"));
        else if (colorNames.isEmpty()) {
            throw new RuntimeException(bundle.getString("allColorsUsed"));
        }
        String colorName = colorNames.remove(random.nextInt(colorNames.size()));
        Color color;
        try {
            color = (Color) Color.class.getField(colorName).get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (bacteriumType == null)
            return new Bacterium(colorName, color, petriTableModel);
        else
            return new Bacterium(colorName, color, petriTableModel, bacteriumType);
    }
    public static void loadTableModel(PetriTableModel petriTableModel) {
        BacteriumGenerator.petriTableModel = petriTableModel;
    }
    public static int getNColors() {
        return colorNames.size();
    }
}
