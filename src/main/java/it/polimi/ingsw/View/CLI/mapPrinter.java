package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;

import java.util.Map;

public abstract class mapPrinter {
    protected void printMap(Map<Resource, Integer> map){
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
