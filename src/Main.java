/**
 *
 *  @author Kamil Stefański
 *
 */

import bacterium.Bacterium;
import bacterium.BacteriumFutureTask;
import bacterium.BacteriumGenerator;
import internationalization.Bundle;
import pres.Frame;
import pres.PetriTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
  private static final int sideNCells = 40;
  private static final Color boardColor = Color.BLACK;
  private static final String iconPath = "src/zad1/images/bacteriaIcon.jpg";
  private static final String bundlePath = "Messages";
  public static void main(String[] args) {
    PetriTableModel petriTableModel = new PetriTableModel(sideNCells, boardColor);
    BacteriumGenerator.loadTableModel(petriTableModel);
    ImageIcon bacteriaIcon = new ImageIcon(iconPath);
    //Creating bacteria:
    List<Bacterium> bacteria = UserInterface.promptForBacteria(bundlePath, bacteriaIcon);

    List<BacteriumFutureTask>
            bacteriumFutureTasks = bacteria.stream()
            .map(bac -> new BacteriumFutureTask(bac.toString(), bac.getColor(), bac))
            .collect(Collectors.toList());

    ExecutorService executorService = Executors.newCachedThreadPool();

    Frame frame = new Frame(petriTableModel, bacteriumFutureTasks, executorService);
    SwingUtilities.invokeLater(frame::setup);

    executorService.submit(() -> {
      do {
        try {
          Thread.sleep(250);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }while (bacteriumFutureTasks.stream().anyMatch(ft -> !ft.isDone()));
      int[] counter= {1};

      SwingUtilities.invokeLater(() ->
              JOptionPane.showMessageDialog(frame,
                      bacteriumFutureTasks.stream()
                      .sorted(Comparator.comparing(BacteriumFutureTask::getResult).reversed())
                      .map(ft -> {
                        String toReturn = String.format("%d. %s: %s", counter[0], ft.getName(), ft.getResult());
                        counter[0] = counter[0] + 1;
                        return toReturn;
                      })
                      .collect(Collectors.joining("\n")),
                      Bundle.get().getString("RANKING"),
                      JOptionPane.INFORMATION_MESSAGE
              )
      );
    });
  }
}

