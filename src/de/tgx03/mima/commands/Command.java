package de.tgx03.mima.commands;

import java.util.Map;
import java.util.Objects;

/**
 * A class representing the basic structure of a MiMa command
 */
public abstract class Command {

    /**
     * This value gets returned as the second return value of the run command
     * when the command doesn't change which command gets executed next
     * and the MiMa shall simply go to the next command
     */
    public static final int DONT_JUMP = -1;
    protected static final int DATA_MAX = 1048575;
    protected static final int DATA_MIN = 0;
    private final Map<String, Integer> map;

    /**
     * @param map The map representing memory
     */
    protected Command(Map<String, Integer> map) {
        this.map = map;
    }

    /**
     * Runs this command and returns the new value for the accu and the address to jump to
     *
     * @param currentAccu The current value stored in the accu
     * @return Where to jump in the instruction memory. If -1 jump to the next, otherwise jump to specified memory address
     */
    public abstract int[] run(int currentAccu);

    /**
     * Reports whether the current command changes the accu.
     * Depending on this the MiMa should update the accu or not since a valid value
     * has to always be returned
     *
     * @return Whether this command changes the accu
     */
    public boolean updatesAccu() {
        return true;
    }

    /**
     * Writes a specified value to the specified address in memory
     *
     * @param memoryAddress The address to write to
     * @param value         The value to store
     */
    protected void write(String memoryAddress, int value) {
        map.remove(memoryAddress);
        map.put(memoryAddress, value);
    }

    /**
     * Reads from a specified memory address
     * When reading from an unknown memory address, it returns 0, since an unset address holds 0 as value
     *
     * @param memoryAddress The memory address to read from
     * @return The stored value
     */
    protected int read(String memoryAddress) {
        Integer result = map.get(memoryAddress);
        return Objects.requireNonNullElse(result, 0);
    }
}
