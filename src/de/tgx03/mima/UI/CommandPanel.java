package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandPanel extends JPanel implements ActionListener, MiMaPanel {

    private final JButton addCommand = new JButton("Add command");
    private final JButton removeCommand = new JButton("Remove selected Command");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> commandList = new JList<>(listModel);
    private final PanelParent parent;
    private final MiMa instance;

    public CommandPanel(MiMa mima, PanelParent parent) {
        this.parent = parent;
        this.instance = mima;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        if (mima != null) {
            for (Command command : mima.getCommands()) {
                listModel.addElement(command.toString());
            }
        }
        this.add(commandList);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        addCommand.addActionListener(this);
        removeCommand.addActionListener(this);
        buttons.add(addCommand);
        buttons.add(removeCommand);
        this.add(buttons);
    }

    public void updateMiMa() {
        listModel.clear();
        for (Command command : instance.getCommands()) {
            listModel.addElement(command.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCommand) {
            addCommand();
        } else if (e.getSource() == removeCommand) {
            removeCommand();
        }
    }

    private void addCommand() {
        CommandCreator creator = new CommandCreator(instance);
        if (creator.validCommandCreated()) {
            Command newCommand = creator.getCreatedCommand();
            int position = creator.commandPosition();
            instance.addCommand(position, newCommand);
            parent.update();
        }
    }

    private void removeCommand() {
        int selectedCommand = commandList.getSelectedIndex();
        instance.removeCommand(selectedCommand);
        parent.update();
    }
}
