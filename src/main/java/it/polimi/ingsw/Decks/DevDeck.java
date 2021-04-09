package it.polimi.ingsw.Decks;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Enums.Colour;
import it.polimi.ingsw.Abilities.ProductionPower.ProductionPower;

import java.util.*;

public class DevDeck implements Deck{
    private List<DevCard> deck;
    private final DevCardType type;

    public DevDeck(DevCardType t) throws IllegalArgumentException{
        deck = new ArrayList<>();
        type = new DevCardType(t.getLevel(), t.getColor());

        EnumMap<Resource, Integer> cost = new EnumMap<>(Resource.class);

        if(t.getLevel() == 1 && t.getColor() == Colour.GREEN){
            //1
            cost.put(Resource.SHIELD, 2);
            deck.add(new DevCard(1, cost, t, new ProductionPower(
                        new Resource[]  {Resource.COIN},
                        new int[]       {1},
                        new Resource[]  {Resource.FAITH},
                        new int[]       {1})));

            //5
            cost.clear();
            cost.put(Resource.SHIELD, 1);
            cost.put(Resource.SERVANT, 1);
            cost.put(Resource.STONE, 1);
            deck.add(new DevCard(2, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1})));

            //9
            cost.clear();
            cost.put(Resource.SHIELD, 3);
            deck.add(new DevCard(3, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {2},
                    new Resource[]  {Resource.COIN, Resource.SHIELD, Resource.STONE},
                    new int[]       {1, 1, 1})));

            //13
            cost.clear();
            cost.put(Resource.SHIELD, 2);
            cost.put(Resource.COIN, 2);
            deck.add(new DevCard(4, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.COIN, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 2 && t.getColor() == Colour.GREEN){
            //17
            cost.put(Resource.SHIELD, 4);
            deck.add(new DevCard(5, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {2})));

            //21
            cost.put(Resource.SHIELD, 3);
            cost.put(Resource.SERVANT, 2);
            deck.add(new DevCard(6, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.STONE},
                    new int[]       {3})));

            //25
            cost.put(Resource.SHIELD, 5);
            deck.add(new DevCard(7, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {2},
                    new Resource[]  {Resource.STONE, Resource.FAITH},
                    new int[]       {2, 2})));

            //29
            cost.put(Resource.SHIELD, 3);
            cost.put(Resource.COIN, 3);
            deck.add(new DevCard(8, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {1},
                    new Resource[]  {Resource.SHIELD, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 3 && t.getColor() == Colour.GREEN){
            //33
            cost.put(Resource.SHIELD, 6);
            deck.add(new DevCard(9, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {2},
                    new Resource[]  {Resource.STONE, Resource.FAITH},
                    new int[]       {3, 2})));

            //37
            cost.put(Resource.SHIELD, 5);
            cost.put(Resource.SERVANT, 2);
            deck.add(new DevCard(10, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SHIELD, Resource.STONE, Resource.FAITH},
                    new int[]       {2, 2, 1})));

            //41
            cost.put(Resource.SHIELD, 7);
            deck.add(new DevCard(11, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.COIN, Resource.FAITH},
                    new int[]       {1, 3})));

            //45
            cost.put(Resource.SHIELD, 4);
            cost.put(Resource.COIN, 4);
            deck.add(new DevCard(12, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.COIN, Resource.SHIELD},
                    new int[]       {3, 1})));

        }
        else if (t.getLevel() == 1 && t.getColor() == Colour.YELLOW){
            //4
            cost.put(Resource.STONE, 2);
            deck.add(new DevCard(1, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {1})));

            //8
            cost.clear();
            cost.put(Resource.SHIELD, 1);
            cost.put(Resource.STONE, 1);
            cost.put(Resource.COIN, 1);
            deck.add(new DevCard(2, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.COIN},
                    new int[]       {1})));

            //12
            cost.clear();
            cost.put(Resource.STONE, 3);
            deck.add(new DevCard(3, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {2},
                    new Resource[]  {Resource.COIN, Resource.SERVANT, Resource.STONE},
                    new int[]       {1, 1, 1})));

            //16
            cost.clear();
            cost.put(Resource.SHIELD, 2);
            cost.put(Resource.STONE, 2);
            deck.add(new DevCard(4, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SHIELD, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 2 && t.getColor() == Colour.YELLOW){
            //20
            cost.put(Resource.STONE, 4);
            deck.add(new DevCard(5, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {2})));

            //24
            cost.put(Resource.STONE, 3);
            cost.put(Resource.SHIELD, 2);
            deck.add(new DevCard(6, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE, Resource.SHIELD},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.COIN},
                    new int[]       {3})));

            //28
            cost.put(Resource.STONE, 5);
            deck.add(new DevCard(7, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {2},
                    new Resource[]  {Resource.SERVANT, Resource.FAITH},
                    new int[]       {2, 2})));

            //32
            cost.put(Resource.STONE, 3);
            cost.put(Resource.SERVANT, 3);
            deck.add(new DevCard(8, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.COIN, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 3 && t.getColor() == Colour.YELLOW){
            //36
            cost.put(Resource.STONE, 6);
            deck.add(new DevCard(9, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {2},
                    new Resource[]  {Resource.SERVANT, Resource.FAITH},
                    new int[]       {3, 2})));

            //40
            cost.put(Resource.STONE, 5);
            cost.put(Resource.SERVANT, 2);
            deck.add(new DevCard(10, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.COIN, Resource.SHIELD, Resource.FAITH},
                    new int[]       {2, 2, 1})));

            //44
            cost.put(Resource.STONE, 7);
            deck.add(new DevCard(11, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.SERVANT, Resource.FAITH},
                    new int[]       {1, 3})));

            //48
            cost.put(Resource.STONE, 4);
            cost.put(Resource.SERVANT, 4);
            deck.add(new DevCard(12, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.SERVANT, Resource.STONE},
                    new int[]       {3, 1})));

        }
        else if (t.getLevel() == 1 && t.getColor() == Colour.BLUE){
            //3
            cost.put(Resource.COIN, 2);
            deck.add(new DevCard(1, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {1})));

            //7
            cost.clear();
            cost.put(Resource.COIN, 1);
            cost.put(Resource.SERVANT, 1);
            cost.put(Resource.STONE, 1);
            deck.add(new DevCard(2, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.STONE},
                    new int[]       {1})));

            //11
            cost.clear();
            cost.put(Resource.COIN, 3);
            deck.add(new DevCard(3, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {2},
                    new Resource[]  {Resource.COIN, Resource.SERVANT, Resource.SHIELD},
                    new int[]       {1, 1, 1})));

            //15
            cost.clear();
            cost.put(Resource.COIN, 2);
            cost.put(Resource.SERVANT, 2);
            deck.add(new DevCard(4, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SHIELD, Resource.STONE},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SERVANT, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 2 && t.getColor() == Colour.BLUE){
            //19
            cost.put(Resource.COIN, 4);
            deck.add(new DevCard(5, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {2})));

            //23
            cost.put(Resource.COIN, 3);
            cost.put(Resource.STONE, 2);
            deck.add(new DevCard(6, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.STONE},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {3})));

            //27
            cost.put(Resource.COIN, 5);
            deck.add(new DevCard(7, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {2},
                    new Resource[]  {Resource.SHIELD, Resource.FAITH},
                    new int[]       {2, 2})));

            //31
            cost.put(Resource.SHIELD, 3);
            cost.put(Resource.COIN, 3);
            deck.add(new DevCard(8, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.STONE, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 3 && t.getColor() == Colour.BLUE){
            //35
            cost.put(Resource.COIN, 6);
            deck.add(new DevCard(9, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {2},
                    new Resource[]  {Resource.SHIELD, Resource.FAITH},
                    new int[]       {3, 2})));

            //39
            cost.put(Resource.COIN, 5);
            cost.put(Resource.STONE, 2);
            deck.add(new DevCard(10, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.SHIELD},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SERVANT, Resource.STONE, Resource.FAITH},
                    new int[]       {2, 2, 1})));

            //43
            cost.put(Resource.COIN, 7);
            deck.add(new DevCard(11, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.SHIELD, Resource.FAITH},
                    new int[]       {1, 3})));

            //47
            cost.put(Resource.STONE, 4);
            cost.put(Resource.COIN, 4);
            deck.add(new DevCard(12, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.SHIELD, Resource.COIN},
                    new int[]       {3, 1})));

        }
        else if (t.getLevel() == 1 && t.getColor() == Colour.PURPLE){
            //2
            cost.put(Resource.SERVANT, 2);
            deck.add(new DevCard(1, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {1})));

            //6
            cost.clear();
            cost.put(Resource.SHIELD, 1);
            cost.put(Resource.SERVANT, 1);
            cost.put(Resource.COIN, 1);
            deck.add(new DevCard(2, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {1},
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {1})));

            //10
            cost.clear();
            cost.put(Resource.SERVANT, 3);
            deck.add(new DevCard(3, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {2},
                    new Resource[]  {Resource.SERVANT, Resource.SHIELD, Resource.STONE},
                    new int[]       {1, 1, 1})));

            //14
            cost.clear();
            cost.put(Resource.SERVANT, 2);
            cost.put(Resource.STONE, 2);
            deck.add(new DevCard(4, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.SHIELD},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.STONE, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 2 && t.getColor() == Colour.PURPLE){
            //18
            cost.put(Resource.SERVANT, 4);
            deck.add(new DevCard(5, cost, t, new ProductionPower(
                    new Resource[]  {Resource.SERVANT},
                    new int[]       {1},
                    new Resource[]  {Resource.FAITH},
                    new int[]       {2})));

            //22
            cost.put(Resource.SERVANT, 3);
            cost.put(Resource.COIN, 2);
            deck.add(new DevCard(6, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN, Resource.SERVANT},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.SHIELD},
                    new int[]       {3})));

            //26
            cost.put(Resource.SERVANT, 5);
            deck.add(new DevCard(7, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {2},
                    new Resource[]  {Resource.COIN, Resource.FAITH},
                    new int[]       {2, 2})));

            //30
            cost.put(Resource.SHIELD, 3);
            cost.put(Resource.SERVANT, 3);
            deck.add(new DevCard(8, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {1},
                    new Resource[]  {Resource.SERVANT, Resource.FAITH},
                    new int[]       {2, 1})));

        }
        else if (t.getLevel() == 3 && t.getColor() == Colour.PURPLE){
            //34
            cost.put(Resource.SERVANT, 6);
            deck.add(new DevCard(9, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE},
                    new int[]       {2},
                    new Resource[]  {Resource.COIN, Resource.FAITH},
                    new int[]       {3, 2})));

            //38
            cost.put(Resource.SERVANT, 5);
            cost.put(Resource.COIN, 2);
            deck.add(new DevCard(10, cost, t, new ProductionPower(
                    new Resource[]  {Resource.STONE, Resource.SHIELD},
                    new int[]       {1, 1},
                    new Resource[]  {Resource.COIN, Resource.SERVANT, Resource.FAITH},
                    new int[]       {2, 2, 1})));

            //42
            cost.put(Resource.SERVANT, 7);
            deck.add(new DevCard(11, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {1},
                    new Resource[]  {Resource.STONE, Resource.FAITH},
                    new int[]       {1, 3})));

            //46
            cost.put(Resource.SHIELD, 4);
            cost.put(Resource.SERVANT, 4);
            deck.add(new DevCard(12, cost, t, new ProductionPower(
                    new Resource[]  {Resource.COIN},
                    new int[]       {1},
                    new Resource[]  {Resource.STONE, Resource.SERVANT},
                    new int[]       {3, 1})));

        }
        else { throw new IllegalArgumentException(); }

        Collections.shuffle(deck);
    }

    public DevCardType getType(){
        return new DevCardType(type.getLevel(), type.getColor());
    }

    @Override
    public void shuffle() {
        Collections.shuffle(deck);
    }

    public DevCard draw() {
        return deck.remove(0);
    }

    public DevCard getTopCard(){
        return this.deck.get(0).clone();
    }

    public boolean isEmpty(){
        return this.deck.isEmpty();
    }

    public int size(){ return this.deck.size(); }
}
