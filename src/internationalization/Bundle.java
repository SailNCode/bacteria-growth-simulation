package internationalization;

import java.util.ResourceBundle;

public class Bundle {
    private static ResourceBundle BUNDLE = null;

    public static void setup(String baseName) {
        BUNDLE = ResourceBundle.getBundle("resources.config." + baseName);
    }
    public static ResourceBundle get() {
        if (BUNDLE == null)
            throw new RuntimeException("Bundle not yet setup!");
        return BUNDLE;
    }
}
