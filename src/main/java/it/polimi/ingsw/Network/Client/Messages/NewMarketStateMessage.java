package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Network.Client.Visitor;

public class NewMarketStateMessage extends FromServerMessage{

    private final Resource[][] grid;
    private Resource slide;

    public NewMarketStateMessage(Resource[] grid, Resource slide) {
        this.grid = new Resource[3][4];

        for(int i = 0; i < grid.length; i++)
            this.grid[i/4][i%4] = grid[i];

        this.slide = slide;
    }

    public Resource[][] getGrid() {
        return grid;
    }

    public Resource getSlide() {
        return slide;
    }
    @Override
    public void visit(Visitor v){
        v.updateModel(this);
    }
}
