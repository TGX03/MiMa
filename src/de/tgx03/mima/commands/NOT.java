package de.tgx03.mima.commands;

/**
 * The not command. Inverts the current value in the accu
 */
public class NOT extends Command {

    private static final int OP_CODE = 0b111100010000000000000000;

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

    @Override
    public String toString() {
        return "NOT";
    }

    @Override
    public int hashCode() {
        return OP_CODE;
    }
}
