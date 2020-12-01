package de.tgx03.mima.ui;

import de.tgx03.mima.MiMa;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * The panel responsible for showing and editing a MiMa's memory
 */
public class MemoryPanel extends JPanel implements ActionListener, MiMaPanel {

    private static final String SEPARATOR = System.getProperty("line.separator");

    private final MiMa instance;
    private final PanelParent parent;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> dataList = new JList<>(listModel);
    private final JButton add = new JButton("add");
    private final JButton remove = new JButton("remove");

    /**
     * Creates a new Memory panel responsible for a MiMa's memory
     * @param mima The MiMa instance this panel is responsible for
     * @param parent The parent of this panel
     */
    public MemoryPanel(@NotNull MiMa mima, @NotNull PanelParent parent) {

        // Assign the required objects
        this.parent = parent;
        this.instance = mima;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add the memory of the MiMa to the list
        listModel.add(0, accu());
        String memory = instance.toString();
        String[] splitMemory = memory.split(SEPARATOR);
        listModel.addAll(Arrays.asList(splitMemory));

        // Add the list
        dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(dataList));

        // Create the add and remove buttons
        JPanel buttons = new JPanel();
        add.addActionListener(this);
        buttons.add(add);
        remove.addActionListener(this);
        buttons.add(remove);
        this.add(buttons);
    }

    @Override
    public void updateMiMa() {
        listModel.clear();
        listModel.add(0, this.accu());
        String memory = instance.toString();
        String[] splitMemory = memory.split(SEPARATOR);
        listModel.addAll(Arrays.asList(splitMemory));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) {
            new DataCreator(this.instance);
            parent.update();
        } else if (e.getSource() == remove) {
            removeData();
        }
    }

    /**
     * Remove the currently selected value from memory
     */
    private void removeData() {
        String selected = dataList.getSelectedValue();
        if (selected != null) {
            String[] split = selected.split(": ");
            instance.clearData(split[0]);
            parent.update();
        }
    }

    /**
     * @return Create a String representing the accu of the MiMa
     */
    private String accu() {
        return "accu: " + this.instance.getAccu();
    }
}
