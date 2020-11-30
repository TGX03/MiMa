package de.tgx03.mima.commands;

import java.util.Map;

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

    protected final Map<String, Integer> map;

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
}
