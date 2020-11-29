package de.tgx03.mima.commands;

/**
 * The not command. Inverts the current value in the accu
 */
public class NOT extends Command {

    /**
     * Well I guess...
     * Luckily Checkstyle isn't required for this
     */
    public NOT() {
        super(null);
    }

    @Override
    public int[] run(int currentAccu) {
        return new int[]{~currentAccu, DONT_JUMP};
    }
}
