package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Tab extends JPanel implements PanelParent, ActionListener {

    private final MiMa instance;
    private final MiMaPanel[] panels = new MiMaPanel[2];
    private final JButton run = new JButton("Run MiMa");
    private final JButton stop = new JButton("STOP");

    public Tab(MiMa target) {
        this.instance = target;
        CommandPanel commandPanel = new CommandPanel(target, this);
        panels[0] = commandPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(run);
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(stop);
        this.add(buttons);

        this.add(Box.createRigidArea(new Dimension(0, 10)));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, commandPanel, new JPanel());
        this.add(split);
    }

    @Override
    public void update() {
        for (MiMaPanel panel : panels) {
            panel.updateMiMa();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
