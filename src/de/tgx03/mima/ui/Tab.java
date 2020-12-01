package de.tgx03.mima.ui;

import de.tgx03.mima.MiMa;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A tab which holds a command and memory panel
 * Each MiMa gets its own tab
 */
class Tab extends JPanel implements PanelParent, ActionListener {

    private static final Dimension VERTICAL_SPACER = new Dimension(0, 10);
    private static final Dimension HORIZONTAL_SPACER = new Dimension(10, 0);

    private final MiMa instance;
    private final ExitNotifier runner;
    private final MiMaPanel[] panels = new MiMaPanel[2];
    private final JButton run = new JButton("Run MiMa");
    private final JButton step = new JButton("Run next command");
    private final JButton stop = new JButton("STOP");
    private final JButton resetCommands = new JButton("Go to first instruction");
    private final JButton resetMiMa = new JButton("Reset Memory and go to first instruction");

    private Thread MiMaThread;

    /**
     * Create a new tab holding the controls for a MiMa
     *
     * @param target The MiMa this tab is assigned to
     */
    public Tab(@NotNull MiMa target) {

        // Assign required object
        this.instance = target;
        this.runner = new ExitNotifier(this.instance, this);
        this.MiMaThread = new Thread(this.runner);

        // Register buttons
        run.addActionListener(this);
        step.addActionListener(this);
        stop.addActionListener(this);
        resetCommands.addActionListener(this);
        resetMiMa.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create the top row of buttons for running and stopping the MiMa
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

        // Create the second row for resetting the MiMa
        JPanel resetButtons = new JPanel();
        resetButtons.setLayout(new BoxLayout(resetButtons, BoxLayout.X_AXIS));
        resetButtons.add(resetCommands);
        resetButtons.add(Box.createRigidArea(HORIZONTAL_SPACER));
        resetButtons.add(resetMiMa);
        this.add(resetButtons);
        this.add(Box.createRigidArea(VERTICAL_SPACER));

        // Create the 2 panels
        CommandPanel commandPanel = new CommandPanel(this.instance, this);
        MemoryPanel memoryPanel = new MemoryPanel(this.instance, this);
        panels[0] = commandPanel;
        panels[1] = memoryPanel;

        // Create the split panel and add the 2 panels
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

    /**
     * Start the MiMa in a separate Thread
     */
    private void runMiMa() {
        if (!MiMaThread.isAlive()) {
            // Check if the thread can be started or a new one needs to be created
            if (MiMaThread.getState() != Thread.State.NEW) {
                MiMaThread = new Thread(this.runner);
            }
            MiMaThread.start();
        }
    }

    /**
     * Stop the MiMa
     */
    private void stopMiMa() {
        if (MiMaThread.isAlive()) {
            instance.exit();
        }
    }

    /**
     * Returns the MiMa this tab is managing.
     * Gets used by the main windows for save operations
     *
     * @return The MiMa of this tab
     */
    protected MiMa getInstance() {
        return this.instance;
    }

    /**
     * A class that runs as a separate thread, but notifies its parent
     * when the thread has finished so the panels can updated
     */
    private static class ExitNotifier implements Runnable {

        private final Runnable instance;
        private final PanelParent parent;

        /**
         * @param instance The Runnable this shall run and then notify the parent
         * @param parent   The parent to notify
         */
        public ExitNotifier(@NotNull Runnable instance, @NotNull PanelParent parent) {
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
