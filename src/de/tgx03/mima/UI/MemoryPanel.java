package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MemoryPanel extends JPanel implements ActionListener, MiMaPanel {

    private static final String SEPARATOR = System.getProperty("line.separator");

    private final MiMa instance;
    private final PanelParent parent;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> dataList = new JList<>(listModel);
    private final JButton add = new JButton("add");
    private final JButton remove = new JButton("remove");

    public MemoryPanel(MiMa mima, PanelParent parent) {
        this.parent = parent;
        this.instance = mima;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        listModel.add(0, accu());
        if (mima != null) {
            String memory = instance.toString();
            String[] splitMemory = memory.split(SEPARATOR);
            listModel.addAll(Arrays.asList(splitMemory));
        }

        dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(new JScrollPane(dataList));

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

    private void removeData() {
        String selected = dataList.getSelectedValue();
        String[] split = selected.split(": ");
        instance.clearData(split[0]);
        parent.update();
    }

    private String accu() {
        return "accu: " + this.instance.getAccu();
    }
}
