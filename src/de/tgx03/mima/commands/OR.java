package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The OR command. ORs the current value in the accu with a value in memory
 */
public class OR extends Command {

    private static final int OP_CODE = 0b010100000000000000000000;

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
        int memoryValue = read(memoryAddress);
        return new int[]{memoryValue | currentAccu, DONT_JUMP};
    }

    @Override
    public String toString() {
        return "OR : " + memoryAddress;
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
