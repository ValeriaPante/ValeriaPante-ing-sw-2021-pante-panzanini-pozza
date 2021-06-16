package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.CreationLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.DisconnectMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.MoveToLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.StartGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.View.ClientModel.Game;

public class InputManager{
    private final Game model;

    public InputManager(Game model){
        this.model = model;
    }

    private String preprocess(String string){
        return string.trim().toUpperCase();
    }

    private boolean possesLeaderCard (int id){
        return model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards().contains(id);
    }

    private boolean possesDevCard (int id){
        int[][] devSlots = model.getPlayerFromId(model.getLocalPlayerId()).getDevSlots();
        for (int[] devSlot : devSlots) {
            for (int i : devSlot)
                if (i == id)
                    return true;
        }
        return false;
    }

    private InGameMessage moveResources(String input) throws IllegalArgumentException{
        if (input.startsWith("SC"))
            return new MoveToSupportContainerMessage();

        if (input.startsWith("SH"))
            return new MoveToShelfMessage(Integer.parseInt(""+input.charAt(2)));

        int leaderCardID = Integer.parseInt(""+input.charAt(2)+input.charAt(3));
        if (!possesLeaderCard(leaderCardID))
            throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
        return new MoveToLeaderStorageMessage(leaderCardID);
    }

    private InGameMessage selectDeselectResources(String input, boolean selection) throws IllegalArgumentException{
        String[] inputParts = input.split(",");
        Resource resource = Resource.fromAlias(inputParts[1]);

        if (input.startsWith("SH")){
            int capacity = Integer.parseInt(""+input.charAt(2));
            return selection? new ShelfSelectionMessage(capacity, resource) : new ShelfDeselectionMessage(capacity, resource);
        }

        int positionOrQuantity = Integer.parseInt(inputParts[2]);
        if(input.startsWith("LC")){
            int leaderCardID = Integer.parseInt(""+input.charAt(2)+input.charAt(3));
            if (!possesLeaderCard(leaderCardID))
                throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
            return new LeaderStorageSelectionMessage(leaderCardID, positionOrQuantity, resource);
        }
        if(input.startsWith("SB"))
            return selection? new StrongBoxSelectionMessage(positionOrQuantity, resource) : new StrongBoxDeselectionMessage(positionOrQuantity, resource);
        return selection? new SupportContainerSelectionMessage(positionOrQuantity, resource) : new SupportContainerDeselectionMessage(positionOrQuantity, resource);
    }

    private InGameMessage selectInMarket(String input) throws IllegalArgumentException{
        String[] inputContent = input.split(",");
        int number = Integer.parseInt(inputContent[1]);

        if(inputContent[0].equals("ROW"))
            return new MarketSelectionMessage(number, true);

        return new MarketSelectionMessage(number, false);
    }

    public PreGameMessage preGameInput(String input) throws IllegalArgumentException{
        String toBeChecked = preprocess(input);

        if (toBeChecked.startsWith("MOVE TO LOBBY ")){
            toBeChecked = toBeChecked.replace("MOVE TO LOBBY ", "");
            try {
                return new MoveToLobbyMessage(Integer.parseInt(toBeChecked));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("You entered a wrong integer! Please, retry...");
            }
        }

        switch(toBeChecked){
            case "CREATE LOBBY":
                return new CreationLobbyMessage();
            case "DISCONNECT":
                return new DisconnectMessage();
            case "START GAME":
                return new StartGameMessage();
            default:
                throw new IllegalArgumentException("Your input was not correct! Please, retry...");
        }
    }

    public InGameMessage discardLeaderCardInput(String input) throws IllegalArgumentException{
        String toBeChecked = preprocess(input);
        try {
            int id = Integer.parseInt(toBeChecked);
            if (!possesLeaderCard(id))
                throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
            else
                return new LeaderDiscardMessage(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("You entered a wrong integer! Please, retry...");
        }
    }

    public InGameMessage selectInitialResource(String input) throws IllegalArgumentException{
        String toBeChecked = preprocess(input);
        if(!toBeChecked.matches("(SH[1-3]:(COIN|STONE|SERVANT|SHIELD))"))
            throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");

        int capacity = Integer.parseInt(""+toBeChecked.charAt(2));
        String alias = toBeChecked.replace("SH"+capacity,"");
        Resource resource = Resource.fromAlias(alias);
        return new SelectResourceMessage(capacity, resource);
    }

    public InGameMessage beforeChoseTurnInput(String input) throws IllegalArgumentException{
        String toBeChecked = preprocess(input);
        if(toBeChecked.matches("(SELECT FROM MARKET:(ROW,[0-2]|COLUMN,[0-3]))"))
            return selectInMarket(toBeChecked.replace("SELECT FROM MARKET:", ""));

        if (toBeChecked.matches("(SELECT:(((LC\\d\\d|SB),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONES|SERVANT|SHIELD))))"))
            return selectDeselectResources(toBeChecked.replace("SELECT:", ""), true);

        if(toBeChecked.matches("(DESELECT:(((LC\\d\\d|SB),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONES|SERVANT|SHIELD))))"))
            return selectDeselectResources(toBeChecked.replace("DESELECT:", ""), false);

        throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");
    }

    public InGameMessage marketTurnInput(String input) throws IllegalArgumentException{
        String toBeChecked = preprocess(input);
        if (toBeChecked.equals("EXCHANGE"))
            return new ExchangeMessage();

        if (toBeChecked.equals("QUIT"))
            return new QuitFromMarketMessage();

        if(toBeChecked.matches("(SELECT FROM MARKET:(ROW,[0-2]|COLUMN,[0-3]))"))
            return selectInMarket(toBeChecked.replace("SELECT FROM MARKET:", ""));

        if (toBeChecked.matches("(MOVE TO:(SC|(LC\\d\\d)|(SH[1-3])))"))
            return moveResources(toBeChecked.replace("MOVE TO:", ""));

        if (toBeChecked.matches("(SELECT:(((LC\\d\\d|SC),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONES|SERVANT|SHIELD))))"))
            return selectDeselectResources(toBeChecked.replace("SELECT:", ""), true);

        if(toBeChecked.matches("(DESELECT:(((LC\\d\\d|SC),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONES|SERVANT|SHIELD))))"))
            return selectDeselectResources(toBeChecked.replace("DESELECT:", ""), false);

        throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");
    }
}
