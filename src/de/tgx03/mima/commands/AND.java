package de.tgx03.mima.commands;

import java.util.Map;

/**
 * A class representing the AND command. It ANDs the current value in the ACCU with a value in memory
 */
public class AND extends Command {

    private static final int OP_CODE = 0b010000000000000000000000;

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

    @Override
    public String toString() {
        return "AND : " + memoryAddress;
    }

    @Override
    public int hashCode() {
        int valueHash;
        try {
            valueHash = Integer.decode(memoryAddress);
        } catch (NumberFormatException e) {
            valueHash = memoryAddress.hashCode();
        }
        while (valueHash > DATA_MAX) {
            valueHash = valueHash - DATA_MAX;
        }
        while (valueHash < 0) {
            valueHash = valueHash + DATA_MAX;
        }
        return OP_CODE + valueHash;
    }
}
