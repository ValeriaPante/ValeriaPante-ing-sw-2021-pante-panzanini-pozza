package it.polimi.ingsw.View.CLI;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.PrintWithoutMessageCreationException;
import it.polimi.ingsw.Exceptions.SuppressNotificationsException;
import it.polimi.ingsw.Messages.InGameMessages.ConcreteMessages.*;
import it.polimi.ingsw.Messages.InGameMessages.InGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.CreationLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.DisconnectMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.MoveToLobbyMessage;
import it.polimi.ingsw.Messages.PreGameMessages.ConcreteMessages.StartGameMessage;
import it.polimi.ingsw.Messages.PreGameMessages.PreGameMessage;
import it.polimi.ingsw.View.ClientModel.Game;

/**
 * This class is used to interpret the input from console and convert it into actions:
 * creating the message specified, print what request oer even show or hide notifications from the server.
 * All the exceptions are really important and each of them contains, as message, a custom string.
 * To know exactly yhe use of each exception check the javadoc of each public method
 */
public class InputManager{
    private final Game model;

    /**
     * @param model is the model the input manager will refer its decision about the input
     */
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

    private InGameMessage resourceSelectionDeselection(String input, boolean selection) throws IllegalArgumentException{
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

    private InGameMessage objectSelection(String input) throws IllegalArgumentException{
        if (input.startsWith("BASIC"))
            return new CardProductionSelectionMessage(0);

        if (input.startsWith("ALL"))
            return new AllProductionPowerSelectionMessage();

        if (input.startsWith("LC")){
            int leaderCardID = Integer.parseInt(input.replace("LC", ""));
            if (!possesLeaderCard(leaderCardID))
                throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
            return new CardProductionSelectionMessage(leaderCardID);
        }

        if (input.startsWith("DC")){
            int devCardID = Integer.parseInt(input.replace("DC", ""));
            if (!possesDevCard(devCardID))
                throw new IllegalArgumentException("You cannot select the development card with that id! Please, retry...");
            return new CardProductionSelectionMessage(devCardID);
        }

        if (input.startsWith("DS"))
            return new ChooseDevSlotMessage(Integer.parseInt("" + input.charAt(2)));

        String[] inputContent = input.split(",");
        int devDecPosition;
        switch(inputContent[2].replace("COLOR:", "")){
            case "GREEN":
                devDecPosition = 0;
                break;
            case "YELLOW":
                devDecPosition = 1;
                break;
            case "BLUE":
                devDecPosition = 2;
                break;
            default:
                devDecPosition = 3;
                break;
        }
        switch(Integer.parseInt(inputContent[1].replace("LEVEL",""))){
            case 2 :
                devDecPosition +=4;
                break;
            case 3 :
                devDecPosition +=8;
                break;
            default:
                break;
        }
        return new ChooseDevCardMessage(devDecPosition+1);
    }

    private InGameMessage selectInMarket(String input) throws IllegalArgumentException{
        String[] inputContent = input.split(",");
        int number = Integer.parseInt(inputContent[1]);

        if(inputContent[0].equals("ROW"))
            return new MarketSelectionMessage(number, true);

        return new MarketSelectionMessage(number, false);
    }

    private InGameMessage actionOnLC (String input){
        String[] inputParts = input.split(":");
        int leaderCardID = Integer.parseInt(inputParts[1].replace(" LC", ""));
        if (!possesLeaderCard(leaderCardID))
            throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
        return new LeaderCardActionMessage(leaderCardID, !input.startsWith("PLAY"));
    }

    private InGameMessage leaderCardAbility(String input) throws IllegalArgumentException{
        if (input.startsWith("TRANSMUTE: ")){
            String[] inputParts = input.replace("TRANSMUTE: ","").split(",");
            String[] internalParts = inputParts[0].split("X");
            int leaderCardID1 = Integer.parseInt(internalParts[0].replace("LC",""));
            int quantity1 = Integer.parseInt(internalParts[1]);
            internalParts = inputParts[1].split("X");
            int leaderCardID2 = Integer.parseInt(internalParts[0].replace("LC",""));
            int quantity2 = Integer.parseInt(internalParts[1]);

            if (!possesLeaderCard(leaderCardID1))
                throw new IllegalArgumentException("You do not own a leader card with the first id you wrote! Please, retry...");
            if (!possesLeaderCard(leaderCardID2))
                throw new IllegalArgumentException("You do not own a leader card with the second id you wrote! Please, retry...");

            return new TransmutationMessage(leaderCardID1, leaderCardID2, quantity1, quantity2);
        }

        int leaderCardID = Integer.parseInt(input.replace("DISCOUNT: LC",""));
        if (!possesLeaderCard(leaderCardID))
            throw new IllegalArgumentException("You do not own that leader card! Please, retry...");
        return new DiscountAbilityMessage(leaderCardID);
    }

    /**
     * Process the input in the pre game phase (when the player is surfing in the lobbies).
     * All the exceptions contain a String with the message to be displayed or to be analysed
     * @param input the input from the player
     * @return a message that has to be send to the server
     * @throws IllegalArgumentException the input is not correct
     * @throws PrintWithoutMessageCreationException the input is a request to print
     * @throws SuppressNotificationsException the player want to show or hide notifications
    */
    public PreGameMessage preGameInput(String input) throws IllegalArgumentException, PrintWithoutMessageCreationException, SuppressNotificationsException{
        String toBeChecked = preprocess(input);
        if (toBeChecked.equals("NOTIFICATIONS"))
            throw new SuppressNotificationsException();

        if (toBeChecked.matches("(PRINT: (MY LOBBY|ALL LOBBIES))"))
            throw new PrintWithoutMessageCreationException(toBeChecked.replace("PRINT: ",""));

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
                if (model.getLocalPlayerLobbyId() == 0)
                    throw new IllegalArgumentException("You are not inside a lobby yet, please pick one");
                return new StartGameMessage();
            default:
                throw new IllegalArgumentException("Your input was not correct! Please, retry...");
        }
    }

    /**
     * Process the input in the initialization phase (when the players have to decide abbout leader cards and resources, eventually).
     * The exception contains a String with the message to be displayed
     * @param input the input from the player
     * @return a message that has to be send to the server
     * @throws IllegalArgumentException the input is not correct
     * @throws PrintWithoutMessageCreationException the input string was a request of printing the nicknames and the playing order of this game
     */
    public InGameMessage initializationInput(String input) throws IllegalArgumentException, PrintWithoutMessageCreationException{
        String toBeChecked = preprocess(input);

        if (toBeChecked.equals("USERNAMES"))
            throw new PrintWithoutMessageCreationException(toBeChecked);

        if(toBeChecked.matches("(SH[1-3]: (COIN|STONE|SERVANT|SHIELD))")){
            String[] inputParts = toBeChecked.split(":");
            int capacity = Integer.parseInt(""+inputParts[0].charAt(2));
            Resource resource = Resource.fromAlias(inputParts[1].trim());
            return new SelectResourceMessage(capacity, resource);
        }

        try {
            int id = Integer.parseInt(toBeChecked);
            if (!possesLeaderCard(id)){
                throw new IllegalArgumentException("You do not own a leader card with that id! Please, retry...");
            }
            else if (model.getPlayerFromId(model.getLocalPlayerId()).getLeaderCards().size() > 2){
                model.getPlayerFromId(model.getLocalPlayerId()).removeLeaderCard(id);
                return new LeaderDiscardMessage(id);
            } else{
                throw new IllegalArgumentException("You have already discarded enough leader cards!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");
        }
    }

    /**
     * Process the input received during the turn of the local player (during the game).
     * All the exceptions contain a String with the message to be displayed or to be analysed
     * @param input the input from the player
     * @return a message that has to be send to the server
     * @throws IllegalArgumentException the input is not correct
     * @throws PrintWithoutMessageCreationException the input is a request to print
     * @throws SuppressNotificationsException the player want to show or hide notifications
     */
    public InGameMessage inTurnInput(String input) throws IllegalArgumentException, PrintWithoutMessageCreationException, SuppressNotificationsException{
        String toBeChecked = preprocess(input);
        if (toBeChecked.equals("NOTIFICATIONS"))
            throw new SuppressNotificationsException();

        if(toBeChecked.matches("(PRINT: (USERNAMES|(SHELVES|STRONGBOX|SUPPORT CONTAINER|(DEV SLOTS |DEV SLOT [1-3] )(CONTENT|TOP)|LEADER CARDS)( @(\\d)+)?|MARKET( LEGEND)?|DEV CARDS ON TABLE|FAITH TRACK( POINTS| VATICAN RELATIONS)?))"))
            throw new PrintWithoutMessageCreationException(toBeChecked.replace("PRINT: ",""));

        if (toBeChecked.equals("END TURN"))
            return new EndTurnMessage();

        if (toBeChecked.equals("CLEAR ANY SELECTION"))
            return new BackFromAnySelectionMessage();

        if (toBeChecked.equals("BUY CARD"))
            return new BuyDevCardMessage();

        if (toBeChecked.equals("ACTIVATE PRODUCTION POWER"))
            return new ProductionActivationMessage();

        if (toBeChecked.equals("TAKE FROM MARKET"))
            return new TakeFromMarketMessage();

        if (toBeChecked.equals("EXCHANGE"))
            return new ExchangeMessage();

        if (toBeChecked.equals("QUIT"))
            return new QuitFromMarketMessage();

        if(toBeChecked.equals("CHECKOUT"))
            return new PaySelectedMessage();

        if(toBeChecked.matches("DISCOUNT: (LC\\d\\d)"))
            return leaderCardAbility(toBeChecked);

        if (toBeChecked.matches("(PLAY|DISCARD): LC\\d\\d"))
            return actionOnLC(toBeChecked);

        if(toBeChecked.matches("SELECT FROM MARKET: (ROW,[0-2]|COLUMN,[0-3])"))
            return selectInMarket(toBeChecked.replace("SELECT FROM MARKET: ", ""));

        if (toBeChecked.matches("SELECT: (((LC\\d\\d|SB|SC),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONE|SERVANT|SHIELD)))"))
            return resourceSelectionDeselection(toBeChecked.replace("SELECT: ", ""), true);

        if(toBeChecked.matches("DESELECT: (((SB|SC),(COIN|STONE|SERVANT|SHIELD),(\\d)+)|((SH[1-3]),(COIN|STONE|SERVANT|SHIELD)))"))
            return resourceSelectionDeselection(toBeChecked.replace("DESELECT: ", ""), false);

        if (toBeChecked.matches("MOVE TO: (SC|(LC\\d\\d)|(SH[1-3]))"))
            return moveResources(toBeChecked.replace("MOVE TO: ", ""));

        if (toBeChecked.matches("ANY SELECTION: (COIN|STONE|SERVANT|SHIELD)"))
            return new AnySelectionMessage(Resource.fromAlias(toBeChecked.replace("ANY SELECTION: ","")));

        if(toBeChecked.matches("TRANSMUTE: (LC\\d\\d)X((\\d)+),(LC\\d\\d)X((\\d)+)"))
            return leaderCardAbility(toBeChecked);

        if (toBeChecked.matches("SELECT: (LC\\d\\d|DC\\d\\d|BASIC PRODUCTION POWER|ALL PRODUCTION POWERS|DS[1-3]|DD,LEVEL[1-3],COLOR:(GREEN|YELLOW|BLUE|PURPLE))"))
            return objectSelection(toBeChecked.replace("SELECT: ",""));

        throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");
    }

    /**
     * Process the input received during the turn of a player different from the local one (during the game).
     * All the exceptions contain a String with the message to be displayed or to be analysed
     * @param input the input from the player
     * @throws IllegalArgumentException the input is not correct
     * @throws PrintWithoutMessageCreationException the input is a request to print
     * @throws SuppressNotificationsException the player want to show or hide notifications
     */
    public void NOTinTurnInput(String input) throws IllegalArgumentException, PrintWithoutMessageCreationException, SuppressNotificationsException{
        String toBeChecked = preprocess(input);
        if (toBeChecked.equals("NOTIFICATIONS"))
            throw new SuppressNotificationsException();

        if (toBeChecked.matches("(PRINT: (USERNAMES|(SHELVES|STRONGBOX|SUPPORT CONTAINER|(DEV SLOTS |DEV SLOT [1-3] )(CONTENT|TOP)|LEADER CARDS)( @(\\d)+)?|MARKET( LEGEND)?|DEV CARDS ON TABLE|FAITH TRACK( POINTS| VATICAN RELATIONS)?))"))
            throw new PrintWithoutMessageCreationException(toBeChecked.replace("PRINT: ",""));

        throw new IllegalArgumentException("Syntax error: what you wrote was not correct, please, retry...");
    }
}