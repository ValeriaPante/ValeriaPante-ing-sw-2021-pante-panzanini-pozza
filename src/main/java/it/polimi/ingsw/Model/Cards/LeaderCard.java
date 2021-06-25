package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Model.Abilities.Ability;

import java.util.*;

/**
 * Representation of Leader Card
 */
public class LeaderCard extends CardVP{

    private final EnumMap<Resource, Integer> resourceReq;
    private final Map<DevCardType, Integer> devCardReq;
    private final LeaderCardType type;
    private final EnumMap<Resource, Integer> input;
    private final EnumMap<Resource, Integer> output;
    private Ability ability;
    private boolean played;
    private final int id;
    private boolean selected;


    /**
     * Leader card constructor
     * @param vp victory points
     * @param resourceReq requirements on resources owned by the player
     * @param devCardReq requirements on development card owned by the player
     * @param type type of leader card (transmutation, storage, discount, production)
     * @param input input needed for the ability of the card
     * @param output output given by the ability of the card
     * @param id id of the card
     * @throws IllegalArgumentException if one of the parameters are null
     */
    public LeaderCard(int vp, EnumMap<Resource, Integer> resourceReq, Map<DevCardType, Integer> devCardReq, LeaderCardType type, EnumMap<Resource, Integer> input, EnumMap<Resource, Integer> output, int id) throws IllegalArgumentException{
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
        this.output = output.clone();
        this.ability = null;
        played = false;
        selected = false;
        this.id = id;
    }

    /**
     * Resource requirements getter
     * @return all resource requirements of the card
     */
    public EnumMap<Resource, Integer> getResourceReq(){
        return resourceReq.clone();
    }

    /**
     * Development card requirements getter
     * @return all development card requirements of the card
     */
    public Map<DevCardType, Integer> getDevCardReq(){
        Map<DevCardType, Integer> temp = new HashMap<>();

        for (Map.Entry<DevCardType, Integer> entry : devCardReq.entrySet()) {
            temp.put(new DevCardType(entry.getKey().getLevel(), entry.getKey().getColor()), entry.getValue());
        }

        return temp;
    }

    /**
     * Leader card type getter
     * @return type of the card
     */
    public LeaderCardType getType(){
        return type;
    }

    /**
     * Leader card ability getter
     * @return ability of the card
     */
    public Ability getAbility(){
        return ability;
    }

    /**
     * Checks if the card has already been played
     * @return true if the card has been played, false otherwise
     */
    public boolean hasBeenPlayed(){
        return played;
    }

    /**
     * Plays the card
     */
    public void play(){
        this.played = true;
        this.ability = new Ability(this.input, this.output, this.type);

    }

    /**
     * Checks if the card has been selected
     * @return true if the card is selected, false otherwise
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * Selects the card
     */
    public void select(){
        this.selected = !this.selected;
    }

    /**
     * Leader card id getter
     * @return id of the card
     */
    public int getId(){
        return this.id;
    }
}
