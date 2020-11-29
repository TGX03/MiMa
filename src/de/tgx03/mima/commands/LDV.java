package de.tgx03.mima.commands;

import java.util.Map;

public class LDV extends Command {

    private final String memoryAddress;

    public LDV(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        return new int[]{map.get(memoryAddress), DONT_JUMP};
    }
}
