package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The JMN command. When a negative value is stored in the accu, it jumps to a specified memory address,
 * otherwise it just goes to the next command
 */
public class JMN extends Command {

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
            int newAddress = map.get(memoryAddress);
            return new int[]{-1, newAddress};
        } else {
            return new int[]{-1, DONT_JUMP};
        }
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }
}
