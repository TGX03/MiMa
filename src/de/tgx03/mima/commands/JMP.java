package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The JMP command. Makes the MiMa jump to a specific location in memory
 */
public class JMP extends Command {

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
}
