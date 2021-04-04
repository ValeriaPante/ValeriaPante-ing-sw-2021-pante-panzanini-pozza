package it.polimi.ingsw.FaithTrack;

public class FaithTrack {
    private static FaithTrack instance;

    public static final int length = 20;
    private SmallPath[] smallPaths;
    private VaticanRelation[] vaticanRelations;

    //chiedo alla faith track quanti punti sono associati ad una determinata posizione
    public int victoryPoints(int pos){
        for (SmallPath elem:smallPaths){
            if (elem.isIn(pos)){
                return elem.getVictoryPoints();
            }
        }
        return 20;
    }

    //singletone
    public static FaithTrack getInstance(){
        if (instance == null){
            instance = new FaithTrack();
        }
        return instance;
    }

    //mi ritorna il rapporto in vaticano se sulla posizione che ho inserito
    //devo far partire un rapporto in vaticano altrimenti null
    public VaticanRelation popeRelation(int pos){
        for (VaticanRelation elem: vaticanRelations){
            if (elem.isOver(pos) && !elem.isAlreadyDone()){
                return elem;
            }
        }
        return null;
    }

    //ritorna true se la posizione supera la lunghezza del percorso
    public boolean finished(int pos){
        return pos >= length;
    }

    //costruttore privato per signletone
    //la faith track è composta da zone rapporto in vaticano e
    //9 percorsi
    private FaithTrack(){
        //creo le 3 zone rapporto in vaticano
        vaticanRelations = new VaticanRelation[]{
                new VaticanRelation(5,8,0),
                new VaticanRelation(12,16,1),
                new VaticanRelation(19,24,2),
        };

        //creo 9 le zone SmallPath
        smallPaths = new SmallPath[]{
                new SmallPath(0, 2, 0),
                new SmallPath(3, 5, 1),
                new SmallPath(6, 8, 2),
                new SmallPath(9, 11, 4),
                new SmallPath(12, 14, 6),
                new SmallPath(15, 17, 9),
                new SmallPath(18, 20, 12),
                new SmallPath(21, 23, 16),
                new SmallPath(24, 24, 20),
        };
    }
}
