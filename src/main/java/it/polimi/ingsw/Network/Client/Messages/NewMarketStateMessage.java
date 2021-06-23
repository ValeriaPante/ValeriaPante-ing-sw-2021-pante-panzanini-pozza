package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

public class NewMarketStateMessage extends FromServerMessage{

    private String type = "newMarketState";
    private final Resource[][] grid;
    private Resource slide;

    public NewMarketStateMessage(Resource[][] grid, Resource slide) {
        this.grid = grid;
        this.slide = slide;
    }

    public Resource[][] getGrid() {
        return grid;
    }

    public Resource getSlide() {
        return slide;
    }
    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
