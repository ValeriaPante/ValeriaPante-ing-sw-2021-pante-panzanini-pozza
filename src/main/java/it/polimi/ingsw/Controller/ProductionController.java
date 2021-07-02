package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Model.Deposit.Depot;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Deposit.Shelf;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Model.Player.DevSlot;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Controller of the production phase in the game
 */
public class ProductionController extends CardActionController{

    private RealPlayer player;

    /**
     * Production Controller constructor
     * @param ftc Faith Track Controller
     */
    public ProductionController(FaithTrackController ftc) {
        super(ftc);
    }

    /**
     * Getter
     * @param player player to evaluate
     * @return all the already selected production powers of a player
     */
    private ArrayList<ProductionPower> getSelectedProductionPowers(RealPlayer player){
        ArrayList<ProductionPower> selectedProdPowers = new ArrayList<>();

        if (player.getBasicProductionPower().isSelected()){
            selectedProdPowers.add(player.getBasicProductionPower().getProductionPower());
        }

        for (DevSlot devSlot : player.getDevSlots()){
            if (!devSlot.isEmpty() && devSlot.topCard().isSelected()){
                selectedProdPowers.add(devSlot.topCard().getProdPower());
            }
        }

        for (LeaderCard leaderCard : player.getLeaderCards()){
            if (leaderCard.isSelected()){
                try {
                    selectedProdPowers.add(leaderCard.getAbility().getProductionPower());
                }
                catch (WrongLeaderCardType ignored){
                }
            }
        }

        return selectedProdPowers;
    }

    /**
     * Check if the player can afford to activate this production power plus all the production powers already selected
     * @param productionPower the new production power
     * @param player the player that wants to activate it
     * @return true if the player can afford to activate this production power plus all the production powers already selected, false otherwise
     */
    private boolean isAffordableSomehow(ProductionPower productionPower, RealPlayer player){
        Depot resourceRequired = new Depot();
        for (ProductionPower alreadySelectedProdPower : this.getSelectedProductionPowers(player)){
            resourceRequired.addEnumMap(alreadySelectedProdPower.getInput());
        }
        resourceRequired.addEnumMap(productionPower.getInput());
        return super.isAffordableSomehow(resourceRequired.content());
    }

    /**
     * Select a production power
     * @param idCard the id of a card production (0 for the basic production power)
     * @param player player with that production power
     */
    private void selectCard(int idCard, RealPlayer player){

        if (idCard == 0){
            if (player.getBasicProductionPower().isSelected()){
                player.getBasicProductionPower().select();
            }
            else{
                if (this.isAffordableSomehow(player.getBasicProductionPower().getProductionPower(), player)){
                    player.getBasicProductionPower().select();
                }
                else{
                    player.setErrorMessage("You can't activate this production because you don't have enough resources", idCard);
                    return;
                }
            }
            return;
        }

        for (DevSlot devSlot : player.getDevSlots()) {
            if (!devSlot.isEmpty() && devSlot.topCard().getId() == idCard) {
                if (devSlot.topCard().isSelected()) {
                    devSlot.selectTopCard();
                } else {
                    if (this.isAffordableSomehow(devSlot.topCard().getProdPower(), player)) {
                        devSlot.selectTopCard();
                    } else {
                        player.setErrorMessage("You can't activate this production because you don't have enough resources", idCard);
                    }
                }
                return;
            }
        }

        for (LeaderCard leaderCard : player.getLeaderCards()) {
            if (leaderCard.getId() == idCard) {
                if (leaderCard.isSelected()) {
                    leaderCard.select();
                } else {
                    ProductionPower leaderProductionPower;
                    try {
                        leaderProductionPower = leaderCard.getAbility().getProductionPower();
                    } catch (WrongLeaderCardType e) {
                        player.setErrorMessage("This is not a production LeaderCard", idCard);
                        return;
                    }

                    if (this.isAffordableSomehow(leaderProductionPower, player)) {
                        leaderCard.select();
                    } else {
                        player.setErrorMessage("You can't activate this production because you don't have enough resources", idCard);
                    }
                }
                return;
            }
        }

        player.setErrorMessage("Card not owned", idCard);
    }

    /**
     *
     * @return true if the player of turn is in a state in which he can do something related to the production
     */
    private boolean isTurnTypeValid(){
        this.player = super.table.turnOf();

        if (this.player.getMacroTurnType() == MacroTurnType.NONE && this.player.getMicroTurnType() == MicroTurnType.NONE){
            this.player.setMacroTurnType(MacroTurnType.PRODUCTION);
            this.player.setMicroTurnType(MicroTurnType.SETTING_UP);
            return true;
        }
        else{
            return this.player.getMacroTurnType() == MacroTurnType.PRODUCTION && this.player.getMicroTurnType() == MicroTurnType.SETTING_UP;
        }
    }

    /**
     * Select a card production if possible, if it's already selected deselects it
     * If the card is not a production card doesn't get selected
     * @param idCard the card id
     */
    public void selectCardProduction(int idCard){
        if (!this.isTurnTypeValid()){
            return;
        }
        this.selectCard(idCard, this.player);

        this.checkResetCondition();
    }

    /**
     * Selects all production Cards if possible
     * Those dont get selected if the player does not have enough resources to activate everything
     */
    public void selectAllProductionPowers(){
        if (!this.isTurnTypeValid()){
            return;
        }

        Depot allInputs = new Depot();

        for (ProductionPower productionPower : this.player.getAllProductionPowers()){
            allInputs.addEnumMap(productionPower.getInput());
        }

        if (!super.isAffordableSomehow(allInputs.content())){
            this.player.setErrorMessage("you can't activate all production Powers together because you don' have enough resources");
            this.checkResetCondition();
            return;
        }

        if (!this.player.getBasicProductionPower().isSelected()) {
            this.player.getBasicProductionPower().select();
        }
        for (DevSlot devSlot : this.player.getDevSlots()){
            if (!devSlot.isEmpty() && !devSlot.topCard().isSelected()){
                devSlot.selectTopCard();
            }
        }

        for (LeaderCard leaderCard : this.player.getLeaderCards()){
            if (leaderCard.hasBeenPlayed()){
                if (!leaderCard.isSelected()) {
                    try {
                        leaderCard.getAbility().getProductionPower();
                        leaderCard.select();
                    } catch (WrongLeaderCardType ignored) {
                    }
                }
            }
        }
        this.player.setMacroTurnType(MacroTurnType.PRODUCTION);
        this.player.setMicroTurnType(MicroTurnType.SETTING_UP);
    }

    /**
     * Selects a resource in a shelf
     * @param resource resource type
     * @param numberOfShelf the target shelf
     * @return true if i handled this selection
     */
    public boolean selectionFromShelf(Resource resource, int numberOfShelf){
        if (!this.isTurnTypeValid()){
            return false;
        }
        super.selectFromShelf(resource, numberOfShelf);
        this.checkResetCondition();
        return true;
    }

    /**
     * Deselection a resource in a shelf
     * @param resource resource type
     * @param numberOfShelf the target shelf
     * @return true if i handled this selection
     */
    public boolean deselectionFromShelf(Resource resource, int numberOfShelf){
        if (!this.isTurnTypeValid()){
            return false;
        }
        super.deselectFromShelf(resource, numberOfShelf);
        this.checkResetCondition();
        return true;
    }

    /**
     * Selects a resource type and amount in the strongbox
     * @param resource resource type
     * @param quantity resource amount
     * @return true if i handled this selection
     */
    public boolean selectionFromStrongBox(Resource resource, int quantity){
        if (!this.isTurnTypeValid()){
            return false;
        }
        super.selectFromStrongBox(resource, quantity);
        this.checkResetCondition();
        return true;
    }

    /**
     * Deselects a resource type and amount in the strongbox
     * @param resource resource type
     * @param quantity resource amount
     * @return true if i handled this selection
     */
    public boolean deselectionFromStrongBox(Resource resource, int quantity){
        if (!this.isTurnTypeValid()){
            return false;
        }
        super.deselectFromStrongBox(resource, quantity);
        this.checkResetCondition();
        return true;
    }

    /**
     * Selects a resource in a storage type Leader card, if its already selected deselect it
     * @param resource resource type
     * @param idCard the card id
     * @param resPosition the position of the resource in the storage
     * @return true if i handled this selection
     */
    public boolean selectionFromLeaderStorage(Resource resource, int idCard, int resPosition){
        if (!this.isTurnTypeValid()){
            return false;
        }
        super.selectFromLeaderStorage(resource, idCard, resPosition);
        this.checkResetCondition();
        return true;
    }

    /**
     * Rollback if the player of turn has no production powers selected and no resources selected
     */
    private void checkResetCondition(){
        this.player = super.table.turnOf();
        if (this.getSelectedProductionPowers(this.player).isEmpty()){
            if (super.getPayableWithSelection().isEmpty()){
                this.player.setMacroTurnType(MacroTurnType.NONE);
                this.player.setMicroTurnType(MicroTurnType.NONE);
            }
        }
    }

    /**
     * Activate the production if possible
     * The production doesn't start if the player has selected more o less resources that are needed
     * Sets the turn to ANYDECISION if there is some any in output
     */
    public void activateProduction(){
        this.player = super.table.turnOf();

        if (!(this.player.getMacroTurnType() == MacroTurnType.PRODUCTION && this.player.getMicroTurnType() == MicroTurnType.SETTING_UP)){
            this.player.setErrorMessage("You can't now");
            return;
        }

        ArrayList<ProductionPower> selectedProdPowers = this.getSelectedProductionPowers(this.player);
        if (selectedProdPowers.isEmpty()){
            this.player.setErrorMessage("Please select some production power");
            return;
        }

        Depot resourceSelected = new Depot();

        for (Shelf shelf : this.player.getShelves()){
            if (!shelf.isEmpty() && shelf.getQuantitySelected()!=0) {
                resourceSelected.addEnumMap(new EnumMap<>(Resource.class) {{
                    put(shelf.getResourceType(), shelf.getQuantitySelected());
                }});
            }
        }

        if (this.player.getStrongBox().areThereSelections()) {
            resourceSelected.addEnumMap(this.player.getStrongBox().getSelection());
        }

        for (LeaderCard leaderCard : this.player.getLeaderCards()){
            if (leaderCard.hasBeenPlayed()){
                try{
                    if (!leaderCard.getAbility().getSelected().isEmpty()){
                        resourceSelected.addEnumMap(leaderCard.getAbility().getSelected());
                    }
                }
                catch(WrongLeaderCardType ignored){
                }
            }
        }

        if (resourceSelected.isEmpty()){
            this.player.setErrorMessage("Please select some resources");
            return;
        }

        Depot allInputs = new Depot();
        Depot allOutputs = new Depot();
        for (ProductionPower productionPower : selectedProdPowers){
            allInputs.addEnumMap(productionPower.getInput());
            allOutputs.addEnumMap(productionPower.getOutput());
        }

        if (allInputs.content().containsKey(Resource.ANY)){
            int anyAmount = allInputs.content().get(Resource.ANY);
            EnumMap<Resource, Integer> inputsWithoutAny = allInputs.content();
            inputsWithoutAny.remove(Resource.ANY);

            Depot temp = new Depot(){{addEnumMap(inputsWithoutAny);}};
            if (resourceSelected.contains(inputsWithoutAny) && resourceSelected.countAll() == temp.countAll() + anyAmount){
                this.setANYDECISIONIfNeeded(this.player, super.getPayableWithSelection(), allOutputs.content());
            }
            else{
                this.player.setErrorMessage("Selection doesn't match the cost");
            }
        }
        else{
            if (resourceSelected.content().equals(allInputs.content())){
                this.setANYDECISIONIfNeeded(this.player, super.getPayableWithSelection(), allOutputs.content());
            }
            else{
                this.player.setErrorMessage("Selection doesn't match the cost");
            }
        }
    }

    /**
     * Check if the output of the production has some any inside, if so updates the player state
     * @param player the player that activated the production
     * @param payableWithSelection All the storages with some selection in it
     * @param toPutInStrongBox the output of the production
     */
    private void setANYDECISIONIfNeeded(RealPlayer player, List<Payable> payableWithSelection, EnumMap<Resource, Integer> toPutInStrongBox){
        if (toPutInStrongBox.containsKey(Resource.ANY)){
            player.getSupportContainer().clear();
            table.updatePlayerOfTurnSupportContainer(new EnumMap<>(Resource.class){{put(Resource.ANY, toPutInStrongBox.get(Resource.ANY));}});
            player.setMicroTurnType(MicroTurnType.ANY_DECISION);
        }
        else{
            this.effectiveTransaction(player, payableWithSelection, toPutInStrongBox);
        }
    }

    /**
     * Deselect all the production cards that the player has
     * @param player the player to deselect all the production cards
     */
    private void deselectAllProdPowersSelected(RealPlayer player){
        if (player.getBasicProductionPower().isSelected()){
            player.getBasicProductionPower().select();
        }

        for (DevSlot devSlot : player.getDevSlots()){
            if (!devSlot.isEmpty() && devSlot.topCard().isSelected()){
                devSlot.selectTopCard();
            }
        }

        for (LeaderCard leaderCard : player.getLeaderCards()){
            if (leaderCard.isSelected()){
                leaderCard.select();
            }
        }
    }

    /**
     * Removes all the selected resources and put the output in the strongbox
     * @param player player that activated the production
     * @param payableWithSelection All the storages with some selection in it
     * @param toPutInStrongBox the output of the production to be put in the strongbox
     */
    private void effectiveTransaction(RealPlayer player, List<Payable> payableWithSelection, EnumMap<Resource, Integer> toPutInStrongBox){
        for (Payable payable : payableWithSelection){
            this.table.payPlayerOfTurn(payable);
        }
        Integer faithPoints = toPutInStrongBox.remove(Resource.FAITH);
        this.table.addToPlayerOfTurnStrongbox(toPutInStrongBox);

        if (faithPoints != null){
            super.faithTrackController.movePlayerOfTurn(faithPoints);
        }
        player.setMacroTurnType(MacroTurnType.DONE);
        player.setMicroTurnType(MicroTurnType.NONE);

        this.deselectAllProdPowersSelected(player);
    }

    /**
     * Stores a resource that a any will be converted into
     * @param resource A resource that a any will be converted into
     */
    public void anySelection(Resource resource){
        RealPlayer player = super.table.turnOf();
        if (player.getMicroTurnType() != MicroTurnType.ANY_DECISION) {
            player.setErrorMessage("You can't do this now");
            return;
        }
        if (resource == Resource.ANY || resource == Resource.WHITE || resource == Resource.FAITH){
            player.setErrorMessage("Resource specified not allowed");
            return;
        }

        this.table.addToSupportContainer(new EnumMap<>(Resource.class){{put(resource, 1);}});
        int anyAmount = player.getSupportContainer().content().get(Resource.ANY);

        if (player.getSupportContainer().countAll() == 2*anyAmount){
            Depot allOutputs = new Depot();
            for (ProductionPower productionPower : this.getSelectedProductionPowers(player)){
                allOutputs.addEnumMap(productionPower.getOutput());
            }
            allOutputs.addEnumMap(player.getSupportContainer().content());
            super.table.updatePlayerOfTurnSupportContainer(new EnumMap<>(Resource.class));
            this.effectiveTransaction(player, super.getPayableWithSelection(), new EnumMap<>(allOutputs.content()){{remove(Resource.ANY);}});
        }
    }

    /**
     * Allows to go back to the selection of resources and cards from the selection of the Any as output
     */
    public void backFromAnySelection(){
        this.player = super.table.turnOf();
        if (this.player.getMicroTurnType() != MicroTurnType.ANY_DECISION){
            this.player.setErrorMessage("You can't go back from here");
            return;
        }
        super.table.updatePlayerOfTurnSupportContainer(new EnumMap<>(Resource.class));
        super.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
    }

}
