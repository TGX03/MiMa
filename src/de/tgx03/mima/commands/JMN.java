package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The JMN command. When a negative value is stored in the accu, it jumps to a specified memory address,
 * otherwise it just goes to the next command
 */
public class JMN extends Command {

    private static final int OP_CODE = 0b100100000000000000000000;

    private final String memoryAddress;

    /**
     * Creates a new JMN instance
     *
     * @param address The address in memory where the address to jump to in case of a negative accu is stored
     * @param map     The map representing memory
     */
    public JMN(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        if (currentAccu < 0) {
            int newAddress = read(memoryAddress);
            return new int[]{-1, newAddress};
        } else {
            return new int[]{-1, DONT_JUMP};
        }
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }

    @Override
    public String toString() {
        return "JMN : " + memoryAddress;
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
