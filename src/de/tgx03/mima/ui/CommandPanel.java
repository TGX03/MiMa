package de.tgx03.mima.ui;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A panel that handles the commands of a MiMa
 * E.g. adding, removing and dealing with breakpoints
 */
public class CommandPanel extends JPanel implements ActionListener, MiMaPanel, ListSelectionListener {

    private final JButton addCommand = new JButton("Add command");
    private final JButton removeCommand = new JButton("Remove selected Command");
    private final JButton breakpoint = new JButton("Remove breakpoint");    // Remove at the beginning because otherwise the remove option doesn't fit
    private final JLabel commandRepresentation = new JLabel();
    private final JComboBox<String> representations = new JComboBox<>(new String[]{"Binary", "Octal", "Decimal", "Hexadecimal"});
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> commandList = new JList<>(listModel);
    private final PanelParent parent;
    private final MiMa instance;

    /**
     * Creates a new panel which is responsible for the commands of a given MiMa
     *
     * @param mima   The MiMa this panel is responsible for
     * @param parent The parent of this panel
     */
    public CommandPanel(@NotNull MiMa mima, @NotNull PanelParent parent) {

        // Assign objects
        this.parent = parent;
        this.instance = mima;

        // Configure the list to color elements correctly and only allow selection of one element
        commandList.setCellRenderer(new ListColorRenderer(this.instance));
        commandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        commandList.addListSelectionListener(this);

        // Set the layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add data to the list and add the list to this panel
        for (Command command : mima.getCommands()) {
            listModel.addElement(command.toString());
        }
        this.add(new JScrollPane(commandList));

        // Configure the buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        addCommand.addActionListener(this);
        removeCommand.addActionListener(this);
        breakpoint.addActionListener(this);
        buttons.add(addCommand);
        buttons.add(removeCommand);
        buttons.add(breakpoint);
        this.add(buttons);

        JPanel commandRepresentation = new JPanel();
        commandRepresentation.setLayout(new FlowLayout());
        commandRepresentation.add(this.representations);
        this.representations.addActionListener(this);
        commandRepresentation.add(this.commandRepresentation);
        this.add(commandRepresentation);
    }

    @Override
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
        } else if (e.getSource() == breakpoint) {
            manageBreakpoint();
        } else if (e.getSource() == representations) {
            updateCommandRepresentation();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selected = commandList.getSelectedIndex();
        breakpoint.setPreferredSize(breakpoint.getSize());
        if (this.instance.isBreakpoint(selected)) {
            breakpoint.setText("Remove breakpoint");
        } else {
            breakpoint.setText("Add breakpoint");
        }
        updateCommandRepresentation();
    }

    /**
     * Add a new command to this MiMa
     */
    private void addCommand() {
        CommandCreator creator = new CommandCreator(instance);
        parent.update();
    }

    /**
     * Remove the currently selected command from this MiMa
     */
    private void removeCommand() {
        int selectedCommand = commandList.getSelectedIndex();
        instance.removeCommand(selectedCommand);
        parent.update();
    }

    /**
     * Depending on the currently selected breakpoint, either add or remove a breakpoint
     */
    private void manageBreakpoint() {
        int selected = commandList.getSelectedIndex();
        if (selected == -1) {
            return;
        }
        if (this.instance.isBreakpoint(selected)) {
            this.instance.removeBreakpoint(selected);
        } else {
            this.instance.addBreakpoint(selected);
        }
        parent.update();
    }

    /**
     * Update the representation of the currently selected command
     */
    private void updateCommandRepresentation() {
        int index = commandList.getSelectedIndex();
        if (index < 0) {
            commandRepresentation.setText("");
        } else {
            int value = this.instance.getCommands()[index].hashCode();
            switch (representations.getSelectedIndex()) {
                case 0 -> commandRepresentation.setText(Integer.toBinaryString(value));
                case 1 -> commandRepresentation.setText(Integer.toOctalString(value));
                case 2 -> commandRepresentation.setText(Integer.toString(value));
                case 3 -> commandRepresentation.setText(Integer.toHexString(value));
                default -> throw new IllegalArgumentException();
            }
        }
    }

    /**
     * The renderer for the List responsible for coloring it correctly
     */
    private static class ListColorRenderer extends DefaultListCellRenderer {

        private final MiMa instance;

        /**
         * Creates a new renderer for a specific MiMa
         *
         * @param mima The instance this renderer may refer to
         */
        public ListColorRenderer(@NotNull MiMa mima) {
            this.instance = mima;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component result = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (index == this.instance.currentCommand()) {
                result.setBackground(Color.GREEN);
            }
            if (this.instance.isBreakpoint(index)) {
                result.setForeground(Color.RED);
            }
            return result;
        }
    }
}
