package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Deposit.Depot;
import it.polimi.ingsw.Enums.MacroTurnType;

import java.util.*;

/**
 * Controller that manages the request of activation or discarding of a leader card by a player
 */
public class LeaderController extends SelectionController{

    public LeaderController(FaithTrackController ftc) {
        super(ftc);
    }

    /**
     * Makes an action (discard or activate) on a leader card if possibile, otherwise sets an error message
     * @param id chosen leader card id
     * @param discard indicates whether the card is to discard (true) or to activate (false)
     */
    public void actionOnLeaderCard(int id, Boolean discard) {
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();
        if(table.turnOf().getMacroTurnType() == MacroTurnType.NONE || table.turnOf().getMacroTurnType() == MacroTurnType.DONE){
            LeaderCard chosenCard = this.leaderCardFromID(id);

            if(chosenCard != null){
                if (discard){
                    table.turnOf().discardLeaderCard(chosenCard);
                    this.faithTrackController.movePlayerOfTurn(1);
                } else if (!chosenCard.hasBeenPlayed()){
                    if (!checkRequirements(chosenCard))
                        table.turnOf().setErrorMessage("You don't have the requirements needed. ");
                    else chosenCard.play();
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }


    /**
     * Checks leader card requirements if the player wants to activate it
     * @param leaderCardForAction leader card to activate
     * @return true if the player satisfies all requirements, false otherwise
     */
    private boolean checkRequirements(LeaderCard leaderCardForAction){
        return checkResourceReq(leaderCardForAction) && checkDevCardTypeReq(leaderCardForAction);
    }


    /**
     * Checks leader card requirements on the resources owned by the player
     * @param leaderCardForAction leader card to activate
     * @return true if the player satisfies all resource requirements, false otherwise
     */
    private boolean checkResourceReq(LeaderCard leaderCardForAction){
        Depot allResourceOwned = new Depot();

        allResourceOwned.addEnumMap(table.turnOf().getResourcesOwned());

        return allResourceOwned.contains(leaderCardForAction.getResourceReq());
    }


    /**
     * Checks leader card requirements on the type of development card owned by the player
     * @param leaderCardForAction leader card to activate
     * @return true if the player satisfies all development card type requirements, false otherwise
     */
    private boolean checkDevCardTypeReq(LeaderCard leaderCardForAction){
        boolean devCardReq = true;
        if(!leaderCardForAction.getDevCardReq().isEmpty()){
            ArrayList<DevCardType> ownedDevCard = new ArrayList<>();
            for(int i = 0; i < table.turnOf().getDevSlots().length; i++)
                ownedDevCard.addAll(table.turnOf().getDevSlots()[i].getDevCardTypeContained());
            int sum;
            for (Map.Entry<DevCardType, Integer> entry : leaderCardForAction.getDevCardReq().entrySet()) {
                sum = 0;
                for (DevCardType devCardType : ownedDevCard) {
                    if ((entry.getKey().getLevel() == 0 && devCardType.getColor() == entry.getKey().getColor()) || (entry.getKey().getLevel() <= devCardType.getLevel() && devCardType.getColor() == entry.getKey().getColor()))
                        sum++;
                }
                if (sum != entry.getValue()){
                    devCardReq = false;
                    break;
                }
            }
        }
        return devCardReq;
    }
}
