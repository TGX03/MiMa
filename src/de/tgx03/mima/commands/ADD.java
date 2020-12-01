package de.tgx03.mima.commands;

import java.util.Map;

/**
 * A commands representing the ADD command
 * It adds the current value in the accu and a value at a specific location in memory
 */
public class ADD extends Command {

    private static final int OP_CODE = 0b001100000000000000000000;

    private final String memoryAddress;

    /**
     * Creates a new add command with access to memory and a memory address to operate on
     *
     * @param address The address of the second value in memory
     * @param map     The map representing the memory
     */
    public ADD(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = read(memoryAddress);
        return new int[]{currentAccu + memoryValue, DONT_JUMP};
    }

    @Override
    public String toString() {
        return "ADD : " + memoryAddress;
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
