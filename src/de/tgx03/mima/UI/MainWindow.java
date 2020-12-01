package de.tgx03.mima.UI;

import de.tgx03.mima.MiMa;
import de.tgx03.mima.commands.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class MainWindow extends JFrame implements ActionListener {

    private static final Dimension initialSize = new Dimension(500, 500);

    private final JMenuBar topMenu = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem emptyMiMa = new JMenuItem("Create new empty MiMa");
    private final JMenuItem loadMiMa = new JMenuItem("Load MiMa from files");
    private final JMenuItem saveCommands = new JMenuItem("Save instructions to file");
    private final JMenuItem saveMemory = new JMenuItem("Save memory to file");
    private final JMenuItem closeMiMa = new JMenuItem("Close MiMa");
    private final JFileChooser chooser = new JFileChooser();
    private final JTabbedPane tabber = new JTabbedPane();

    public MainWindow() {
        topMenu.add(fileMenu);
        fileMenu.add(emptyMiMa);
        fileMenu.add(loadMiMa);
        fileMenu.add(saveCommands);
        fileMenu.add(saveMemory);
        fileMenu.add(closeMiMa);
        emptyMiMa.addActionListener(this);
        loadMiMa.addActionListener(this);
        saveCommands.addActionListener(this);
        saveMemory.addActionListener(this);
        closeMiMa.addActionListener(this);
        setJMenuBar(topMenu);

        this.add(tabber);

        this.setSize(initialSize);
        this.setVisible(true);
    }

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

    private void createEmptyMiMa() {
        Tab newTab = new Tab(new MiMa(new String[0]));
        tabber.add(newTab);
    }

    private void loadMiMa() {
        MiMaLoader loader = new MiMaLoader();
        if (loader.success()) {
            Tab newTab = new Tab(loader.getCreatedMiMa());
            tabber.add(newTab);
        }
    }

    private void saveCommands() {
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            Tab selectedTab = (Tab) tabber.getSelectedComponent();
            MiMa instance = selectedTab.getInstance();
            Command[] commands = instance.getCommands();
            File target = chooser.getSelectedFile();
            try {
                if (!target.exists()) {
                    target.createNewFile();
                }
                PrintWriter writer = new PrintWriter(target);
                for (Command command : commands) {
                    String value = command.toString();
                    value = value.replaceFirst(": ", "");
                    writer.println(value);
                }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveMemory() {
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            Tab selectedTab = (Tab) tabber.getSelectedComponent();
            MiMa instance = selectedTab.getInstance();
            Set<Map.Entry<String, Integer>> entries = instance.getMemory();
            File target = chooser.getSelectedFile();
            try {
                if (!target.exists()) {
                    target.createNewFile();
                }
                ArrayList<String> strings = new ArrayList<>(entries.size());
                for (Map.Entry<String, Integer> entry : entries) {
                    strings.add(entry.getKey() + " " + entry.getValue());
                }
                PrintWriter writer = new PrintWriter(target);
                strings.sort(Comparator.naturalOrder());
                for (String string : strings) {
                    writer.println(string);
                }
                writer.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void closeMiMa() {
        int selectedTab = tabber.getSelectedIndex();
        tabber.remove(selectedTab);
    }
}
