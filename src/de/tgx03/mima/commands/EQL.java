package de.tgx03.mima.commands;

import java.util.Map;

public class EQL extends Command {

    private final String memoryAddress;

    public EQL(String address, Map<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = map.get(memoryAddress);
        return new int[]{currentAccu == memoryValue ? -1 : 0, DONT_JUMP};
    }
}
