package de.tgx03.mima;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A simple frontend for the MiMa
 */
public class Launcher {

    private static final String lineSeperator = System.getProperty("line.separator");
    private static MiMa mima;

    /**
     * Creates a MiMa instance from a file containing the commands (and another one containing the inital memory),
     * runs it and prints memory after the MiMa terminated
     * Command file must follow "CMD Value"
     * Data File must follow "Address Value"
     * When the Data File can't be read for whatever reason, it gets treated as non-existent and only the commands get loaded
     *
     * @param args First argument is the location of the command file, second argument either doesn't exist
     *             or is the file holding the initial memory
     * @throws IOException In case the command file couldn't be read
     */
    public static void main(String[] args) throws IOException {
        switch (args.length) {
            case 1 -> initializeNoData(args[0]);
            case 2 -> initializeWithData(args[0], args[1]);
            default -> throw new IllegalArgumentException("Invalid arguments provided");
        }
        mima.run();
        System.out.println(mima.toString());
    }

    /**
     * Initializes a MiMa with both commands and initial memory.
     * When the memory file cannot be read, the MiMa gets initialized with empty memory
     *
     * @param commandFile The location of the file containing the commands
     * @param dataFile    The location of the file containing the initial memory state
     * @throws IOException Gets thrown when the command file couldn't be read
     */
    private static void initializeWithData(String commandFile, String dataFile) throws IOException {
        String[] commands = Files.readString(Paths.get(commandFile)).split(lineSeperator);
        try {
            String[] data = Files.readString(Paths.get(dataFile)).split(lineSeperator);
            mima = new MiMa(commands, data);
        } catch (IOException e) {
            initializeNoData(commandFile);
        }
    }

    /**
     * Initializes a MiMa with commands
     *
     * @param commandFile The location of the file containing the commands
     * @throws IOException Gets thrown when the command file couldn't be read
     */
    private static void initializeNoData(String commandFile) throws IOException {
        String[] commands = Files.readString(Paths.get(commandFile)).split(lineSeperator);
        mima = new MiMa(commands);
    }
}
