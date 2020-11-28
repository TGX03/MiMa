package de.tgx03.mima;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Launcher {

    private static final String lineSeperator = System.getProperty("line.separator");
    private static MiMa mima;

    public static void main(String[] args) throws IOException {
        switch (args.length) {
            case 1 -> initializeNoData(args[0]);
            case 2 -> initializeWithData(args[0], args[1]);
            default -> throw new IllegalArgumentException("Invalid arguments provided");
        }
        mima.run();
        System.out.println(mima.toString());
    }

    private static void initializeWithData(String commandFile, String dataFile) throws IOException {
        String[] commands = Files.readString(Paths.get(commandFile)).split(lineSeperator);
        try {
            String[] data = Files.readString(Paths.get(dataFile)).split(lineSeperator);
            mima = new MiMa(commands, data);
        } catch (IOException e) {
            initializeNoData(commandFile);
        }
    }

    private static void initializeNoData(String commandFile) throws IOException {
        String[] commands = Files.readString(Paths.get(commandFile)).split(lineSeperator);
        mima = new MiMa(commands);
    }
}
