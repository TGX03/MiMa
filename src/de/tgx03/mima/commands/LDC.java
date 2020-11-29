package de.tgx03.mima.commands;

/**
 * The LDC command. Loads a constant value into the accu
 */
public class LDC extends Command {

    private static final int DATA_MAX = 524287;
    private static final int DATA_MIN = -524288;

    private final int value;

    /**
     * Creates a new LDC command
     *
     * @param constant The constant to load into the accu
     */
    public LDC(int constant) {
        super(null);
        if (constant > DATA_MAX || constant < DATA_MIN) {
            throw new IllegalArgumentException("Can't store this value for a command");
        }
        this.value = constant;
    }

    @Override
    public int[] run(int currentAccu) {
        return new int[]{this.value, DONT_JUMP};
    }
}
