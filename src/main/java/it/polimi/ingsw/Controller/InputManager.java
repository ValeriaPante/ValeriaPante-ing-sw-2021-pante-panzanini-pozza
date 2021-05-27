package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Abilities.ProductionPower.ProductionPower;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Model.Player.RealPlayer;

import java.util.EnumMap;
import java.util.LinkedList;

public class InputManager {

    private static InputManager instance;

    private Payable legendStorage(String symbol, RealPlayer player) throws IllegalArgumentException{
        switch (symbol){
            case("SB"):
                return player.getStrongBox();
            case("S1"):
                return player.getShelves()[0];
            case("S2"):
                return player.getShelves()[1];
            case("S3"):
                return player.getShelves()[2];
            case("L1"):
                //verifica blanda del fatto che abbia almeno una leadercard
                if (player.getLeaderCards().length>=1){
                    return player.getLeaderCards()[0].getAbility();
                }
                else throw new IllegalArgumentException("Non hai la prima leadercards");
            case("L2"):
                //verifica blanda del fatto che abbia almeno due leadercard
                if (player.getLeaderCards().length >= 2) {
                    return player.getLeaderCards()[1].getAbility();
                }
                else throw new IllegalArgumentException("Non hai la seconda leadercards");
            default:
                throw new IllegalArgumentException("Inserisci SB o S1 o S2 o S3 o L1 o L2");
        }
    }

    private Resource legendResource(String symbol) throws IllegalArgumentException{
        //da modificare
        switch (symbol){
            case ("CO"):
                return Resource.COIN;
            case ("SE"):
                return Resource.SERVANT;
            case ("SH"):
                return Resource.SHIELD;
            case ("ST"):
                return Resource.STONE;
            default:
                throw new IllegalArgumentException();
        }
    }

    //singletone
    public static InputManager getInstance(){
        if (instance == null){
            instance = new InputManager();
        }
        return instance;
    }

    private InputManager(){

    }

    public LinkedList<ProductionPower> selectProductionPowers(String selection, RealPlayer player) throws IllegalArgumentException{
        LinkedList<ProductionPower> allProductionPowers = player.getAllProductionPowers();

        selection = selection.trim();
        if (selection.isEmpty()){
            return allProductionPowers;
        }
        else if (!selection.matches("^[1-6]{1,6}$")){
            throw new IllegalArgumentException("Formato non corretto");
        }
        else{
            LinkedList<ProductionPower> selectedProductionPowers = new LinkedList<>();
            for (int i=0; i<allProductionPowers.size(); i++){
                if (selection.contains(String.valueOf(i+1))){
                    selectedProductionPowers.add(allProductionPowers.get(i));
                }
            }
            return selectedProductionPowers;
        }
    }

    public EnumMap<Resource, Integer> selectResources(String selection) throws IllegalArgumentException{
        selection = selection.trim();
        selection= selection.toUpperCase();
        if (!selection.matches("^(([1-9]\\d* (CO|SE|SH|ST))(, [1-9]\\d* (CO|SE|SH|ST))*)$")){
            throw new IllegalArgumentException();
        }
        else{
            EnumMap<Resource, Integer> result = new EnumMap<>(Resource.class);
            int quantity;
            Resource resource;
            int i=0;
            int j=-4;
            do{
                i = j+4;
                j=i+1;
                for (; Character.isDigit(selection.charAt(j)); j++);
                quantity = Integer.parseInt(selection.substring(i,j));
                //ora so che è su uno spazio

                //-> i prossimi due caratteri identificheranno il tipo di risorsa
                j++;
                resource = legendResource(selection.substring(j, j+2));

                result.put(resource, result.get(resource) == null ? quantity : result.get(resource) + quantity);

                //se j+2 == alla lunghezza della stringa -> ho finito
                //altrimenti si trova su una virgola e ricomincia,
            }while(j+2<selection.length());

            return result;
        }
    }

    //^(([-](L[1-2]|SB|S[1-3]))|(L[1-2]|SB|S[1-3])((: -?[1-9]\d* (CO|SE|SH|ST))(, -?[1-9]\d* (CO|SE|SH|ST))*))$
    public SelectResourceOutput selectResourcesInStorages(String string, RealPlayer player) throws IllegalArgumentException{
        string = string.trim();
        string = string.toUpperCase();
        if(!string.matches("^(([-](L[1-2]|SB|S[1-3]))|(L[1-2]|SB|S[1-3])((: -?[1-9]\\d* (CO|SE|SH|ST))(, -?[1-9]\\d* (CO|SE|SH|ST))*))$")){
            throw new IllegalArgumentException();
        }
        else{
            EnumMap<Resource, Integer> toAdd = new EnumMap<>(Resource.class);
            EnumMap<Resource, Integer> toRemove = new EnumMap<>(Resource.class);
            Payable deposit;

            if (string.charAt(0) == '-'){
                return new SelectResourceOutput(legendStorage(string.substring(1), player), toAdd, toRemove);
            }
            else{
                //ora inizia la parte sbatti ma nemmeno troppo

                //inizialmente so che i primi due caratteri sono il tipo di deposito
                //QUESTA CHIAMATA POTREBBE GENERARE ECCEZIONE ---------------------------------------------------------
                deposit = legendStorage(string.substring(0,2), player);
                //poi ho due punti e spazio
                //il quinto carattere so che è un numero oppure un '-'
                //ma non so quanti numeri lo seguono
                int i;
                int j=2;
                int quantity;
                Resource resource;
                EnumMap<Resource, Integer> resourceMap;
                boolean remove;
                do{
                    remove = false;
                    i=j+2;
                    //System.out.println("i:" +i+"-----"); //DEBUG
                    //il quinto carattere so che è un numero oppure un '-'
                    //ma non so quanti numeri lo seguono
                    if (string.charAt(i) == '-'){
                        i+=1;
                        remove = true;
                    }

                    j=i+1;
                    //in sostanza cerco il terminatore
                    for (; j<string.length() && string.charAt(j)!=','; j++){}
                    //resourceMap avrà sempre e solo un elemento
                    resourceMap = this.selectResources(string.substring(i,j));
                    //funziona
                    resource = resourceMap.keySet().iterator().next();
                    quantity = resourceMap.get(resource);

                    //in base a remove so se questa risorsa la voglio levare o tenere
                    if (remove){

                        toRemove.put(resource, toRemove.get(resource) == null ? quantity : toRemove.get(resource) + quantity);
                    }
                    else{
                        toAdd.put(resource, toAdd.get(resource) == null ? quantity : toAdd.get(resource) + quantity);
                    }
                    //System.out.println(j); //DEBUG
                    //se j == alla lunghezza della stringa -> ho finito
                    //altrimenti si trova su una virgola e ricomincia,
                }while(j<string.length());

                //arrivato a questo punto ho le mie due belle mappe
                //devo capire se ci sono duplicati;
                //ciclo su una
                if(!toAdd.isEmpty() && !toRemove.isEmpty()){
                    for (Resource resourceToCheck : toAdd.keySet()) {
                        if (toRemove.containsKey(resourceToCheck)) {
                            int balance = toAdd.get(resourceToCheck) - toRemove.get(resourceToCheck);
                            if (balance > 0) {
                                toRemove.remove(resourceToCheck);
                                toAdd.put(resourceToCheck, balance);
                            } else if (balance < 0) {
                                toRemove.put(resourceToCheck, -balance);
                                toAdd.remove(resourceToCheck);
                            } else {
                                toAdd.remove(resourceToCheck);
                                toRemove.remove(resourceToCheck);
                            }
                        }
                    }
                }
                return new SelectResourceOutput(deposit, toAdd, toRemove);
            }
        }
    }

    /*   COME LO STAVO PENSANDO TUTTO INSIEME
    public void selectResourcesInStorages(String string) throws IllegalArgumentException{
        string = string.trim();
        string.toUpperCase();
        if (string.isEmpty()){
            //parte il pagamento
            //catalyst.pay ???
        }
        else if(!string.matches("^(([-](L[1-2]|SB|S[1-3]))|(L[1-2]|SB|S[1-3])((: -?[1-9]\\d* (CO|SE|SH|ST))(, -?[1-9]\\d* (CO|SE|SH|ST))*))$")){
            throw new IllegalArgumentException();
        }
        else{
            EnumMap<Resource, Integer> toAdd = new EnumMap<>(Resource.class);
            EnumMap<Resource, Integer> toRemove = new EnumMap<>(Resource.class);
            Payable deposit;

            if (string.charAt(0) == '-'){
                string.substring(1);
                //chiamo il converter passandogli string.substring(1)
                //chiamo il catalyst e faccio una remove di quel deposit
                //catalyst.remove();
                //fine
            }
            else{
                //ora inizia la parte sbatti ma nemmeno troppo

                //inizialmente so che i primi due caratteri sono il tipo di deposito
                deposit = legendStorage(string.substring(0,2));
                //poi ho due punti e spazio
                //il quinto carattere so che è un numero oppure un '-'
                //ma non so quanti numeri lo seguono
                int i;
                int j=2;
                int quantity;
                Resource resource;
                boolean remove = false;
                do{
                    i=j+2;
                    //il quinto carattere so che è un numero oppure un '-'
                    //ma non so quanti numeri lo seguono
                    if (string.charAt(i) == '-'){
                        i+=1;
                        remove = true;
                    }
                    j=i+1;
                    for (; Character.isDigit(string.charAt(j)); j++);
                    quantity = Integer.parseInt(string.substring(i,j));

                    //a questo punto so che j è su qualcosa che non è un numero -> è per forza sullo spazio
                    //-> i prossimi due caratteri identificheranno il tipo di risorsa
                    j++;
                    resource = legendResource(string.substring(j, j+2));

                    //in base a remove so se questa risorsa la voglio levare o tenere
                    if (remove){
                        toRemove.put(resource, quantity);
                    }
                    else{
                        toAdd.put(resource, quantity);
                    }

                    //se j+2 == alla lunghezza della stringa -> ho finito
                    //altrimenti si trova su una virgola e ricomincia,
                }while(j+2<string.length());

                //arrivato a questo punto ho le mie due belle mappe
                //devo capire se ci sono duplicati;
                //ciclo su una
                if(!toAdd.isEmpty() && !toRemove.isEmpty()){
                    for (Resource resourceToCheck : toAdd.keySet()) {
                        if (toRemove.containsKey(resourceToCheck)) {
                            int balance = toAdd.get(resourceToCheck) - toRemove.get(resourceToCheck);
                            if (balance > 0) {
                                toRemove.remove(resourceToCheck);
                                toAdd.put(resourceToCheck, balance);
                            } else if (balance < 0) {
                                toRemove.put(resourceToCheck, -balance);
                                toAdd.remove(resourceToCheck);
                            } else {
                                toAdd.remove(resourceToCheck);
                                toRemove.remove(resourceToCheck);
                            }
                        }
                    }
                }

                //arrivato fino a qui devo fare le remove e le add

                //-----Sezione Commentata
                try{
                    catalyst.remove(toRemove);
                    catalyst.add(toAdd);

                }
                catch (WeDontDoSuchThingsHere e){
                    //significa che in questo punto quando ho provato a fare le robe dentro catalyst
                    //è partito un errore perchè evidentemente l'abilitè non era di tipo storage
                }
                catch (eccezioni che ha specificato alberto){

                }
                //----Fine sezione commentata

           }
        }
    }
     */
}
