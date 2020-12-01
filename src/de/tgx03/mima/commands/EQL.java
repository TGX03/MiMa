package de.tgx03.mima.commands;

import java.util.Map;

/**
 * A class representing the EQL command. When the current value in the accu
 * equals the one at the set address in memory, it writes -1 to the accu,
 * otherwise 0
 */
public class EQL extends Command {

    private static final int OP_CODE = 0b011100000000000000000000;

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
        int memoryValue = read(memoryAddress);
        return new int[]{currentAccu == memoryValue ? -1 : 0, DONT_JUMP};
    }

    @Override
    public String toString() {
        return "EQL : " + memoryAddress;
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
