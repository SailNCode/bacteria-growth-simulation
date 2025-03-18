import bacterium.Bacterium;
import bacterium.BacteriumGenerator;
import bacterium.BacteriumType;
import internationalization.Bundle;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserInterface {
    public static List<Bacterium> promptForBacteria(String bundlePath, ImageIcon bacteriaIcon) {
        List<Bacterium> bacteriumTasks = new ArrayList<>();
        Object[] options = {"Polish", "English"};
        int selectedOption = JOptionPane.showOptionDialog(
                null,
                "Select a language:",
                "Language Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selectedOption == -1) {
            System.exit(0);
        }
        switch (selectedOption) {
            case 0:
                Locale.setDefault(new Locale("pl"));
                break;
            default:
                Locale.setDefault(Locale.ENGLISH);
        }
        Bundle.setup(bundlePath);
        ResourceBundle bundle = Bundle.get();
        //Getting number of bacteria from user:
        int nBacteria = 0;
        do {
            try {
                //"Input number of bacteria in range [1,10]"
                String input = (String) JOptionPane.showInputDialog(null,
                        bundle.getString("nBacteriaInput"),
                        bundle.getString("GAME_TITLE"),
                        JOptionPane.QUESTION_MESSAGE,
                        bacteriaIcon,
                        null,
                        "10");
                if (input == null) {
                    System.exit(0);
                }
                nBacteria = Integer.parseInt(input);
            } catch (NumberFormatException e) {}
        } while (nBacteria < 1 || nBacteria > BacteriumGenerator.getNColors());
        //Getting number of bacteria types from user:
        String input;
        String[] splitInput;
        int[] nGivenType = new int[BacteriumType.values().length];

        String exemplaryTypeInput = getExemplaryInput(BacteriumType.values().length, nBacteria);
        do {
            try {
                input = (String) JOptionPane.showInputDialog(null,
                        bundle.getString("nBacteriaTypeInput") + ":\n" + Arrays.toString(BacteriumType.translateAll()),
                        bundle.getString("GAME_TITLE"),
                        JOptionPane.QUESTION_MESSAGE,
                        bacteriaIcon,
                        null,
                        exemplaryTypeInput);
                if (input == null)
                    System.exit(0);
                splitInput = input.split(",");
                if (splitInput.length != BacteriumType.values().length)
                    continue;
                nGivenType = Arrays.stream(splitInput)
                        .map(String::trim)
                        .mapToInt(Integer::parseInt)
                        .toArray();
            } catch (NumberFormatException e) {}
        } while (Arrays.stream(nGivenType).sum() != nBacteria || Arrays.stream(nGivenType).anyMatch(value -> value < 0));

        int[] finalNGivenType = nGivenType;
        Arrays.stream(BacteriumType.values()).forEach(type -> {
            int nLeftToGenerate = finalNGivenType[type.ordinal()];
            while (nLeftToGenerate > 0) {
                bacteriumTasks.add(BacteriumGenerator.generateBacterium(type));
                nLeftToGenerate --;
            }
        });
        return bacteriumTasks;
    }
    private static String getExemplaryInput(int nElements, int sum) {
        List<Integer> ints = new ArrayList<>();
        for (int i = nElements; i > 0; i--) {
            ints.add(sum / nElements);
        }
        int toDistribute = sum - ints.stream().mapToInt(Integer::intValue).sum();
        for (int i = 0; i < ints.size() && toDistribute > 0; i++, toDistribute--) {
            ints.set(i, ints.get(i) + 1);
        }
        return ints.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
