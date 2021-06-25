package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.Resource;

import java.util.Map;

/**
 * This class is used to print on terminal the shelves with the content specified
 * as parameter in the public method "printShelves(...)"
 */
public class ShelvesPrinter {
    /**
     * Prints on terminal the shelves containing the resources with the quantities specified in the maps passed as parameters.
     * Each map should contain at most one resource mapped to the quantity in that precise shelf
     * @param shelf1 content of shelf with capacity one
     * @param shelf2 content of shelf with capacity two
     * @param shelf3 content of shelf with capacity three
     */
    public void printShelves(Map<Resource, Integer> shelf1, Map<Resource, Integer> shelf2, Map<Resource, Integer> shelf3){
        System.out.println("\n" +
                "+---------- SHELVES --------\n" +
                "|\n" +
                "|           |¯¯¯¯¯|\n" +
                "|           "+ getContent(1, shelf1) +"\n" +
                "|           |_____|\n" +
                "|                \n" +
                "|       |¯¯¯¯¯| |¯¯¯¯¯|\n" +
                "|       "+ getContent(2, shelf2) +"\n" +
                "|       |_____| |_____|\n" +
                "|        \n" +
                "|   |¯¯¯¯¯| |¯¯¯¯¯| |¯¯¯¯¯|\n" +
                "|   "+ getContent(3, shelf3) +"\n" +
                "|   |_____| |_____| |_____|\n" +
                "|\n" +
                "+---------------------------");
    }

    private String getContent(int capacity, Map<Resource, Integer> shelf){
        switch (capacity){
            case 1:
                return shelf == null || shelf.isEmpty() ?
                        "|     |" :
                        "| "+ Resource.toAliasCompact(getTheOnlyEntry(shelf)) +" |";
            case 2:
                return shelf == null || shelf.isEmpty() ?
                        "|     | |     |" :
                        "| "+ Resource.toAliasCompact(getTheOnlyEntry(shelf)) +" | "
                                + "| "+ (shelf.get(getTheOnlyEntry(shelf)) > 1 ? Resource.toAliasCompact(getTheOnlyEntry(shelf)) : "   ") +" | ";
            default:
                return shelf == null || shelf.isEmpty() ?
                        "|     | |     | |     |" :
                        "| "+ Resource.toAliasCompact(getTheOnlyEntry(shelf)) +" | "
                                + "| "+ (shelf.get(getTheOnlyEntry(shelf)) > 1 ? Resource.toAliasCompact(getTheOnlyEntry(shelf)) : "   ") +" | "
                                + "| "+ (shelf.get(getTheOnlyEntry(shelf)) > 2 ? Resource.toAliasCompact(getTheOnlyEntry(shelf)) : "   ") +" | ";
        }
    }

    private Resource getTheOnlyEntry(Map<Resource, Integer> shelf){
        for (Map.Entry<Resource, Integer> entry : shelf.entrySet())
            return entry.getKey();

        //never used
        return null;
    }
}
