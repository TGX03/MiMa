package de.tgx03.mima.commands;

import de.tgx03.mima.MiMa;

/**
 * Represents the HALT command, terminating the MiMa it belongs to
 */
public class HALT extends Command {

    private final MiMa parentMiMa;

    /**
     * Creates a new HALT instance
     *
     * @param parent The MiMa instance this Halt shall terminate
     */
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
