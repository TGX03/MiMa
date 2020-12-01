package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * The main window of the application
 */
public class MainWindow extends JFrame implements ActionListener {

    private final JMenuBar topMenu = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem emptyMiMa = new JMenuItem("Create new empty MiMa");
    private final JMenuItem loadMiMa = new JMenuItem("Load MiMa from files");
    private final JMenuItem saveCommands = new JMenuItem("Save instructions to file");
    private final JMenuItem saveMemory = new JMenuItem("Save memory to file");
    private final JMenuItem closeMiMa = new JMenuItem("Close MiMa");
    private final JFileChooser chooser = new JFileChooser();
    private final JTabbedPane tabber = new JTabbedPane();

    /**
     * Creates and shows a new window
     */
    public MainWindow() {
        // Initialise the top menu
        topMenu.add(fileMenu);
        fileMenu.add(emptyMiMa);
        fileMenu.add(loadMiMa);
        fileMenu.add(saveCommands);
        fileMenu.add(saveMemory);
        fileMenu.add(closeMiMa);
        setJMenuBar(topMenu);

        // Add actions to the buttons
        emptyMiMa.addActionListener(this);
        loadMiMa.addActionListener(this);
        saveCommands.addActionListener(this);
        saveMemory.addActionListener(this);
        closeMiMa.addActionListener(this);

        this.add(tabber);

        // Initialize with an empty MiMa to set correct size
        createEmptyMiMa();
        this.pack();
        tabber.remove(0);

        this.setTitle("MiMa Emulator");
        this.setVisible(true);
    }

    /**
     * Launches the program and shows the window
     * @param args ignored
     */
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == emptyMiMa) {
            createEmptyMiMa();
        } else if (e.getSource() == loadMiMa) {
            loadMiMa();
        }else if (e.getSource() == saveCommands) {
            saveCommands();
        } else if (e.getSource() == saveMemory) {
            saveMemory();
        } else if (e.getSource() == closeMiMa) {
            closeMiMa();
        }
    }

    /**
     * Create a new tab with an empty MiMa
     */
    private void createEmptyMiMa() {
        Tab newTab = new Tab(new MiMa(new String[0]));
        tabber.add(String.valueOf(tabber.getTabCount()), newTab);
    }

    /**
     * Load a MiMa from one or two files
     */
    private void loadMiMa() {
        MiMaLoader loader = new MiMaLoader();
        if (loader.success()) {
            Tab newTab = new Tab(loader.getCreatedMiMa());
            tabber.add(String.valueOf(tabber.getTabCount()), newTab);
        }
    }

    /**
     * Save the commands of the currently selected MiMa to a file
     */
    private void saveCommands() {
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            // Get the commands of the currently shown MiMa
            Tab selectedTab = (Tab) tabber.getSelectedComponent();
            MiMa instance = selectedTab.getInstance();
            Command[] commands = instance.getCommands();
            File target = chooser.getSelectedFile(); // Get the destination

            try {

                // Create the file if it doesn't exist
                if (!target.exists()) {
                    target.createNewFile();
                }

                // Write to the file
                PrintWriter writer = new PrintWriter(target);
                for (Command command : commands) {
                    String value = command.toString();
                    value = value.replaceFirst(": ", "");
                    writer.println(value);
                }
                writer.close();

            } catch (IOException e) {
                // Show an error window when something goes wrong
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Save the memory of the currently active MiMa to a file
     */
    private void saveMemory() {
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            // Get the currently active MiMa and its memory
            Tab selectedTab = (Tab) tabber.getSelectedComponent();
            MiMa instance = selectedTab.getInstance();
            Set<Map.Entry<String, Integer>> entries = instance.getMemory();
            File target = chooser.getSelectedFile();

            try {

                // Create the file if it doesn't exist
                if (!target.exists()) {
                    target.createNewFile();
                }

                // Create the Strings of each memory entry
                ArrayList<String> strings = new ArrayList<>(entries.size());
                for (Map.Entry<String, Integer> entry : entries) {
                    strings.add(entry.getKey() + " " + entry.getValue());
                }

                // Write the generated string to the file
                PrintWriter writer = new PrintWriter(target);
                strings.sort(Comparator.naturalOrder());
                for (String string : strings) {
                    writer.println(string);
                }
                writer.close();

            } catch (IOException e) {
                // Show an error window when something goes wrong
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Remove a MiMa instance from this tool
     */
    private void closeMiMa() {
        int selectedTab = tabber.getSelectedIndex();
        tabber.remove(selectedTab);
    }
}
