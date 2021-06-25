import com.google.gson.Gson;
import it.polimi.ingsw.Network.Client.Messages.ChangedLobbyMessage;
import it.polimi.ingsw.Network.Client.Messages.FromServerMessage;
import org.junit.jupiter.api.Test;

public class MessageTest {
    @Test
    public void noActionTest(){
        ChangedLobbyMessage message = new ChangedLobbyMessage(1, new String[]{"Daniel", "Vale"}, false);
        outputTest(message);
    }
    private void outputTest(FromServerMessage message){
        Gson gson = new Gson();
        System.out.println(gson.toJson(message));
    }
}
