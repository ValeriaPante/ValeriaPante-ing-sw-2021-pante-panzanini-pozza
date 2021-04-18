package DepositTest;

import it.polimi.ingsw.Deposit.Market;
import it.polimi.ingsw.Enums.Resource;
import org.junit.jupiter.api.BeforeEach;
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

        EnumMap<Resource, Integer> marketContent = new EnumMap<>(Resource.class);

    }
}
