package de.tgx03.mima.commands;

import java.util.HashMap;

public class JMP extends Command {

    private final String memoryAddress;

    public JMP(String address, HashMap<String, Integer> map) {
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
