package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Abilities.Ability;

import java.util.*;

public class LeaderCard extends CardVP{
    private final EnumMap<Resource, Integer> resourceReq;
    private final Map<DevCardType, Integer> devCardReq;
    private final LeaderCardType type;
    private final EnumMap<Resource, Integer> input;
    private Ability ability;
    private boolean played;

    public LeaderCard(int vp, EnumMap<Resource, Integer> resourceReq, Map<DevCardType, Integer> devCardReq, LeaderCardType type, EnumMap<Resource, Integer> input) throws IllegalArgumentException{
        super(vp);

        if(resourceReq == null || devCardReq == null || type == null || input == null){
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.resourceReq = resourceReq.clone();
        Map<DevCardType, Integer> temp = new HashMap<>();
        for (Map.Entry<DevCardType, Integer> entry : devCardReq.entrySet()) {
            temp.put(new DevCardType(entry.getKey().getLevel(), entry.getKey().getColor()), entry.getValue());
        }
        this.devCardReq = temp;
        this.input = input.clone();
        this.ability = null;
        played = false;
    }

    public EnumMap<Resource, Integer> getResourceReq(){
        return resourceReq.clone();
    }

    public Map<DevCardType, Integer> getDevCardReq(){
        Map<DevCardType, Integer> temp = new HashMap<>();

        for (Map.Entry<DevCardType, Integer> entry : devCardReq.entrySet()) {
            temp.put(new DevCardType(entry.getKey().getLevel(), entry.getKey().getColor()), entry.getValue());
        }

        return temp;
    }

    public LeaderCardType getType(){
        return type;
    }

    public Ability getAbility(){
        return ability;
    }

    public boolean hasBeenPlayed(){
        return played;
    }

    public void play(){
        this.played = true;
        this.ability = new Ability(this.input, this.type);

    }
}
