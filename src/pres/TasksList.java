package pres;

import internationalization.Bundle;
import bacterium.BacteriumFutureTask;
import bacterium.BacteriumState;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class TasksList extends JScrollPane {
    public TasksList(Dimension dimension, List<BacteriumFutureTask> bacteriumFutureTasks, ExecutorService executorService) {
        ResourceBundle bundle = Bundle.get();
        JPanel taskPanelsContainer = new JPanel();
        taskPanelsContainer.setLayout(new BoxLayout(taskPanelsContainer, BoxLayout.Y_AXIS));

        //Start all button:
        JButton startAllButton = new JButton(bundle.getString("START_ALL"));
        startAllButton.addActionListener(event -> {
            bacteriumFutureTasks.stream()
                    .filter(ft -> ft.getState() == BacteriumState.UNBORN)
                    .forEach(executorService::submit);
        });
        startAllButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        taskPanelsContainer.add(startAllButton);

        //Adding task panels to the container
        Dimension taskPanelDimension = new Dimension(dimension.width, dimension.height / bacteriumFutureTasks.size());
        for (BacteriumFutureTask BacteriumFutureTask : bacteriumFutureTasks) {
            TaskPanel taskPanel = new TaskPanel(BacteriumFutureTask, executorService, taskPanelDimension);
            taskPanelsContainer.add(taskPanel);
        }

        this.setViewportView(taskPanelsContainer);
        this.setPreferredSize(dimension);
    }
}

class TaskPanel extends JPanel {
    private BacteriumFutureTask futureTask;
    private ExecutorService executorService;
    private Dimension dimension;
    private List<JButton> buttons = Arrays.stream(ButtonType.values()).map(enm -> new JButton(enm.toString())).collect(Collectors.toList());
    private HashMap<ButtonType, JTextField> bacteriaOutputs;
    public TaskPanel(BacteriumFutureTask futureTask, ExecutorService executorService, Dimension dimension) {
        this.futureTask = futureTask;
        this.executorService = executorService;
        this.dimension = dimension;
        ResourceBundle bundle = Bundle.get();
        setupPanel(bundle);

        //Initiating thread updating the information about tasks
        initiateUpdater(bundle);
    }
    private void setupPanel(ResourceBundle bundle) {


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Dimension buttonDimension =
                new Dimension(dimension.width - 20 , dimension.height / buttons.size());

        //Name field:
        JTextField textField = new JTextField(futureTask.getName());
        textField.setEditable(false);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBackground(futureTask.getColor());
        this.add(textField);

        //StartCancelPanel
        JPanel startCancelPanel = new JPanel();
        startCancelPanel.setLayout(new FlowLayout());
            //Starting button:
            JButton startButton = new JButton(bundle.getString("START"));
            startButton.addActionListener(event -> {
                if (futureTask.getState() == BacteriumState.UNBORN) {
                    System.out.println(executorService.submit(futureTask));
                    System.out.println(futureTask);
                }

            });
            startCancelPanel.add(startButton);
            //Cancelling button:
            JButton cancelButton = new JButton(bundle.getString("CANCEL"));
            cancelButton.addActionListener(event -> {
                if (!futureTask.isCancelled() && futureTask.getState() != BacteriumState.UNBORN) {
                    System.out.println("Could be cancelled: " + futureTask.cancel(true));
                }
            });
        startCancelPanel.add(cancelButton);
        this.add(startCancelPanel);

        bacteriaOutputs = new HashMap<>();
        bacteriaOutputs.put(
                ButtonType.STATE,
                new JTextField(bundle.getString("STATE") + ": " + futureTask.getState().translate())
        );
        bacteriaOutputs.put(
                ButtonType.CANCEL,
                new JTextField(bundle.getString("CANCELLED") + ": " + futureTask.isCancelled())
        );
        bacteriaOutputs.put(
                ButtonType.RESULT, new JTextField(bundle.getString("RESULT") + ": " + (futureTask.getResult() == -1 ? "" : futureTask.getResult()))
        );
        bacteriaOutputs.put(
                ButtonType.READY, new JTextField(bundle.getString("READY") + ": " + futureTask.isDone())
        );

        bacteriaOutputs.forEach((key, txtField)-> {
           this.add(txtField);
           txtField.setSize(buttonDimension);
           txtField.setEditable(false);
        });
    }
    private void initiateUpdater(ResourceBundle bundle) {
        new Thread(() -> {
            boolean stopUpdating = false;
            while (!Thread.currentThread().isInterrupted() && !stopUpdating) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("Updater interrupted!");
                }
                stopUpdating = updateBacteriaOutputs(bundle);
            }
        }).start();
    }
    private boolean updateBacteriaOutputs(ResourceBundle bundle) {
        bacteriaOutputs.forEach((key, textField) -> {
            switch (key) {
                case STATE:
                    textField.setText(bundle.getString("STATE") + ": " + futureTask.getState().translate());
                    break;
                case CANCEL:
                    textField.setText(bundle.getString("CANCELLED") + ": " + futureTask.isCancelled());
                    break;
                case RESULT:
                    textField.setText(bundle.getString("RESULT") + ": " + (futureTask.getResult() == -1 ? "" : futureTask.getResult()));
                    break;
                case READY:
                    textField.setText(bundle.getString("READY") + ": " + futureTask.isDone());
                    break;
            }
        });

        //True if the updating thread should stop updating - task is already done and nothing will change
        return futureTask.isDone();
    }
}