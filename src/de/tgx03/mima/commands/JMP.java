package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The JMP command. Makes the MiMa jump to a specific location in memory
 */
public class JMP extends Command {

    private static final int OP_CODE = 0b100000000000000000000000;

    private final String memoryAddress;

    /**
     * @param address Where the address to jump to is stored in memory
     * @param map     The map representing memory
     */
    public JMP(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int newAddress = map.get(memoryAddress);
        return new int[]{-1, newAddress};
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }

    @Override
    public String toString() {
        return "JMP : " + memoryAddress;
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
