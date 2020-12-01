package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CommandCreator extends JDialog implements ActionListener {

    private static final Dimension spacer = new Dimension(10, 5);

    private final JComboBox<String> commandList = new JComboBox<>(new String[]{"ADD", "AND", "EQL", "HALT", "JMN", "JMP", "LDC", "LDV", "NOT", "OR", "RAR", "STV", "XOR"});
    private final JTextField commandValue = new JTextField("Command value");
    private final JTextField position = new JTextField("Position");
    private final JButton ok = new JButton("OK");
    private final JButton cancel = new JButton("Cancel");
    private final MiMa instance;


    private int commandPosition;
    private Command newCommand;
    private boolean success = false;

    public CommandCreator(MiMa target) {
        this.instance = target;
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(commandList);
        this.add(Box.createRigidArea(spacer));
        this.add(commandValue);
        this.add(Box.createRigidArea(spacer));
        this.add(position);
        this.add(Box.createRigidArea(spacer));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        ok.addActionListener(this);
        cancel.addActionListener(this);
        buttonPanel.add(ok);
        buttonPanel.add(Box.createRigidArea(spacer));
        buttonPanel.add(cancel);
        this.add(buttonPanel);
        this.pack();
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setVisible(true);
    }

    public boolean validCommandCreated() {
        return success;
    }

    public int commandPosition() {
        if (success) {
            return this.commandPosition;
        } else {
            throw new IllegalStateException("No valid command was created");
        }
    }

    public Command getCreatedCommand() {
        if (success) {
            return newCommand;
        } else {
            throw new IllegalStateException("No valid command was created");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            ok();
        } else if (e.getSource() == cancel) {
            cancel();
        }
    }

    private void ok() {
        try {
            newCommand = instance.createCommand((String) commandList.getSelectedItem(), commandValue.getText());
            commandPosition = Integer.parseInt(position.getText());
            success = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            success = false;
        } finally {
            this.setVisible(false);
            this.dispose();
        }
    }

    private void cancel() {
        this.setVisible(false);
        this.dispose();
    }
}
