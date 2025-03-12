package internationalization;

public interface TranslatableEnum {

    default String translate() {
        return Bundle.get().getString(this.toString());
    }

}
