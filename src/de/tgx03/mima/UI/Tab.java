package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Tab extends JPanel implements PanelParent, ActionListener {

    private final MiMa instance;
    private final Thread MiMaThread;
    private final MiMaPanel[] panels = new MiMaPanel[2];
    private final JButton run = new JButton("Run MiMa");
    private final JButton stop = new JButton("STOP");

    public Tab(MiMa target) {
        this.instance = target;
        this.MiMaThread = new Thread(new ExitNotifier(this.instance, this));
        CommandPanel commandPanel = new CommandPanel(target, this);
        panels[0] = commandPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        run.addActionListener(this);
        buttons.add(run);
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        stop.addActionListener(this);
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
        if (e.getSource() == run) {
            runMiMa();
        } else if (e.getSource() == stop) {
            stopMiMa();
        }
    }

    private void runMiMa() {
        if (!MiMaThread.isAlive()) {
            MiMaThread.start();
        }
    }

    private void stopMiMa() {
        if (MiMaThread.isAlive()) {
            instance.exit();
        }
    }

    private static class ExitNotifier implements Runnable {

        private final Runnable instance;
        private final Tab parent;

        public ExitNotifier(Runnable instance, Tab parent) {
            this.instance = instance;
            this.parent = parent;
        }

        @Override
        public void run() {
            instance.run();
            parent.update();
        }
    }
}
