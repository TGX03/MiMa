package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The LDV command. Loads a value from a specific address in memory
 */
public class LDV extends Command {

    private static final int OP_CODE = 0b000100000000000000000000;

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
        return new int[]{read(memoryAddress), DONT_JUMP};
    }

    @Override
    public String toString() {
        return "LDV : " + memoryAddress;
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
