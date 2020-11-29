package de.tgx03.mima.commands;

import java.util.Map;

/**
 * The XOR command. It XORs the current value in the accu with a value at a specified memory location
 */
public class XOR extends Command {

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
        int memoryValue = map.get(memoryAddress);
        return new int[]{memoryValue ^ currentAccu, DONT_JUMP};
    }
}
