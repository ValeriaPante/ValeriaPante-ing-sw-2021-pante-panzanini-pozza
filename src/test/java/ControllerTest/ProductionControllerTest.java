package ControllerTest;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.ProductionController;
import it.polimi.ingsw.Enums.LeaderCardType;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionControllerTest {

    private FaithTrackController faithTrackController;
    private ProductionController productionController;
    private Table table;

    @BeforeEach
    @DisplayName("Setup")
    private void init(){
        this.table = new Table(4){{
            addPlayer(new RealPlayer("A"));
            addPlayer(new RealPlayer("B"));
            addPlayer(new RealPlayer("C"));
            addPlayer(new RealPlayer("D"));
        }};

        this.faithTrackController = new FaithTrackController(table);
        this.productionController = new ProductionController(this.faithTrackController);

        //in questo modo simulo che la partita sia in corso
        for (RealPlayer player : this.table.getPlayers()){
            player.setMacroTurnType(MacroTurnType.NONE);
            player.setMicroTurnType(MicroTurnType.NONE);
        }
    }

    @Test
    @DisplayName("selectCardProduction method test")
    public void selectCardProductionTest(){
        //selectBasicProductionPower
        this.productionController.selectCardProduction(0);

        //non ha risorse quindi non viene selezionato
        assertFalse(this.table.turnOf().getBasicProductionPower().isSelected());
        //e pure il turnType non cambia visto che non è stata selezionata
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        this.table.getPlayers()[0].getShelves()[0].singleAdd(Resource.COIN);
        this.table.getPlayers()[0].getShelves()[2].addAllIfPossible(Resource.STONE, 2);

        //ora che ha abbastanza risorse viene selezionato
        this.productionController.selectCardProduction(0);
        assertTrue(this.table.turnOf().getBasicProductionPower().isSelected());
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    //---Selection-Resource
    @Test
    @DisplayName("selectionFromShelf method test")
    public void selectionFromShelfTest(){
        //Resource not present
        //Selection handled but nothing has changed
        assertTrue(this.productionController.selectionFromShelf(Resource.COIN, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
        assertTrue(this.table.turnOf().getShelves()[0].isEmpty());

        //adding a resource to the first shelf
        this.table.turnOf().getShelves()[0].singleAdd(Resource.COIN);
        assertTrue(this.productionController.selectionFromShelf(Resource.COIN, 1));
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("deselectionFromShelf method test")
    public void deselectionFromShelfTest(){
        //Resource not present
        //Deselection handled but nothing has changed
        assertTrue(this.productionController.deselectionFromShelf(Resource.COIN, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //adding a resource to the first shelf
        this.table.turnOf().getShelves()[0].singleAdd(Resource.COIN);
        this.table.turnOf().getShelves()[0].singleSelection();
        this.table.turnOf().getShelves()[1].singleAdd(Resource.SERVANT);
        this.table.turnOf().getShelves()[1].singleSelection();

        //since there is something still selected the turnType does not change
        assertTrue(this.productionController.deselectionFromShelf(Resource.COIN, 1));
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());

        assertTrue(this.productionController.deselectionFromShelf(Resource.SERVANT, 2));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("selectionFromStrongBox method test")
    public void selectionFromStrongBoxTest(){
        //Resource not present
        //Selection handled but nothing has changed
        assertTrue(this.productionController.selectionFromStrongBox(Resource.COIN, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //adding resources to the strongBox
        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SERVANT, 1);
        }});

        //since there is something still selected the turnType does not change
        assertTrue(this.productionController.selectionFromStrongBox(Resource.STONE, 1));
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("deselectionFromStrongBox method test")
    public void deselectionFromStrongBoxTest(){
        //Resource not present
        //Deselection handled but nothing has changed
        assertTrue(this.productionController.deselectionFromStrongBox(Resource.COIN, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //adding resources to the strongBox
        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.SERVANT, 1);
        }});
        this.table.turnOf().getStrongBox().singleSelection(Resource.STONE);
        this.table.turnOf().getStrongBox().singleSelection(Resource.SERVANT);

        //since there is something still selected the turnType does not change
        assertTrue(this.productionController.deselectionFromStrongBox(Resource.STONE, 1));
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());

        //nothing more selected
        assertTrue(this.productionController.deselectionFromStrongBox(Resource.SERVANT, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("selectionFromLeaderStorage method Test")
    public void selectionFromLeaderStorage(){
        //card that the player doesn't have
        //Selection handled but nothing changes
        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.STONE, 4, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //setting up a leader card
        LeaderCard leaderCard = new LeaderCard(3, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
            put(Resource.COIN, 1);
        }}, 30);
        leaderCard.play();

        leaderCard.getAbility().add(Resource.STONE);
        leaderCard.getAbility().add(Resource.STONE);
        leaderCard.getAbility().add(Resource.COIN);
        this.table.turnOf().addLeaderCard(leaderCard);

        assertEquals(this.table.turnOf().getLeaderCards()[0].getAbility().getContent(), new EnumMap<>(Resource.class){{
            put(Resource.STONE, 2);
            put(Resource.COIN, 1);
        }});

        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.STONE, 30, 2));
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());

        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.COIN, 30, 1));

        //this resource doee not exist
        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.STONE, 30, 5));

        assertEquals(1, this.table.turnOf().getLeaderCards()[0].getAbility().getSelected().get(Resource.STONE));

        //i know that there is only one leaderCard
        assertEquals(this.table.turnOf().getLeaderCards()[0].getAbility().getSelected().size(), 2);

    }

    @Test
    public void selectAllProductionPowersTest(){
        //situazione in cui ho un giocatore con nulla dentro
        //assertEquals();
    }

}
