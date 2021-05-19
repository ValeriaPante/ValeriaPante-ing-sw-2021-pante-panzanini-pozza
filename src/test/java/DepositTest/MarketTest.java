package DepositTest;

import it.polimi.ingsw.Deposit.Market;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    private Market market;

    @Test
    @DisplayName("creation test")
    public void testC(){
        market = new Market();
        Resource[][] marketMatrix = market.getState();
        EnumMap<Resource, Integer> marketContent = new EnumMap<>(Resource.class);
        for(int i=0; i<3; i++)
            for (int j=0; j<4; j++)
                marketContent.put(marketMatrix[i][j], 1 + (marketContent.get(marketMatrix[i][j]) == null ? 0 : marketContent.get(marketMatrix[i][j]) ) );

        marketContent.put(market.getSlide(), 1+ (marketContent.get(market.getSlide()) == null ? 0 : marketContent.get(market.getSlide())));

        assertEquals(marketContent, new EnumMap<Resource, Integer>(Resource.class) {{
            put(Resource.WHITE, 4);
            put(Resource.SHIELD, 2);
            put(Resource.STONE, 2);
            put(Resource.COIN, 2);
            put(Resource.SERVANT, 2);
            put(Resource.FAITH, 1);
        }});

        assertFalse(this.market.areThereSelections());
        assertFalse(this.market.isRowSelected());
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.takeSelection());

        //no rep exposition
        if (marketMatrix[1][1].equals(Resource.COIN)){
            marketMatrix[1][1] = Resource.STONE;
            marketMatrix = market.getState();
            assertNotEquals(Resource.STONE, marketMatrix[1][1]);
            assertEquals(Resource.COIN, marketMatrix[1][1]);
        }
        else{
            Resource oldType = marketMatrix[1][1];
            marketMatrix[1][1] = Resource.COIN;
            marketMatrix = market.getState();
            assertNotEquals(Resource.COIN, marketMatrix[1][1]);
            assertEquals(oldType, marketMatrix[1][1]);
        }

        //no spontaneous change
        for (int k=0; k<10; k++){
            Resource[][] marketMatrixCopy = market.getState();
            for(int i=0; i<3; i++)
                for (int j=0; j<4; j++)
                    assertEquals(marketMatrixCopy[i][j], marketMatrix[i][j]);
        }
    }

    @Test
    @DisplayName("selecting test")
    public void testS(){
        market = new Market();
        assertFalse(this.market.areThereSelections());
        assertFalse(this.market.isRowSelected());
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.takeSelection());
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.getPosSelected());
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.selectColumn(10));
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.selectColumn(4));
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.selectRow(3));
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.selectRow(8));

        market.selectRow(2);

        assertTrue(this.market.areThereSelections());
        assertTrue(this.market.isRowSelected());
        assertEquals(this.market.getPosSelected(), 2);

        market.selectRow(1);

        assertTrue(this.market.areThereSelections());
        assertTrue(this.market.isRowSelected());
        assertEquals(this.market.getPosSelected(), 1);

        market.selectColumn(3);

        assertTrue(this.market.areThereSelections());
        assertFalse(this.market.isRowSelected());
        assertEquals(this.market.getPosSelected(), 3);

        market.deleteSelection();

        assertFalse(this.market.areThereSelections());
        assertFalse(this.market.isRowSelected());
        assertThrows(IndexOutOfBoundsException.class, () -> this.market.getPosSelected());

        Integer tryExternalChange = 2;
        market.selectColumn(tryExternalChange);
        tryExternalChange = 1;

        assertTrue(this.market.areThereSelections());
        assertFalse(this.market.isRowSelected());
        assertEquals(this.market.getPosSelected(), 2);
    }

    @Test
    @DisplayName("picking test")
    public void testP(){
//        market = new Market();
//        Resource[][] marketMatrix = market.getState();
//        Resource oldSlide = market.getSlide();
//        market.selectColumn(0);
//
//        assertTrue(this.market.areThereSelections());
//        assertFalse(this.market.isRowSelected());
//        assertEquals(this.market.getPosSelected(), 0);
//
//        EnumMap<Resource, Integer> columnContent = new EnumMap<>(Resource.class);
//        for (int i=0; i<3; i++)
//            columnContent.put(marketMatrix[i][0], 1 + (columnContent.get(marketMatrix[i][0]) == null ? 0 : 1));
//
//        assertEquals(market.takeSelection(), columnContent);
//        Resource newSlide = market.getSlide();
//        Resource[][] newMarketMatrix = market.getState();
//        for (int i=0; i<3; i++) {
//            for (int j = 0; j < 4; j++) {
//                if (j == 0) {
//                    if (i < 2)
//                        assertEquals(newMarketMatrix[i][j], marketMatrix[i + 1][j]);
//                    else
//                        assertEquals(newMarketMatrix[i][j], oldSlide);
//                } else {
//                    assertEquals(newMarketMatrix[i][j], marketMatrix[i][j]);
//                }
//            }
//        }
//        assertEquals(newSlide, marketMatrix[0][0]);
//
//        EnumMap<Resource, Integer> marketContent = new EnumMap<>(Resource.class);
//        for(int i=0; i<3; i++)
//            for (int j=0; j<4; j++)
//                marketContent.put(newMarketMatrix[i][j], 1 + (marketContent.get(newMarketMatrix[i][j]) == null ? 0 : marketContent.get(newMarketMatrix[i][j]) ) );
//
//        marketContent.put(market.getSlide(), 1+ (marketContent.get(market.getSlide()) == null ? 0 : marketContent.get(market.getSlide())));
//
//        assertEquals(marketContent, new EnumMap<Resource, Integer>(Resource.class) {{
//            put(Resource.WHITE, 4);
//            put(Resource.SHIELD, 2);
//            put(Resource.STONE, 2);
//            put(Resource.COIN, 2);
//            put(Resource.SERVANT, 2);
//            put(Resource.FAITH, 1);
//        }});
//
//        market.selectRow(1);
//
//        assertTrue(this.market.areThereSelections());
//        assertTrue(this.market.isRowSelected());
//        assertEquals(this.market.getPosSelected(), 1);
//
//        market.deleteSelection();
//
//        assertFalse(this.market.areThereSelections());
//        assertFalse(this.market.isRowSelected());
//
//        market.selectRow(1);
//        marketMatrix = newMarketMatrix;
//        oldSlide = newSlide;
//        EnumMap<Resource, Integer> rowContent = new EnumMap<>(Resource.class);
//        for (int i=0; i<4; i++)
//            rowContent.put(marketMatrix[1][i], 1 + (rowContent.get(marketMatrix[1][i]) == null ? 0 : 1));
//        assertEquals(market.takeSelection(), rowContent);
//        newMarketMatrix = market.getState();
//        newSlide = market.getSlide();
//        for (int i=0; i<3; i++) {
//            for (int j = 0; j < 4; j++) {
//                if (i == 1) {
//                    if (j < 3)
//                        assertEquals(newMarketMatrix[i][j], marketMatrix[i][j + 1]);
//                    else
//                        assertEquals(newMarketMatrix[i][j], oldSlide);
//                } else {
//                    assertEquals(newMarketMatrix[i][j], marketMatrix[i][j]);
//                }
//            }
//        }
//        assertEquals(newSlide, marketMatrix[1][0]);
//
//        marketContent.clear();
//        for(int i=0; i<3; i++)
//            for (int j=0; j<4; j++)
//                marketContent.put(newMarketMatrix[i][j], 1 + (marketContent.get(newMarketMatrix[i][j]) == null ? 0 : marketContent.get(newMarketMatrix[i][j]) ) );
//
//        marketContent.put(market.getSlide(), 1+ (marketContent.get(market.getSlide()) == null ? 0 : marketContent.get(market.getSlide())));
//
//        assertEquals(marketContent, new EnumMap<Resource, Integer>(Resource.class) {{
//            put(Resource.WHITE, 4);
//            put(Resource.SHIELD, 2);
//            put(Resource.STONE, 2);
//            put(Resource.COIN, 2);
//            put(Resource.SERVANT, 2);
//            put(Resource.FAITH, 1);
//        }});
    }
}
