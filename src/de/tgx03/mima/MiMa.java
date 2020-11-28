package de.tgx03.mima;

import de.tgx03.mima.commands.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class MiMa implements Runnable {

    private final Command[] commands;
    private final HashMap<String, Integer> map = new HashMap<>();
    private boolean exit = false;
    private boolean forcedExit = false;
    private int accu;

    public MiMa(String[] commands) {
        this.commands = initializeCommands(commands, this);;
    }

    public MiMa(String[] commands, String[] data) {
        this.commands = initializeCommands(commands, this);
        for (String line : data) {
            String[] splitData = line.split(" ");
            map.put(splitData[0], Integer.parseInt(splitData[1]));
        }
    }

    public synchronized void run() {
        int currentCommand = 0;
        while (!exit) {
            if (currentCommand >= commands.length) {
                exit = true;
                forcedExit = true;
                return;
            }
            int[] commandResult = commands[currentCommand].run(accu);
            if (commands[currentCommand].updatesAccu()) {
                accu = commandResult[0];
            }
            if (commandResult[1] != Command.DONT_JUMP) {
                currentCommand = commandResult[1];
            } else {
                currentCommand++;
            }
        }
    }

    public void exit() {
        exit = true;
    }

    public String toString() {
        ArrayList<String> elements;
        synchronized (this) {
            Set<String> keys = map.keySet();
            if (keys.size() == 0) {
                return "";
            }
            elements = new ArrayList<>(keys.size());
            keys.forEach(key -> {
                int value = map.get(key);
                elements.add(key + ": " + value);
            });
        }
        elements.sort(Comparator.naturalOrder());
        StringBuilder result = new StringBuilder(elements.get(0).length() * elements.size());
        String lineSeparator = System.getProperty("line.separator");
        for (String string : elements) {
            result.append(string);
            result.append(lineSeparator);
        }
        if (forcedExit) {
            result.append("MiMa WAS FORCEFULLY TERMINATED BECAUSE OF AN ERROR");
        }
        return result.toString();
    }

    private static Command[] initializeCommands(String[] commands, MiMa instance) {
        Command[] interpretedCommands = new Command[commands.length];
        final HALT staticHalt = new HALT(instance);
        final NOT staticNot = new NOT();
        final RAR staticRar = new RAR();
        for (int i = 0; i < commands.length; i++) {
            String[] currentCommand = commands[i].split(" ");
            Command command;
            switch (currentCommand[0]) {
                case "ADD" -> command = new ADD(currentCommand[1], instance.map);
                case "AND" -> command = new AND(currentCommand[1], instance.map);
                case "EQL" -> command = new EQL(currentCommand[1], instance.map);
                case "HALT" -> command = staticHalt;
                case "JMN" -> command = new JMN(currentCommand[1], instance.map);
                case "JMP" -> command = new JMP(currentCommand[1], instance.map);
                case "LDC" -> command = new LDC(Integer.decode(currentCommand[1]));
                case "LDV" -> command = new LDV(currentCommand[1], instance.map);
                case "NOT" -> command = staticNot;
                case "OR" -> command = new OR(currentCommand[1], instance.map);
                case "RAR" -> command = staticRar;
                case "STV" -> command = new STV(currentCommand[1], instance.map);
                case "XOR" -> command = new XOR(currentCommand[1], instance.map);
                default -> throw new IllegalArgumentException(currentCommand[0] + " is not a valid MiMa instruction");
            }
            interpretedCommands[i] = command;
        }
        return interpretedCommands;
    }
}