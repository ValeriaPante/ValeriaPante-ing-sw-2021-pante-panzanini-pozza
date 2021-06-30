package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

/**
 * This class is used to represent the market of the game:
 * the structure and the business logic connected to randomization of the creation of the initial grill
 * (The randomization is "total": there are no such things as seeds involved)
 * The representation with slides is the same of the market in the game:
 * row and column zero are considered to be close to the market and the marble will be inserted from
 * "the other side" of the market (last column or last row)
 * The representation is directly through resources and not marbles
 */
public class Market {
    private final Resource[][] grid;   //first position row, second position column: [row][column]
    private Resource slide;
    private int posSelected;        //if ==-1 not initialized, interval [-1, 3];
    private int isRowSelected;      //if ==1 selected row, ==0 column, ==-1 no selections

    /**
     * Selects the column specified in the market
     * @param columnSelected a number ranging from 0 to 3 corresponding to the column to be selected
     * @throws IndexOutOfBoundsException if parameter "columnSelected" is not inside the right range (0-3)
     */
    public synchronized void selectColumn(int columnSelected) throws IndexOutOfBoundsException{
        if (columnSelected > 3)
            throw new IndexOutOfBoundsException();

        isRowSelected = 0;
        posSelected = columnSelected;
    }

    /**
     * Takes the marbles from the column specified
     * @param chosenColumn target column (is considered to be ranging from 0 to 3)
     * @return the resources corresponding to the marbles in the column picked
     */
    private synchronized EnumMap<Resource, Integer> pickColumn(int chosenColumn) {
        EnumMap<Resource, Integer> returningMap = new EnumMap<>(Resource.class);
        for (int i=0; i<3; i++)
            returningMap.put(grid[i][chosenColumn], 1 + ( (returningMap.get(grid[i][chosenColumn]) == null) ? 0 : returningMap.get(grid[i][chosenColumn]) ));
        this.shiftColumn(chosenColumn);
        posSelected = -1;
        isRowSelected = -1;
        return returningMap;
    }

    /**
     * Changes the market inserting the marble from the slide in the specified column
     * @param column target column
     */
    private synchronized void shiftColumn(int column) {
        Resource support;
        support = slide;
        slide = grid[0][column];
        grid[0][column] = grid[1][column];
        grid[1][column] = grid[2][column];
        grid[2][column] = support;
    }

    /**
     * Selects the row specified in the market
     * @param rowSelected a number ranging from 0 to 2 corresponding to the row to be selected
     * @throws IndexOutOfBoundsException if parameter "rowSelected" is not inside the right range (0-2)
     */
    public synchronized void selectRow(int rowSelected) throws IndexOutOfBoundsException{
        if (rowSelected > 2)
            throw new IndexOutOfBoundsException();

        isRowSelected = 1;
        posSelected = rowSelected;
    }

    /**
     * Takes the marbles from the row specified
     * @param chosenRow target row (is considered to be ranging from 0 to 2)
     * @return the resources corresponding to the marbles in the row picked
     */
    private synchronized EnumMap<Resource, Integer> pickRow(int chosenRow) {
        EnumMap<Resource, Integer> returningMap = new EnumMap<>(Resource.class);
        for (int i=0; i<4; i++)
            returningMap.put(grid[chosenRow][i], 1 + ((returningMap.get(grid[chosenRow][i]) == null) ? 0 : returningMap.get(grid[chosenRow][i])) );
        this.shiftRow(chosenRow);
        posSelected = -1;
        isRowSelected = -1;
        return returningMap;
    }

    /**
     * Changes the market inserting the marble from the slide in the specified row
     * @param row target column (is considered to be ranging from 0 to 2)
     */
    private synchronized void shiftRow(int row) {
        Resource support;
        support = slide;
        slide = grid[row][0];
        grid[row][0] = grid[row][1];
        grid[row][1] = grid[row][2];
        grid[row][2] = grid[row][3];
        grid[row][3] = support;
    }

    /**
     * getter
     * @return the current state of the grill of resources represented by the marbles
     */
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

    /**
     * Interacts with the market inserting the "marble" from the slide into the grill
     * @return the content of the row/column selected in the market
     * @throws IndexOutOfBoundsException if there was no previous selection
     */
    public synchronized EnumMap<Resource, Integer> takeSelection() throws IndexOutOfBoundsException{
        if (isRowSelected == -1)
            throw new IndexOutOfBoundsException(); //there was no previous selection!

        EnumMap<Resource, Integer> resourcesSelected;
        if (isRowSelected == 1)
            resourcesSelected = this.pickRow(posSelected);
        else
            resourcesSelected = this.pickColumn(posSelected);
        posSelected = -1;
        isRowSelected = -1;
        return resourcesSelected;
    }

    public synchronized Resource getSlide(){
        return slide;
    }

    /**
     * Clears all possibles selections in the market
     */
    public synchronized void deleteSelection(){
        isRowSelected = -1;
        posSelected = -1;
    }

    /**
     *
     * @return true if a row is selected, false otherwise
     * (a column is selected or nothing is selected: the selection concept is not binary)
     * This method is mostly used combined with the method "areThereAnySelections"
     */
    public synchronized boolean isRowSelected(){
        return isRowSelected == 1;
    }

    /**
     *
     * @return true if there are selections, false otherwise
     */
    public synchronized  boolean areThereSelections(){
        return isRowSelected != -1;
    }

    /**
     * The position selected is the integer representing the row or the column selected in the market
     * @return the position selected
     * @throws IndexOutOfBoundsException if nothing was selected
     */
    public synchronized int getPosSelected() throws IndexOutOfBoundsException{
        if (posSelected == -1)
            throw new IndexOutOfBoundsException(); //there was no selection!

        return posSelected;
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
        isRowSelected = -1;
        posSelected = -1;
    }
}
