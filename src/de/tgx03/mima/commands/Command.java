package de.tgx03.mima.commands;

import java.util.HashMap;

public abstract class Command {

    public static final int DONT_JUMP = -1;

    protected final HashMap<String, Integer> map;

    protected Command(HashMap<String, Integer> map) {
        this.map = map;
    }

    /**
     * Runs this command and returns the new value for the accu and the address to jump to
     * @param currentAccu The current value stored in the accu
     * @return Where to jump in the instruction memory. If -1 jump to the next, otherwise jump to specified memory address
     */
    public abstract int[] run(int currentAccu);

    public boolean updatesAccu() {
        return true;
    }
}
