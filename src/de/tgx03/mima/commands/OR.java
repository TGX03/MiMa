package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The OR command. ORs the current value in the accu with a value in memory
 */
public class OR extends Command {

    private final String memoryAddress;

    /**
     * Creates a new OR object
     *
     * @param address The address where the second value is stored in memory
     * @param map     The map representing memory
     */
    public OR(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = map.get(memoryAddress);
        return new int[]{memoryValue | currentAccu, DONT_JUMP};
    }
}
