package de.tgx03.mima.commands;

import java.util.Map;

public class JMN extends Command {

    private final String memoryAddress;

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
