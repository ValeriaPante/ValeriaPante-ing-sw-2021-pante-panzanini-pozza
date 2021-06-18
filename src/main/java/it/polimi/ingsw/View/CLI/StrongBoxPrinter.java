package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;

import java.util.Map;

public class StrongBoxPrinter {
    public void printSupportContainer(Map<Resource, Integer> map){
        System.out.println("\n" +
                "+---- SUPPORT CONTAINER CONTENT: ----\n" +
                "|");
        print(map);
    }

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
