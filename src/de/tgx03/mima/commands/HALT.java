package de.tgx03.mima.commands;

import de.tgx03.mima.MiMa;

public class HALT extends Command {

    private final MiMa parentMiMa;

    public HALT(MiMa parent) {
        super(null);
        this.parentMiMa = parent;
    }

    @Override
    public int[] run(int currentAccu) {
        parentMiMa.exit();
        return new int[]{-1, DONT_JUMP};
    }

    @Override
    public boolean updatesAccu() {
        return false;
    }
}
