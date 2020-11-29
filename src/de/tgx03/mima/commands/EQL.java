package de.tgx03.mima.commands;

import java.util.Map;

/**
 * A class representing the EQL command. When the current value in the accu
 * equals the one at the set address in memory, it writes -1 to the accu,
 * otherwise 0
 */
public class EQL extends Command {

    private final String memoryAddress;

    /**
     * Creates a new EQL instance
     *
     * @param address The memory address the accu shall be compated to
     * @param map     The map representing memory
     */
    public EQL(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = map.get(memoryAddress);
        return new int[]{currentAccu == memoryValue ? -1 : 0, DONT_JUMP};
    }
}
