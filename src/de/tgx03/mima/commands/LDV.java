package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The LDV command. Loads a value from a specific address in memory
 */
public class LDV extends Command {

    private final String memoryAddress;

    /**
     * @param address The address to load the value from
     * @param map     The map representing memory
     */
    public LDV(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        return new int[]{map.get(memoryAddress), DONT_JUMP};
    }
}
