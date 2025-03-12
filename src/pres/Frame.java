package pres;

import internationalization.Bundle;
import bacterium.BacteriumFutureTask;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Frame extends JFrame{
    private final String title = Bundle.get().getString("GAME_TITLE");
    private final Dimension tableDim = new Dimension(600,600);
    private final Dimension listDim = new Dimension(250,600);

    private final PetriTableModel petriTableModel;
    private final List<BacteriumFutureTask> bacTasks;
    private final ExecutorService executorService;
    public Frame(PetriTableModel petriTableModel, List<BacteriumFutureTask> bacteriumFutureTasks, ExecutorService executorService) {
        this.petriTableModel = petriTableModel;
        this.executorService = executorService;
        this.bacTasks = bacteriumFutureTasks;
    }
    public void setup() {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(new Dimension(tableDim.width + listDim.width + 50,
                Math.max(tableDim.height, listDim.height) +  super.getInsets().top + 25));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        PetriTable petriTable = new PetriTable(tableDim,petriTableModel);
        TasksList tasksList = new TasksList(listDim, bacTasks, executorService);

        add(petriTable);
        add(tasksList);
    }
}
