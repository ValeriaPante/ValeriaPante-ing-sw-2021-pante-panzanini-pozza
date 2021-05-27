package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public interface Payable {
    boolean contains(EnumMap<Resource, Integer> checkMap);

    void pay();
}
