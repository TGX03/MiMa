package de.tgx03.mima.commands;

import java.util.HashMap;

public class STV extends Command {

    private final String memoryAdress;

    public STV(String address, HashMap<String, Integer> map) {
        super(map);
        this.memoryAdress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        map.put(memoryAdress, currentAccu);
        return new int[]{-1, DONT_JUMP};
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }
}
