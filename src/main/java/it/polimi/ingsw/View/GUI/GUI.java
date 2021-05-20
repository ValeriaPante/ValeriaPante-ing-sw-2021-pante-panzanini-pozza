package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Network.Client.MessageToServerCreator;
import it.polimi.ingsw.View.ClientModel.Game;
import it.polimi.ingsw.View.Observable;
import it.polimi.ingsw.View.View;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class GUI extends Observable implements View {
    private final Game model;

    private JFrame frame;

    private JPanel scrollPanel;
    private HashMap<Integer, String[]> lobbiesToPut;


    public GUI(){
        model = new Game();

        frame = new JFrame("Masters of Renaissance");
        HomePanel panel = new HomePanel();
        panel.addObserver(this);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void updateToUsernameFrame(){
        frame.setVisible(false);
        frame.dispose();
        frame = new JFrame("Masters of Renaissance");
        UsernamePanel panel = new UsernamePanel();
        panel.addObserver(this);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    public void updateToLobbyFrame(String username){
        this.sendMessageToServer(username);
        frame.setVisible(false);
        frame.dispose();
        frame = new JFrame("Masters of Renaissance");

        scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(0, 4));
        JPanel p = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        p.add(scrollPane, BorderLayout.CENTER);

        synchronized (lobbiesToPut){
            for(HashMap.Entry<Integer, String[]> entry: lobbiesToPut.entrySet())
                scrollPanel.add(new SingleLobbyPanel(entry.getKey(), entry.getValue()));
        }

        frame.add(p);
        frame.setSize(800, 300);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

    }

    @Override
    public void updateLobbyState(int lobbyId, String[] players) {
        synchronized (lobbiesToPut){
            if(scrollPanel == null){
                lobbiesToPut.put(lobbyId, players);
            } else {
                scrollPanel.remove(SingleLobbyPanel.removeLobby(lobbyId));
                scrollPanel.add(new SingleLobbyPanel(lobbyId, players));
                frame.setVisible(true);
            }
        }
    }

    @Override
    public void removeLobby(int lobbyId) {
        synchronized (lobbiesToPut){
            if(scrollPanel == null){
                lobbiesToPut.remove(lobbyId);
            } else {
                scrollPanel.remove(SingleLobbyPanel.removeLobby(lobbyId));
                frame.setVisible(true);
            }
        }
    }

    @Override
    public void chooseLobby(int lobbyId){
        this.sendMessageToServer(MessageToServerCreator.createMoveToLobbyMessage(lobbyId));

        frame.setVisible(false);
        frame.dispose();
        frame = new JFrame("Masters of Renaissance");
        frame.add(new JLabel("Loading"));
        frame.setSize(400,500);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void chooseLeaderCards() {

    }

    @Override
    public void chooseInitialResources() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void showMarket() {

    }

    @Override
    public void showDevDecks() {

    }

    @Override
    public void updatePositions(int playerId, int pos) {

    }

    @Override
    public void updatePopeFavourCard(int playerId, PopeFavorCardState[] states) {

    }

    @Override
    public void updateStrongbox(int playerId) {

    }

    @Override
    public void updateShelves(int playerId, int numShelf) {

    }

    @Override
    public void updateSupportContainer(int playerId) {

    }

    @Override
    public void updateLeaderStorage(int playerId, int cardId) {

    }

    @Override
    public void activateLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void discardLeaderCard(int playerId, int cardId) {

    }

    @Override
    public void addDevCardInSlot(int playerId, int cardId, int slot) {

    }

    @Override
    public void showWinner(String username) {

    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Warning!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public Game getModel(){
        return model;
    }
}
