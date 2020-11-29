package de.tgx03.mima.commands;

/**
 * Represents the RAR command. It shifts all bits currently in the accu one bit to the right
 */
public class RAR extends Command {

    /**
     * You'll figure out yourself what this does
     */
    public RAR() {
        super(null);
    }

    @Override
    public int[] run(int currentAccu) {
        int shifted = currentAccu >>> 1;
        if (currentAccu % 2 == 1) {
            int firstOne = 0b10000000000000000000000000000000;
            shifted = shifted | firstOne;
        }
        return new int[]{shifted, DONT_JUMP};
    }

}
