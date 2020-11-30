package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The STV command. Stores the current value in the accu at a specified memory address
 */
public class STV extends Command {

    private static final int OP_CODE = 0b001000000000000000000000;

    private final String memoryAddress;

    /**
     * Creates a new STV command with its memory address
     *
     * @param address The address to store the current accu value at
     * @param map     The map representing memory
     */
    public STV(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        map.put(memoryAddress, currentAccu);
        return new int[]{-1, DONT_JUMP};
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }

    @Override
    public String toString() {
        return "STV : " + memoryAddress;
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
