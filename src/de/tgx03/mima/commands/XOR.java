package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The XOR command. It XORs the current value in the accu with a value at a specified memory location
 */
public class XOR extends Command {

    private static final int OP_CODE = 0b011000000000000000000000;

    private final String memoryAddress;

    /**
     * Creates a new XOR instance with its target address
     *
     * @param address The address the second value is stored at
     * @param map     The map representing memory
     */
    public XOR(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = read(memoryAddress);
        return new int[]{memoryValue ^ currentAccu, DONT_JUMP};
    }

    public String toString() {
        return "XOR : " + memoryAddress;
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
