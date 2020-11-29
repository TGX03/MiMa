package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The STV command. Stores the current value in the accu at a specified memory address
 */
public class STV extends Command {

    private final String memoryAdress;

    /**
     * Creates a new STV command with its memory address
     *
     * @param address The address to store the current accu value at
     * @param map     The map representing memory
     */
    public STV(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAdress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        map.put(memoryAdress, currentAccu);
        return new int[]{-1, DONT_JUMP};
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }
}
