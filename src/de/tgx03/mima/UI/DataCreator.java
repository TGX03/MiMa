package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Dialog to create new Memory entries for the MiMa
 */
class DataCreator extends JDialog implements ActionListener {

    private final JTextField address = new JTextField("Address");
    private final JTextField value = new JTextField("Value");
    private final JButton ok = new JButton("OK");
    private final JButton cancel = new JButton("cancel");
    private final MiMa instance;

    /**
     * Creates a new Dialog to add Data to a provided MiMa
     * @param target The MiMa to create a command for
     */
    public DataCreator(MiMa target) {
        this.instance = target;

        // Create text fields
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.add(address);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        this.add(value);
        this.add(Box.createRigidArea(new Dimension(0, 5)));

        // Create buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        ok.addActionListener(this);
        buttons.add(ok);
        cancel.addActionListener(this);
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(cancel);

        // Set window properties
        this.add(buttons);
        this.pack();
        this.setResizable(false);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setTitle("Add Data");
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
     * When pressing the ok button
     */
    private void ok() {
        try {
            Integer value = Integer.decode(this.value.getText());
            instance.addData(this.address.getText(), value);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setVisible(false);
            this.dispose();
        }
    }

    /**
     * When pressing the cancel button
     */
    private void cancel() {
        this.setVisible(false);
        this.dispose();
    }
}
