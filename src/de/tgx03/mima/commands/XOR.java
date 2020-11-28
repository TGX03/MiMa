package de.tgx03.mima.commands;

import java.util.HashMap;

public class XOR extends Command {

    private final String memoryAddress;

    public XOR(String address, HashMap<String, Integer> map) {
        super(map);
        this.memoryAddress = address;
    }

    @Override
    public int[] run(int currentAccu) {
        int memoryValue = map.get(memoryAddress);
        return new int[]{memoryValue ^ currentAccu, DONT_JUMP};
    }
}
