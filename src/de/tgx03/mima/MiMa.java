package de.tgx03.mima;

import de.tgx03.mima.commands.*;

import java.util.*;

/**
 * Implements a basic MiMa that can be run
 */
public class MiMa implements Runnable {

    private final Command[] commands;
    private final HashMap<String, Integer> map = new HashMap<>();
    private final HashMap<String, Integer> originalMap = new HashMap<>();
    private boolean exit = false;
    private boolean forcedExit = false;
    private int accu;
    int currentCommand = 0;
    private Collection<Integer> breakpoints;
    private boolean broke = false;

    /**
     * Creates a MiMa with its commands but no data
     * Commands must follow "CMD Data"
     *
     * @param commands The commands of this MiMa
     */
    public MiMa(String[] commands) {
        this.commands = initializeCommands(commands, this);
    }

    /**
     * Creates a MiMa with its commands and initial data
     * Commands must follow "CMD Data"
     * Data must follow "Address Data"
     *
     * @param commands The commands of this MiMa
     * @param data     The initial data of this MiMa
     */
    public MiMa(String[] commands, String[] data) {
        this.commands = initializeCommands(commands, this);
        for (String line : data) {
            String[] splitData = line.split(" ");
            map.put(splitData[0], Integer.parseInt(splitData[1]));
        }
        this.originalMap.putAll(this.map);
    }

    /**
     * Converts an array of Strings to an array of commands
     *
     * @param commands The string to be converted
     * @param instance The instance the commands belong to
     * @return An array of all the resulting commands
     */
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

    /**
     * Executes this MiMa completely
     */
    public synchronized void run() {
        while (!exit && (this.breakpoints == null || broke || !this.breakpoints.contains(currentCommand))) {
            broke = false;
            runNextCommand();
        }
        if (this.breakpoints.contains(currentCommand)) {
            broke = true;
        }
    }

    /**
     * Run only the next command
     */
    public synchronized void runSingleCommand() {
        if (!exit) {
            runNextCommand();
        }
    }

    /**
     * Runs the next command in line and deals with its output
     */
    private void runNextCommand() {
        if (currentCommand >= commands.length) {
            exit = true;
            forcedExit = true;
            return;
        }
        int[] commandResult;
        synchronized (map) {
            commandResult = commands[currentCommand].run(accu);
        }
        if (commands[currentCommand].updatesAccu()) {
            accu = commandResult[0];
        }
        if (commandResult[1] != Command.DONT_JUMP) {
            currentCommand = commandResult[1];
        } else {
            currentCommand++;
        }
    }

    /**
     * Resets this MiMa to its initial state
     */
    public synchronized void reset() {
        this.exit = false;
        this.forcedExit = false;
        this.currentCommand = 0;
        this.map.clear();
        this.map.putAll(this.originalMap);
    }

    /**
     * Prepares this MiMa for a re-run, but keeps the Memory
     */
    public synchronized void resetInstructions() {
        this.exit = false;
        this.forcedExit = false;
        this.currentCommand = 0;
    }

    public synchronized void addBreakpoint(int breakpoint) {
        if (this.breakpoints == null) {
            this.breakpoints = new ArrayList<>();
        }
        if (!this.breakpoints.contains(breakpoint)) {
            this.breakpoints.add(breakpoint);
        }
    }

    public synchronized void addBreakpoints(Collection<Integer> breakpoints) {
        if (this.breakpoints == null) {
            this.breakpoints = breakpoints;
        } else {
            this.breakpoints.addAll(breakpoints);
        }
    }

    public synchronized void removeBreakpoint(int breakpoint) {
        if (this.breakpoints != null) {
            this.breakpoints.remove(breakpoint);
        }
    }

    public synchronized void removeBreakpoints(Collection<Integer> breakpoints) {
        this.breakpoints.removeAll(breakpoints);
    }

    /**
     * Tell this MiMa to exit on the next possibility
     */
    public void exit() {
        exit = true;
    }

    @Override
    public String toString() {
        Set<Map.Entry<String, Integer>> keyPairs;
        synchronized (map) {
            keyPairs = map.entrySet();
        }
        ArrayList<String> elements = new ArrayList<>(keyPairs.size());
        for (Map.Entry<String, Integer> pair : keyPairs) {
            elements.add(pair.getKey() + ": " + pair.getValue());
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
}