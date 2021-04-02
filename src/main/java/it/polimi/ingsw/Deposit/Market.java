package it.polimi.ingsw.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

public class Market {
    private final Resource[][] grid;   //first position row, second position column: [row][column]
    private Resource slide;

    public synchronized EnumMap<Resource, Integer> pickColumn(int chosenColumn) {
        EnumMap<Resource, Integer> returningMap = new EnumMap<>(Resource.class);
        for (int i=0; i<3; i++)
            returningMap.put(grid[i][chosenColumn], 1 + ( (returningMap.get(grid[i][chosenColumn]) == null) ? 0 : returningMap.get(grid[i][chosenColumn]) ));
        this.shiftColumn(chosenColumn);
        return returningMap;
    }

    private void shiftColumn(int column) {
        Resource support;
        support = slide;
        slide = grid[0][column];
        grid[0][column] = grid[1][column];
        grid[1][column] = grid[2][column];
        grid[2][column] = support;
    }

    public synchronized EnumMap<Resource, Integer> pickRow(int chosenRow) {
        EnumMap<Resource, Integer> returningMap = new EnumMap<>(Resource.class);
        for (int i=0; i<4; i++)
            returningMap.put(grid[chosenRow][i], 1 + ((returningMap.get(grid[chosenRow][i]) == null) ? 0 : returningMap.get(grid[chosenRow][i])) );
        this.shiftRow(chosenRow);
        return returningMap;
    }

    private void shiftRow(int row) {
        Resource support;
        support = slide;
        slide = grid[row][0];
        grid[row][0] = grid[row][1];
        grid[row][1] = grid[row][2];
        grid[row][2] = grid[row][3];
        grid[row][3] = support;
    }

    public synchronized Resource[][] getState(){
        Resource[][] gridCopy = new Resource[3][4];
        //cloning by row
        for (int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                gridCopy[i][j] = grid[i][j];
            }
        }
        return gridCopy;
    }

    public Market() {
        grid = new Resource[3][4];
        ArrayList<Resource> pickingList = new ArrayList<>();
        int i, j;

        for (i=0; i<2; i++) pickingList.add(Resource.COIN);
        pickingList.add(Resource.FAITH);
        for (i=0; i<2; i++) pickingList.add(Resource.SERVANT);
        for (i=0; i<2; i++) pickingList.add(Resource.SHIELD);
        for (i=0; i<2; i++) pickingList.add(Resource.STONE);
        for (i=0; i<4; i++) pickingList.add(Resource.WHITE);

        Collections.shuffle(pickingList);

        //initialization by row
        for (i=0; i<3; i++){
            for (j=0; j<4; j++){
                grid[i][j] = pickingList.remove(pickingList.size()-1);               //pickingList.remove returns the removed object
            }
        }
        slide = pickingList.remove(0);
    }
}
