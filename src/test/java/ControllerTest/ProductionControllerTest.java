package ControllerTest;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Controller.FaithTrackController;
import it.polimi.ingsw.Controller.ProductionController;
import it.polimi.ingsw.Enums.*;
import it.polimi.ingsw.Model.Game.Table;
import it.polimi.ingsw.Model.Player.RealPlayer;
import it.polimi.ingsw.PreGameModel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionControllerTest {

    private ProductionController productionController;
    private Table table;

    @BeforeEach
    @DisplayName("Setup")
    private void init(){
        this.table = new Table(4){{
            addPlayer(new RealPlayer(new User("A", new FakeConnectionHandler(1))));
            addPlayer(new RealPlayer(new User("B", new FakeConnectionHandler(2))));
            addPlayer(new RealPlayer(new User("C", new FakeConnectionHandler(3))));
            addPlayer(new RealPlayer(new User("D", new FakeConnectionHandler(4))));
        }};

        FaithTrackController faithTrackController = new FaithTrackController(table);
        this.productionController = new ProductionController(faithTrackController);

        //in questo modo simulo che la partita sia in corso
        for (RealPlayer player : this.table.getPlayers()){
            player.setMacroTurnType(MacroTurnType.NONE);
            player.setMicroTurnType(MicroTurnType.NONE);
        }
    }

    @Test
    @DisplayName("selectCardProduction (basic production powers) method test")
    public void selectCardProduction1Test(){
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        this.productionController.selectCardProduction(0);

        //nothing has changed
        assertFalse(this.table.turnOf().getBasicProductionPower().isSelected());
        assertEquals(MacroTurnType.BUY_NEW_CARD, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

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

        //ora la deseleziono
        this.productionController.selectCardProduction(0);
        assertFalse(this.table.turnOf().getBasicProductionPower().isSelected());

        //visto che non ha altro di selezionato
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("selectCardProduction (devCard) method test")
    public void selectCardProduction2Test(){
        LeaderCard leaderCard = new LeaderCard(3, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
            put(Resource.COIN, 1);
        }}, new EnumMap<>(Resource.class), 20);
        leaderCard.play();

        DevCard devCard = new DevCard(2, new EnumMap<>(Resource.class), new DevCardType(1, Colour.PURPLE), new ProductionPower(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE,1);
        }}, new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.FAITH, 1);
        }}), 30);

        this.table.turnOf().getDevSlots()[0].addCard(devCard);

        //non ha risorse -> il turnType non cambia visto che non è stata selezionata
        this.productionController.selectCardProduction(30);
        assertFalse(this.table.turnOf().getDevSlots()[0].topCard().isSelected());
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        leaderCard.getAbility().add(Resource.COIN);
        leaderCard.getAbility().add(Resource.STONE);
        leaderCard.getAbility().add(Resource.STONE);

        this.table.turnOf().addLeaderCard(leaderCard);

        //ha risorse -> il turnType cambia visto che è stata selezionata
        this.productionController.selectCardProduction(30);
        assertTrue(this.table.turnOf().getDevSlots()[0].topCard().isSelected());
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("selectCardProduction (LeaderCard) method test")
    public void selectCardProduction3Test(){
        LeaderCard leaderCard = new LeaderCard(2, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.PRODPOWER, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE,1);
        }}, new EnumMap<>(Resource.class),20);
        leaderCard.play();
        this.table.turnOf().addLeaderCard(leaderCard);

        //non ha risorse -> il turnType non cambia visto che non è stata selezionata
        this.productionController.selectCardProduction(20);
        assertFalse(this.table.turnOf().getLeaderCards()[0].isSelected());
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 3);
            put(Resource.SERVANT, 1);
            put(Resource.STONE, 2);
        }});

        //ha risorse -> il turnType cambia visto che è stata selezionata
        this.productionController.selectCardProduction(20);
        assertTrue(this.table.turnOf().getLeaderCards()[0].isSelected());
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("selectAllProductionPowers method test DA FINIRE")
    public void selectAllProductionPowersTest(){
        //ha solo il basicProductionPower ma non ha abbastanza risorse -> nulla viene selezionato
        this.productionController.selectAllProductionPowers();
        assertFalse(this.table.turnOf().getBasicProductionPower().isSelected());
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //do al giocatore una leadercard production e una dev card
        DevCard devCard = new DevCard(2, new EnumMap<>(Resource.class), new DevCardType(1, Colour.PURPLE), new ProductionPower(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE,1);
        }}, new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.FAITH, 1);
        }}), 30);
        this.table.turnOf().getDevSlots()[0].addCard(devCard);

        LeaderCard leaderCard1 = new LeaderCard(2, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.PRODPOWER, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT,1);
        }}, new EnumMap<>(Resource.class),20);
        leaderCard1.play();
        this.table.turnOf().addLeaderCard(leaderCard1);

        //mi servono 2 qualsiasi, 2 coin, 1 stone, 1 servant

        LeaderCard leaderCard2 = new LeaderCard(1, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
        }}, new EnumMap<>(Resource.class),15);
        leaderCard2.play();
        leaderCard2.getAbility().add(Resource.COIN);
        leaderCard2.getAbility().add(Resource.COIN);
        leaderCard2.getAbility().add(Resource.SERVANT);
        this.table.turnOf().addLeaderCard(leaderCard2);

        this.table.turnOf().getShelves()[0].singleAdd(Resource.SHIELD);
        this.table.turnOf().getShelves()[1].singleAdd(Resource.STONE);

        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
        }});

        //ha risorse -> il turnType cambia visto che è stata selezionata
        this.productionController.selectAllProductionPowers();
        assertTrue(this.table.turnOf().getBasicProductionPower().isSelected());
        assertTrue(this.table.turnOf().getDevSlots()[0].topCard().isSelected());
        assertTrue(this.table.turnOf().getLeaderCards()[0].isSelected());
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

    //---Selection-Resource
    @Test
    @DisplayName("selectionFromShelf method test")
    public void selectionFromShelfTest(){
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        assertFalse(this.productionController.selectionFromShelf(Resource.COIN, 1));

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

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
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        assertFalse(this.productionController.deselectionFromShelf(Resource.COIN, 1));

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

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
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        assertFalse(this.productionController.selectionFromStrongBox(Resource.COIN, 1));

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

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
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        assertFalse(this.productionController.deselectionFromStrongBox(Resource.COIN, 1));

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

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
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
        assertFalse(this.productionController.selectionFromLeaderStorage(Resource.STONE, 4, 1));

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.NONE);
        this.table.turnOf().setMicroTurnType(MicroTurnType.NONE);

        //card that the player doesn't have
        //Selection handled but nothing changes
        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.STONE, 4, 1));
        assertEquals(MacroTurnType.NONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());

        //setting up a leader card
        LeaderCard leaderCard = new LeaderCard(3, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.STONE, 3);
            put(Resource.COIN, 1);
        }}, new EnumMap<>(Resource.class),30);
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

        //this resource does not exist
        assertTrue(this.productionController.selectionFromLeaderStorage(Resource.STONE, 30, 5));

        assertEquals(1, this.table.turnOf().getLeaderCards()[0].getAbility().getSelected().get(Resource.STONE));

        //i know that there is only one leaderCard
        assertEquals(this.table.turnOf().getLeaderCards()[0].getAbility().getSelected().size(), 2);

    }

    @Test
    @DisplayName("activateProduction (without any as output) method test")
    public void  activateProductionTest1(){
        //do al giocatore due devCard
        DevCard devCard1 = new DevCard(2, new EnumMap<>(Resource.class), new DevCardType(1, Colour.PURPLE), new ProductionPower(new EnumMap<>(Resource.class){{
            put(Resource.ANY, 1);
            put(Resource.STONE,1);
        }}, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
        }}), 30);
        this.table.turnOf().getDevSlots()[0].addCard(devCard1);

        DevCard devCard2 = new DevCard(2, new EnumMap<>(Resource.class), new DevCardType(1, Colour.PURPLE), new ProductionPower(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT,1);
        }}, new EnumMap<>(Resource.class){{
            put(Resource.SHIELD, 3);
            put(Resource.FAITH, 1);
        }}), 31);
        this.table.turnOf().getDevSlots()[1].addCard(devCard2);

        //giving some Resources
        LeaderCard leaderCard = new LeaderCard(1, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
        }}, new EnumMap<>(Resource.class),15);
        leaderCard.play();
        leaderCard.getAbility().add(Resource.COIN);
        leaderCard.getAbility().add(Resource.COIN);
        leaderCard.getAbility().add(Resource.SERVANT);
        this.table.turnOf().addLeaderCard(leaderCard);

        this.table.turnOf().getShelves()[0].singleAdd(Resource.SHIELD);
        this.table.turnOf().getShelves()[1].singleAdd(Resource.STONE);

        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 1);
        }});

        //2 coin, 1 stone, 1 servant

        this.productionController.selectionFromLeaderStorage(Resource.COIN, 15, 1);
        this.productionController.selectCardProduction(30);
        this.productionController.selectionFromStrongBox(Resource.COIN, 1);
        this.productionController.selectionFromShelf(Resource.STONE, 2);
        this.productionController.selectCardProduction(31);
        this.productionController.selectionFromLeaderStorage(Resource.SERVANT, 15, 1);

        //ottengo anche una risorsa faith
        int position = this.table.turnOf().getPosition();

        //visto che non ci sono any la transazione inizia e finisce qui
        this.productionController.activateProduction();
        EnumMap<Resource, Integer> expectedStrongBox = new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 2);
            put(Resource.SHIELD, 3);
        }};

        assertEquals(position+1, this.table.turnOf().getPosition());
        assertEquals(expectedStrongBox, this.table.turnOf().getStrongBox().content());

        assertFalse(this.table.turnOf().getDevSlots()[0].topCard().isSelected());
        assertFalse(this.table.turnOf().getDevSlots()[1].topCard().isSelected());

        assertTrue(this.table.turnOf().getShelves()[1].isEmpty());

        assertEquals(MacroTurnType.DONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("activateProduction (with any as output) method test")
    public void activateProductionTest2(){
        //do al giocatore una leadercard production e una dev card
        DevCard devCard = new DevCard(2, new EnumMap<>(Resource.class), new DevCardType(1, Colour.PURPLE), new ProductionPower(new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.STONE,1);
        }}, new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.FAITH, 1);
        }}), 30);
        this.table.turnOf().getDevSlots()[0].addCard(devCard);

        LeaderCard leaderCard1 = new LeaderCard(2, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.PRODPOWER, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 1);
            put(Resource.SERVANT,1);
        }}, new EnumMap<>(Resource.class),20);
        leaderCard1.play();
        this.table.turnOf().addLeaderCard(leaderCard1);

        //giving some Resources

        LeaderCard leaderCard2 = new LeaderCard(1, new EnumMap<>(Resource.class), new HashMap<>(), LeaderCardType.STORAGE, new EnumMap<>(Resource.class){{
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
        }}, new EnumMap<>(Resource.class),15);
        leaderCard2.play();
        leaderCard2.getAbility().add(Resource.COIN);
        leaderCard2.getAbility().add(Resource.COIN);
        leaderCard2.getAbility().add(Resource.SERVANT);
        this.table.turnOf().addLeaderCard(leaderCard2);

        this.table.turnOf().getShelves()[0].singleAdd(Resource.SHIELD);
        this.table.turnOf().getShelves()[1].singleAdd(Resource.STONE);

        this.table.turnOf().getStrongBox().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.STONE, 1);
            put(Resource.COIN, 1);
        }});

        //need selected 2 coin, 1 stone, 1 servant
        this.productionController.selectCardProduction(20);
        this.productionController.selectionFromShelf(Resource.STONE, 2);
        this.productionController.selectCardProduction(30);
        this.productionController.selectionFromLeaderStorage(Resource.COIN, 15, 1);
        this.productionController.selectionFromLeaderStorage(Resource.SERVANT, 15, 1);
        this.productionController.selectionFromStrongBox(Resource.COIN, 1);

        //visto che ci sono any la transazione inizia e non finisce qui
        this.productionController.activateProduction();



        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.ANY_DECISION, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("anySelection method test")
    public void anySelectionTest(){
        this.activateProductionTest2();
        assertFalse(this.table.turnOf().getSupportContainer().isEmpty());

        //ho una any in support container -> vado a selezionare una risorsa

        //risorsa non corretta -> nothing changes
        this.productionController.anySelection(Resource.WHITE);
        assertEquals(new EnumMap<>(Resource.class){{put(Resource.ANY, 1);}}, this.table.turnOf().getSupportContainer().content());

        //risorsa corretta -> the transaction starts
        this.productionController.anySelection(Resource.SHIELD);
        EnumMap<Resource, Integer> expectedStrongBox = new EnumMap<>(Resource.class){{
            put(Resource.SERVANT, 2);
            put(Resource.STONE, 1);
            put(Resource.SHIELD, 1);
        }};

        assertEquals(expectedStrongBox, this.table.turnOf().getStrongBox().content());

        assertEquals(MacroTurnType.DONE, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.NONE, this.table.turnOf().getMicroTurnType());
    }

    @Test
    @DisplayName("backFromAnySelection method test")
    public void backFromAnySelectionTest(){
        //turn type not valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
        this.table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);

        //nothing changes
        this.productionController.backFromAnySelection();
        assertEquals(MacroTurnType.BUY_NEW_CARD, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());

        //turn type valid
        this.table.turnOf().setMacroTurnType(MacroTurnType.PRODUCTION);
        this.table.turnOf().setMicroTurnType(MicroTurnType.ANY_DECISION);
        this.table.turnOf().getSupportContainer().addEnumMap(new EnumMap<>(Resource.class){{
            put(Resource.ANY, 3);
            put(Resource.COIN, 1);
        }});

        this.productionController.backFromAnySelection();
        assertTrue(this.table.turnOf().getSupportContainer().isEmpty());
        assertEquals(MacroTurnType.PRODUCTION, this.table.turnOf().getMacroTurnType());
        assertEquals(MicroTurnType.SETTING_UP, this.table.turnOf().getMicroTurnType());
    }

}
