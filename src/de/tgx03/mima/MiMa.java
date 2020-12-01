package de.tgx03.mima;

import de.tgx03.mima.commands.*;

import java.util.*;

/**
 * Implements a basic MiMa that can be run
 */
public class MiMa implements Runnable {

    private static final int MAX_DATA_VALUE = 8388607;
    private static final int MIN_DATA_VALUE = -8388608;

    private final ArrayList<Command> commands;
    private final HashMap<String, Integer> map = new HashMap<>();
    private final HashMap<String, Integer> originalMap = new HashMap<>();
    private volatile boolean exit = false;
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
        Command[] loadedCommands = initializeCommands(commands, this);
        this.commands = new ArrayList<>(loadedCommands.length);
        this.commands.addAll(Arrays.asList(loadedCommands));
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
        Command[] loadedCommands = initializeCommands(commands, this);
        this.commands = new ArrayList<>(loadedCommands.length);
        this.commands.addAll(Arrays.asList(loadedCommands));
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
        if (this.breakpoints != null && this.breakpoints.contains(currentCommand)) {
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
        if (currentCommand >= commands.size()) {
            exit = true;
            forcedExit = true;
            return;
        }
        int[] commandResult;
        synchronized (map) {
            commandResult = commands.get(currentCommand).run(accu);
        }
        if (commands.get(currentCommand).updatesAccu()) {
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

    /**
     * Adds a new breakpoint to this MiMa
     * @param breakpoint The number of the command to stop on
     */
    public synchronized void addBreakpoint(int breakpoint) {
        if (this.breakpoints == null) {
            this.breakpoints = new ArrayList<>();
        }
        if (!this.breakpoints.contains(breakpoint)) {
            this.breakpoints.add(breakpoint);
        }
    }

    /**
     * Adds multiple new breakpoints to this MiMa
     * @param breakpoints The breakpoints to add
     */
    public synchronized void addBreakpoints(Collection<Integer> breakpoints) {
        if (this.breakpoints == null) {
            this.breakpoints = breakpoints;
        } else {
            this.breakpoints.addAll(breakpoints);
        }
    }

    /**
     * Remove a breakpoint from this MiMa
     * @param breakpoint The number of the breakpoint to remove
     */
    public synchronized void removeBreakpoint(int breakpoint) {
        if (this.breakpoints != null) {
            this.breakpoints.remove(breakpoint);
        }
    }

    /**
     * Removes multiple breakpoints from this MiMa
     * @param breakpoints Th breakpoints to remove
     */
    public synchronized void removeBreakpoints(Collection<Integer> breakpoints) {
        this.breakpoints.removeAll(breakpoints);
    }

    /**
     * Tell this MiMa to exit on the next possibility
     */
    public void exit() {
        exit = true;
    }

    /**
     * Whether this MiMa has completely terminated. If the MiMa has only halted because of breakpoints,
     * this returns false
     * @return Whether the MiMa has terminated
     */
    public boolean exited() {
        return this.exit;
    }

    /**
     * Add a new command to this MiMa
     * @param position The position to add this command to
     * @param newCommand The Command to add
     */
    public void addCommand(int position, Command newCommand) {
        this.commands.add(position,newCommand);
    }

    /**
     * Removes a command at a specified position from this MiMa
     * @param position The position of the command to remove
     */
    public void removeCommand(int position) {
        if (position < this.commands.size()) {
            this.commands.remove(position);
        }
    }

    public void addData(String address, int value) {
        if (value > MAX_DATA_VALUE || value < MIN_DATA_VALUE) {
            throw new IllegalArgumentException("This data value doesn't fit into 24 bits");
        }
        map.put(address, value);
        originalMap.put(address, value);
    }

    public void clearData(String address) {
        map.remove(address);
        originalMap.remove(address);
    }

    public Set<Map.Entry<String, Integer>> getMemory() {
        return map.entrySet();
    }

    public Command[] getCommands() {
        return commands.toArray(Command[]::new);
    }

    public Command createCommand(String commandName, String commandValue) {
        Command command;
        switch (commandName) {
            case "ADD" ->command = new ADD(commandValue, this.map);
            case "AND" -> command = new AND(commandValue, this.map);
            case "EQL" -> command = new EQL(commandValue, this.map);
            case "HALT" -> command = new HALT(this);
            case "JMN" -> command = new JMN(commandValue, this.map);
            case "JMP" -> command = new JMP(commandValue, this.map);
            case "LDC" -> command = new LDC(Integer.decode(commandValue));
            case "LDV" -> command = new LDV(commandValue, this.map);
            case "NOT" -> command = new NOT();
            case "OR" -> command = new OR(commandValue, this.map);
            case "RAR" -> command = new RAR();
            case "STV" -> command = new STV(commandValue, this.map);
            case "XOR" -> command = new XOR(commandValue, this.map);
            default -> throw new IllegalArgumentException(commandName + " is not a valid MiMa instruction");
        }
        return command;
    }

    @Override
    public String toString() {
        Set<Map.Entry<String, Integer>> keyPairs;
        synchronized (map) {
            if (map.size() == 0) {
                return forcedExit ? "MiMa WAS FORCEFULLY TERMINATED BECAUSE OF AN ERROR" : "";
            }
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

    public int currentCommand() {
        return currentCommand;
    }

    public int getAccu() {
        return accu;
    }
}