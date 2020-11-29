package de.tgx03.mima.commands;

import java.util.Map;

/**
 * A class representing the AND command. It ANDs the current value in the ACCU with a value in memory
 */
public class AND extends Command {

    private final String memoryAddress;

    /**
     * Create a new AND instance with access to memory and a memory address to operate on
     *
     * @param address The memory address to operate on
     * @param map     The map representing memory
     */
    public AND(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = map.get(memoryAddress);
        return new int[]{memoryValue & currentAccu, DONT_JUMP};
    }
}
