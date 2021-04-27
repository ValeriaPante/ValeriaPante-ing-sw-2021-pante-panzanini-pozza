package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.DevSlot;
import it.polimi.ingsw.Player.RealPlayer;
import it.polimi.ingsw.Enums.Resource;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ProductionController extends CardActionController{

    RealPlayer player;

    public ProductionController(Table table) {
        super(table);
    }

    //mi serve un modo per capire quali poteri di produzone sono già selezionati
    private ArrayList<ProductionPower> getSelectedProductionPowers(RealPlayer player){
        ArrayList<ProductionPower> selectedProdPowers = new ArrayList<>();

        if (player.getBasicProductionPower().isSelected()){
            selectedProdPowers.add(player.getBasicProductionPower().getProductionPower());
        }

        for (DevSlot devSlot : player.getDevSlots()){
            if (devSlot.topCard().isSelected()){
                selectedProdPowers.add(devSlot.topCard().getProdPower());
            }
        }

        for (LeaderCard leaderCard : player.getLeaderCards()){
            if (leaderCard.isSelected()){
                try {
                    selectedProdPowers.add(leaderCard.getAbility().getProductionPower());
                }
                catch (WeDontDoSuchThingsHere e){
                    //non dovrebbe succedere di arrivare qui
                }
            }
        }

        return selectedProdPowers;
    }

    private boolean isAffordableSomehow(ProductionPower productionPower, RealPlayer player){
        //mi serve un modo per capire quali altri poteri di produzone sono già selezionati
        Depot resourceRequired = new Depot();
        for (ProductionPower alreadySelectedProdPower : this.getSelectedProductionPowers(player)){
            resourceRequired.addEnumMap(alreadySelectedProdPower.getInput());
        }
        return super.isAffordableSomehow(resourceRequired.content());
    }

    private void select(int idCard, RealPlayer player){

        if (idCard == 0){
            if (player.getBasicProductionPower().isSelected()){
                player.getBasicProductionPower().select();
            }
            else{
                if (this.isAffordableSomehow(player.getBasicProductionPower().getProductionPower(), player)){
                    player.getBasicProductionPower().select();
                }
            }
        }
        else{
            //cerco questo specifico id in tutte le carte che il giocatore possiede
            boolean found = false;

            //devCards
            DevSlot[] devSlots = player.getDevSlots();
            for (int i=0; i<devSlots.length && !found; i++){
                if (devSlots[i].topCard().getId() == idCard){
                    found = true;
                    if (devSlots[i].topCard().isSelected()){
                        devSlots[i].topCard().select();
                    }
                    else{
                        if (this.isAffordableSomehow(devSlots[i].topCard().getProdPower(), player)){
                            devSlots[i].topCard().select();
                        }
                        else{
                            //eccezione / modifica nel model che l'ultima azione è scorretta
                            //non si può permettere questa carta
                            return; //da eliminare
                        }
                    }
                }
            }

            if (!found){
                LeaderCard[] leaderCards = player.getLeaderCards();
                for (int i=0; i<leaderCards.length && !found; i++){
                    if (leaderCards[i].getId()==idCard){
                        found = true;
                        if (leaderCards[i].isSelected()){
                            leaderCards[i].select();
                        }
                        else{
                            ProductionPower leaderProductionPower;
                            try {
                                leaderProductionPower = leaderCards[i].getAbility().getProductionPower();
                            }
                            catch(WeDontDoSuchThingsHere e){
                                //eccezione / modifica nel model che l'ultima azione è scorretta
                                //ha selezionato una carta che non è di tipo produzione
                                return; //da eliminare
                            }

                            if (this.isAffordableSomehow(leaderProductionPower, player)){
                                leaderCards[i].select();
                            }
                            else{
                                //eccezione / modifica nel model che l'ultima azione è scorretta
                                //non si può permettere questa carta
                            }
                        }
                    }
                }
            }

            if (!found){
                //eccezione / modifica nel model che l'ultima azione è scorretta
                //il giocatore ha specificato una carta che non possiede
                return; //da eliminare
            }
        }
    }

    public void selectCardProduction(int idCard){
        this.player = super.table.turnOf();
        if (player.getMacroTurnType() == MacroTurnType.NONE){
            player.setMacroTurnType(MacroTurnType.PRODUCTION);
            player.setMicroTurnType(MicroTurnType.SETTINGUP);
            this.select(idCard, player);
        }
        else if (player.getMacroTurnType() == MacroTurnType.PRODUCTION){
            if (player.getMicroTurnType() == MicroTurnType.NONE){
                player.setMicroTurnType(MicroTurnType.SETTINGUP);
            }
            this.select(idCard, player);
        }
        else{
            //eccezione / modifica nel model che l'ultima azione è scorretta
            //non può fare questa cosa in questo momento, non rispetta il flow
            return; //da eliminare
        }


        //DEVO CAPIRE QUANTE CARTE SONO SELEZIONATE PER SETTARE IL TURNO A NONE NEL CASO VOLESSE FARE ALTRO
        if (this.getSelectedProductionPowers(player).isEmpty()){
            // -> significa che non ci sono carte selezionate

            //chiedere ai ragazzi se per loro ha senso che anche le risorse vadano deselezionate
        }

    }

    public void selectAllProductionPowers(){
        this.player = super.table.turnOf();
        Depot allInputs = new Depot();

        for (ProductionPower productionPower : this.player.getAllProductionPowers()){
            allInputs.addEnumMap(productionPower.getInput());
        }

        ProductionPower allInputsCombined =  new ProductionPower(allInputs.content(), new EnumMap<>(Resource.class));
        if (!this.isAffordableSomehow(allInputsCombined, this.player)){
            //eccezione / modifica nel model che l'ultima azione è scorretta
            //non ha abbastanza risorse per attivarli tutti
            return; //da eliminare
        }

        if (!this.player.getBasicProductionPower().isSelected()) {
            this.player.getBasicProductionPower().select();
        }
        for (DevSlot devSlot : this.player.getDevSlots()){
            if (!devSlot.isEmpty() && !devSlot.topCard().isSelected()){
                devSlot.topCard().select();
            }
        }

        for (LeaderCard leaderCard : this.player.getLeaderCards()){
            if (leaderCard.hasBeenPlayed()){
                if (!leaderCard.isSelected()) {
                    try {
                        leaderCard.getAbility().getProductionPower();
                        leaderCard.select();
                    } catch (WeDontDoSuchThingsHere e) {
                        //non è una carta con poteri di produzione
                    }
                }
            }
        }
        //a questo punto sono tutti selezionati
    }

    public void SelectResource(Payable storage, Resource resource, int amount, int pos){
        //chiamo super con i metodi che abbiamo deciso
    }

    public void activateProduction(){
        this.player = super.table.turnOf();
        ArrayList<ProductionPower> selectedProdPowers = this.getSelectedProductionPowers(this.player);

        //----Vado a prendermi tutte le risorse selezionate
        Depot resourceSelected = new Depot();

        for (Shelf shelf : this.player.getShelves()){
            if (!shelf.isEmpty()) {
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
                catch(WeDontDoSuchThingsHere e){
                    //non è una carta con poteri di produzione
                }
            }
        }
        //----

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
                //il pagamento passa
                //bisogna capire se negli output ci sono delle any
                this.setANYDECISIONIfNeeded(this.player, super.getPayableWithSelection(), allOutputs.content());
            }
            else{
                //errore:
                //le risorse selezionate non sono abbastanza da coprire il costo delle cate selezionate
            }
        }
        else{
            if (resourceSelected.content().equals(allInputs.content())){
                //il pagamento passa
                //bisogna capire se negli output ci sono delle any
                this.setANYDECISIONIfNeeded(this.player, super.getPayableWithSelection(), allOutputs.content());
            }
            else{
                //errore:
                //le risorse selezionate non sono abbastanza da coprire il costo delle carte selezionate
            }
        }
    }

    private void setANYDECISIONIfNeeded(RealPlayer player, List<Payable> payableWithSelection, EnumMap<Resource, Integer> toPutInStrongBox){
        if (toPutInStrongBox.containsKey(Resource.ANY)){
            player.getSupportContainer().clear();
            player.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{put(Resource.ANY, toPutInStrongBox.get(Resource.ANY));}});
            player.setMicroTurnType(MicroTurnType.ANYDECISION);
        }
        else{
            this.effectiveTransaction(player, payableWithSelection, toPutInStrongBox);
        }
    }

    private void deselectAllProdPowersSelected(RealPlayer player){
        if (player.getBasicProductionPower().isSelected()){
            player.getBasicProductionPower().select();
        }

        for (DevSlot devSlot : player.getDevSlots()){
            if (!devSlot.isEmpty() && devSlot.topCard().isSelected()){
                devSlot.topCard().select();
            }
        }

        //non controllo nemmeno se è un leadercard si tipo prodizione, deseleziono comunque tutto
        for (LeaderCard leaderCard : player.getLeaderCards()){
            if (leaderCard.isSelected()){
                leaderCard.select();
            }
        }
    }

    private void effectiveTransaction(RealPlayer player, List<Payable> payableWithSelection, EnumMap<Resource, Integer> toPutInStrongBox){
        for (Payable payable : payableWithSelection){
            payable.pay();
        }
        player.getStrongBox().addEnumMap(toPutInStrongBox);
        player.setActionDone(true);
        player.setMacroTurnType(MacroTurnType.NONE);
        player.setMicroTurnType(MicroTurnType.NONE);

        this.deselectAllProdPowersSelected(player);
        //le risorse selezionate non dovrebebro più esserci
    }

    public void anySelection(Resource resource){
        RealPlayer player = super.table.turnOf();
        if (resource == Resource.ANY || resource == Resource.WHITE){
            //exception:
            //risorsa inserita non conforme
            return; //da cancellare
        }
        if (player.getMicroTurnType() != MicroTurnType.ANYDECISION) {
            //exception:
            //non rispetta il flow del turno
            return; //da cancellare
        }

        //a questo punto sono in tra
        player.getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{put(resource, 1);}});
        int anyAmount = player.getSupportContainer().content().get(Resource.ANY);

        if (player.getSupportContainer().countAll() == 2*anyAmount){
            //devo ottenere tutto l'output dei poteri di produzione
            Depot allOutputs = new Depot();
            for (ProductionPower productionPower : this.getSelectedProductionPowers(player)){
                allOutputs.addEnumMap(productionPower.getOutput());
            }
            //se sono finito qui significa che in questo output ci sono sicuramente delle any che devo togliere
            allOutputs.addEnumMap(player.getSupportContainer().content());
            this.effectiveTransaction(player, super.getPayableWithSelection(), new EnumMap<>(allOutputs.content()){{remove(Resource.ANY);}});
        }
    }

    public void backFromAnySelection(){
        super.table.turnOf().getSupportContainer().clear();
        super.table.turnOf().setMicroTurnType(MicroTurnType.SETTINGUP);
    }

}
