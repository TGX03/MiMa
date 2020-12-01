package de.tgx03.mima.ui;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A small Dialog in which the user may create a new Command for a MiMa
 */
class CommandCreator extends JDialog implements ActionListener {

    private static final Dimension spacer = new Dimension(10, 5);

    private final JComboBox<String> commandList = new JComboBox<>(new String[]{"ADD", "AND", "EQL", "HALT", "JMN", "JMP", "LDC", "LDV", "NOT", "OR", "RAR", "STV", "XOR"});
    private final JTextField commandValue = new JTextField("Command value");
    private final JTextField position = new JTextField("Position");
    private final JButton ok = new JButton("OK");
    private final JButton cancel = new JButton("Cancel");
    private final MiMa instance;

    /**
     * Launches a new Dialog that asks for the data of the new command
     * and adds it to the MiMa if the User so chooses
     * @param target The MiMa to add the new Command to
     */
    public CommandCreator(@NotNull MiMa target) {
        this.instance = target;

        // Create text fields
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(commandList);
        this.add(Box.createRigidArea(spacer));
        this.add(commandValue);
        this.add(Box.createRigidArea(spacer));
        this.add(position);
        this.add(Box.createRigidArea(spacer));

        // Create buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        ok.addActionListener(this);
        cancel.addActionListener(this);
        buttonPanel.add(ok);
        buttonPanel.add(Box.createRigidArea(spacer));
        buttonPanel.add(cancel);
        this.add(buttonPanel);

        // Set window properties
        this.pack();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setResizable(false);
        this.setTitle("Create Command");
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            ok();
        } else if (e.getSource() == cancel) {
            cancel();
        }
    }

    /**
     * When the user presses the ok button
     */
    private void ok() {
        try {
            Command newCommand = instance.createCommand((String) commandList.getSelectedItem(), commandValue.getText());
            int commandPosition = Integer.parseInt(position.getText());
            this.instance.addCommand(commandPosition, newCommand);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setVisible(false);
            this.dispose();
        }
    }

    /**
     * When the user presses the cancel button
     */
    private void cancel() {
        this.setVisible(false);
        this.dispose();
    }
}
