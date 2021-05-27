package it.polimi.ingsw.Model.FaithTrack;

//rappresenta la zona rapporto in vaticano sul percorso fede
//questa ha una posizione di inizio posStart
//ha una posizione in cui sta il simbolo del Papa posPope
//un id che mi rappresenta seè il primo/secondo/terzo
//visto che il rapporto viene attivato una volta sola il boolean alreadyDone mi rappresenta questo
public class VaticanRelation implements Cloneable{
    private final int id;
    private final int posStart;
    private final int posPope;
    private boolean alreadyDone;

    public VaticanRelation(int start, int posPope, int id){
        this.id = id;
        this.posStart = start;
        this.posPope = posPope;
        this.alreadyDone = false;
    }

    //getter id
    public int getId(){
        return id;
    }


    public boolean isInOrOver(int pos){
        return pos>=posStart;
    }

    //chiamo questo metodo per capire se qualcuno è sopra o ha superato la posPope ->
    //faccio partire il rapporto in vaticano
    public boolean isOnPopePositionOrOver(int pos){
        return pos>= posPope;
    }

    //getter
    public boolean isAlreadyDone(){
        return alreadyDone;
    }

    //quando finisci il rapporto in vaticano modifichi lo stato
    //-> la prossima volta questo non lo consideri
    public void done(){
        this.alreadyDone = true;
    }

    public VaticanRelation clone(){
        VaticanRelation clone = new VaticanRelation(this.posStart, this.posPope, this.id);
        if (this.alreadyDone){
            clone.alreadyDone = true;
        }
        return clone;
    }
}
