package de.tgx03.mima.commands;

public class NOT extends Command {

    public NOT() {
        super(null);
    }

    @Override
    public int[] run(int currentAccu) {
        return new int[]{~currentAccu, DONT_JUMP};
    }
}
