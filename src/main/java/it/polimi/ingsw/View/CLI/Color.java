package it.polimi.ingsw.View.CLI;

/**
 * This class is used to color strings or integers so they will appear with the specified color once printed on the terminal
 */
public class Color {
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    /**
     * Takes a string and will return the same string with an additional prefix and
     * an additional suffix so the string will be printed with the specified color on terminal
     * @param text the string to be colored
     * @param inputColor a string corresponding to a color, you can pick between "BLACK", "RED", "GREEN", "BLU", "PURPLE", "CYAN", "WHITE", "YELLOW"
     *                   (it is not case sensitive). If this parameter is not corresponding to one of the color listed before, the text will not be colored
     * @return the "text" with the prefix and suffix that will make it colored when printed on terminal
     */
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
            case "YELLOW":
                return YELLOW + text + RESET;
            default:
                return text;
        }
    }

    /**
     * Takes am integer and will return it "stringed" with an additional prefix and
     * an additional suffix so it will be printed with the specified color on terminal
     * @param number the integer to be colored
     * @param color a string corresponding to a color, you can pick between "BLACK", "RED", "GREEN", "BLU", "PURPLE", "CYAN", "WHITE", "YELLOW"
     *                   (it is not case sensitive). If this parameter is not corresponding to one of the color listed before, the integer will not be colored
     * @return the integer "stringed" with the prefix and suffix that will make it colored when printed on terminal
     */
    public static String colourInt (int number, String color){
        return colourText("" + number,color);
    }
}
