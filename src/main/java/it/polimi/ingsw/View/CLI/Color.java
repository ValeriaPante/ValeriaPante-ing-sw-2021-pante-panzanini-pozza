package it.polimi.ingsw.View.CLI;

public class Color {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static String colourText(String text, String inputColor){
        switch (inputColor.toUpperCase()){
            case "BLACK":
                return BLACK + text + RESET;
            case "RED":
                return RED + text + RESET;
            case "GREEN":
                return GREEN + text + RESET;
            case "BLUE":
                return BLUE + text + RESET;
            case "PURPLE":
                return PURPLE + text + RESET;
            case "CYAN":
                return CYAN + text + RESET;
            case "WHITE":
                return WHITE + text + RESET;
            default:
                return YELLOW + text + RESET;
        }
    }

    public static String colourInt (int number, String color){
        String toString = "";
        toString += number;
        return colourText(toString,color);
    }
}
