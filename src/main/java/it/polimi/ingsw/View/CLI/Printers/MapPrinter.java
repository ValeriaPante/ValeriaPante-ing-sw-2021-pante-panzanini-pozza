package it.polimi.ingsw.View.CLI.Printers;

import it.polimi.ingsw.Enums.Resource;

import java.util.Map;

/**
 * This class is used to print on terminal a map
 * in a compact way (not "interpreted" as for support containers or strongboxes)
 * mostly used when printing cards
 */
public abstract class MapPrinter {
    /**
     * prints the map in a compact way (only in one line)
     * @param map map to be printed
     */
    protected void printMapCompact(Map<Resource, Integer> map){
        boolean commaNeeded = false;
        if (map.isEmpty()){
            System.out.println("nothing");
            return;
        }

        for (Resource r: Resource.values()){
            if (map.containsKey(r)){
                if (commaNeeded)
                    System.out.print(", ");
                else
                    commaNeeded = true;
                System.out.print(map.get(r) + " x " + r.toString());
            }
        }
        System.out.print("\n");
    }
}
