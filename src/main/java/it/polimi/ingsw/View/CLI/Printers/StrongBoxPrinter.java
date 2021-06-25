package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.Resource;

import java.util.Map;

/**
 * This class is used to print on the terminal strongbox or support container
 * (in general containers whose content can be represented with a map that has not a maximum limit on the entries equals to one, as with shelves)
 */
public class StrongBoxPrinter {
    /**
     * Prints on terminal the support container containing the resources with the quantities specified in the map passed as a parameter
     * @param map content of the support container (mapping each resource to its quantity in the container)
     */
    public void printSupportContainer(Map<Resource, Integer> map){
        System.out.println("\n" +
                "+---- SUPPORT CONTAINER CONTENT: ----\n" +
                "|");
        print(map);
    }

    /**
     * Prints on terminal the strongbox containing the resources with the quantities specified in the map passed as a parameter
     * @param map content of the strongbox (mapping each resource to its quantity in the container)
     */
    public void printStrongBox(Map<Resource, Integer> map){
        System.out.println("\n" +
                "+-------- STRONGBOX CONTENT: --------\n" +
                "|");
        print(map);
    }

    private void print(Map<Resource, Integer> map){
        if (map.isEmpty()) {
            System.out.println(
                    "|      |¯*¯*¯|\n" +
                    "|      *     *   IS COMPLETELY EMPTY\n" +
                    "|      |_*_*_|\n"+
                    "|");
        } else {
            for(Map.Entry<Resource, Integer> entry : map.entrySet()){
                System.out.println(
                        "|      |¯¯¯¯¯| \n" +
                        "|      | " + Resource.toAliasCompact(entry.getKey()) + " |   x  " + entry.getValue() + " \n" +
                        "|      |_____|\n" +
                        "|");
            }
        }
        System.out.println(
                "+------------------------------------");
    }
}
