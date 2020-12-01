package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Tab extends JPanel implements PanelParent, ActionListener {

    private static final Dimension VERTICAL_SPACER = new Dimension(0, 10);
    private static final Dimension HORIZONTAL_SPACER = new Dimension(10, 0);

    private final MiMa instance;
    private final Thread MiMaThread;
    private final MiMaPanel[] panels = new MiMaPanel[2];
    private final JButton run = new JButton("Run MiMa");
    private final JButton step = new JButton("Run next command");
    private final JButton stop = new JButton("STOP");
    private final JButton resetCommands = new JButton("Go to first instruction");
    private final JButton resetMiMa = new JButton("Reset Memory and go to first instruction");

    public Tab(MiMa target) {
        this.instance = target;
        this.MiMaThread = new Thread(new ExitNotifier(this.instance, this));
        run.addActionListener(this);
        step.addActionListener(this);
        stop.addActionListener(this);
        resetCommands.addActionListener(this);
        resetMiMa.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(Box.createRigidArea(VERTICAL_SPACER));
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(run);
        buttons.add(Box.createRigidArea(HORIZONTAL_SPACER));
        buttons.add(step);
        buttons.add(Box.createRigidArea(HORIZONTAL_SPACER));
        buttons.add(stop);
        this.add(buttons);
        this.add(Box.createRigidArea(VERTICAL_SPACER));

        JPanel resetButtons = new JPanel();
        resetButtons.setLayout(new BoxLayout(resetButtons, BoxLayout.X_AXIS));
        resetButtons.add(resetCommands);
        resetButtons.add(Box.createRigidArea(HORIZONTAL_SPACER));
        resetButtons.add(resetMiMa);
        this.add(resetButtons);
        this.add(Box.createRigidArea(VERTICAL_SPACER));

        CommandPanel commandPanel = new CommandPanel(this.instance, this);
        MemoryPanel memoryPanel = new MemoryPanel(this.instance, this);
        panels[0] = commandPanel;
        panels[1] = memoryPanel;

        JPanel panels = new JPanel();
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, commandPanel, memoryPanel);
        panels.add(split);
        this.add(panels);
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
        } else if (e.getSource() == step) {
            instance.runSingleCommand();
            update();
        } else if (e.getSource() == resetCommands) {
            instance.resetInstructions();
            update();
        } else if (e.getSource() == resetMiMa) {
            instance.reset();
            update();
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

    protected MiMa getInstance() {
        return this.instance;
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
