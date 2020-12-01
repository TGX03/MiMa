package de.tgx03.mima.ui;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The dialog for loading a new MiMa from its command file
 * and optionally data file
 */
class MiMaLoader extends JDialog implements ActionListener {

    private static final Dimension SIZE = new Dimension(500, 130);
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(300, 20);
    private static final Dimension VERTICAL_SPACE = new Dimension(0, 10);
    private static final Dimension HORIZONTAL_SPACE = new Dimension(10, 0);

    private final JTextField commandPath = new JTextField("Instruction File");
    private final JTextField dataPath = new JTextField("Data File");
    private final JButton chooseCommands = new JButton("Choose instruction file");
    private final JButton chooseData = new JButton("Choose Data file (optional)");
    private final JButton ok = new JButton("OK");
    private final JButton cancel = new JButton("cancel");
    private final JFileChooser chooser = new JFileChooser();

    private MiMa createdMiMa;

    public MiMaLoader() {
        this.setTitle("Load MiMa");
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // This gets divided in three parallel columns
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));

        // Create the labels for the text fields
        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        labels.add(new JLabel("Instructions:"));
        labels.add(Box.createRigidArea(VERTICAL_SPACE));
        labels.add(new JLabel("Data:"));
        settings.add(labels);

        // Create text fields
        settings.add(Box.createRigidArea(HORIZONTAL_SPACE));
        JPanel textFields = new JPanel();
        textFields.setLayout(new BoxLayout(textFields, BoxLayout.Y_AXIS));
        commandPath.setMaximumSize(TEXT_FIELD_SIZE);
        dataPath.setMaximumSize(TEXT_FIELD_SIZE);
        commandPath.setMinimumSize(TEXT_FIELD_SIZE);
        dataPath.setMinimumSize(TEXT_FIELD_SIZE);
        textFields.add(commandPath);
        textFields.add(Box.createRigidArea(VERTICAL_SPACE));
        textFields.add(dataPath);
        settings.add(textFields);

        // Create buttons to launch file chooser
        settings.add(Box.createRigidArea(HORIZONTAL_SPACE));
        JPanel choosers = new JPanel();
        choosers.setLayout(new BoxLayout(choosers, BoxLayout.Y_AXIS));
        choosers.add(chooseCommands);
        choosers.add(Box.createRigidArea(VERTICAL_SPACE));
        choosers.add(chooseData);
        settings.add(choosers);

        this.add(settings);

        // Create the ok and cancel button
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(cancel);
        buttons.add(ok);
        this.add(buttons);

        // Register this with the buttons
        chooseCommands.addActionListener(this);
        chooseData.addActionListener(this);
        cancel.addActionListener(this);
        ok.addActionListener(this);

        // Set the size of this and put it in the foreground
        this.setSize(SIZE);
        this.setResizable(false);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setVisible(true);
    }

    public boolean success() {
        return createdMiMa != null;
    }

    public MiMa getCreatedMiMa() {
        return createdMiMa;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseCommands) {
            selectCommandFile();
        } else if (e.getSource() == chooseData) {
            selectDataFile();
        } else if (e.getSource() == ok ) {
            generateMiMa();
        } else if (e.getSource() == cancel) {
            cancel();
        }
    }

    private void generateMiMa() {
        try {
            java.util.List<String> instructions = Files.readAllLines(Paths.get(this.commandPath.getText()));
            try {
                java.util.List<String> data = Files.readAllLines(Paths.get(this.dataPath.getText()));
                createdMiMa = new MiMa(instructions.toArray(new String[0]), data.toArray(new String[0]));
            } catch (IOException e) {
                createdMiMa = new MiMa(instructions.toArray(new String[0]));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void cancel() {
        this.setVisible(false);
        this.dispose();
    }

    private void selectCommandFile() {
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            commandPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void selectDataFile() {
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            dataPath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
