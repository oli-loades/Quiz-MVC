/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author 14054494
 */
public class TeamPane extends GridPane implements Observer {

    private final int LABEL_WIDTH = 30;
    private final int PREF_HEIGHT = 30;
    private final int TEXTFIELD_WIDTH = 100;
    private final int PREF_GAP_SIZE = 5;

    private TextField teamName = new TextField();
    private Label teamScore = new Label("0");
    private Label plusTen = new Label("+10");
    private Label minusFive = new Label("-5");

    private ArrayList<TextField> playerNames = new ArrayList<>();
    private ArrayList<Label> playerScore = new ArrayList<>();
    private ArrayList<Label> playerCorrect = new ArrayList<>();
    private ArrayList<Label> playerWrong = new ArrayList<>();

    private QuizModel model;
    private int teamNum;
    private int numPlayers;

    public TeamPane(QuizModel model, int teamNum) {
        this.model = model;
        this.teamNum = teamNum;
        numPlayers = this.model.getNumPlayers();

        teamName.setText("Team " + (teamNum + 1));

        for (int i = 0; i < numPlayers; i++) {
            playerNames.add(new TextField("Player " + (i + 1)));
            playerScore.add(new Label("0"));
            playerCorrect.add(new Label("0"));
            playerWrong.add(new Label("0"));
        }

        setSizes(); //sets sizes of components
        addElements(); //adds components to gridPane
        setGapSize(); //sets gap ize of gridpane
        setUpTeam(); //sets name of players and team with default values

        model.addObserver(this);
        update(null, null);
    }

    private void setSizes() {
        teamName.setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
        teamScore.setPrefSize(LABEL_WIDTH, PREF_HEIGHT);
        plusTen.setPrefSize(LABEL_WIDTH, PREF_HEIGHT);
        minusFive.setPrefSize(LABEL_WIDTH, PREF_HEIGHT);

        for (int i = 0; i < numPlayers; i++) {
            playerNames.get(i).setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
            playerCorrect.get(i).setPrefSize(LABEL_WIDTH, PREF_HEIGHT);
            playerWrong.get(i).setPrefSize(LABEL_WIDTH, PREF_HEIGHT);
            playerScore.get(i).setPrefSize(LABEL_WIDTH, PREF_HEIGHT);

        }

    }

    private void addElements() {
        int playerNum = 0;
        for (TextField name : playerNames) {
            final Player player = model.getTeam(teamNum).getPlayer(playerNum); //gets the player
            name.textProperty().addListener((obs, oldVal, newVal) -> {//when text field changes
                player.setName(newVal);//updates player name
            });
            playerNum++;//next player
        }

        teamName.textProperty().addListener((obs, oldVal, newVal) -> {
            model.getTeam(teamNum).setTeamName(newVal);
        });

        add(teamName, 0, 0);
        add(plusTen, 1, 0);
        add(minusFive, 2, 0);
        add(teamScore, 3, 0);

        for (int i = 0; i < numPlayers; i++) {
            add(playerNames.get(i), 0, i + 1);
            add(playerCorrect.get(i), 1, i + 1);
            add(playerWrong.get(i), 2, i + 1);
            add(playerScore.get(i), 3, i + 1);
        }

    }

    private void setGapSize() {
        setHgap(PREF_GAP_SIZE);
        setVgap(PREF_GAP_SIZE);
    }

    @Override
    public void update(Observable o, Object arg) {
        teamName.setText(model.getTeam(teamNum).getTeamName());
        teamScore.setText(Integer.toString(model.getTotalScore(teamNum)));

        for (int i = 0; i < numPlayers; i++) {
            playerNames.get(i).setText(model.getTeam(teamNum).getPlayer(i).getName());
            playerScore.get(i).setText(Integer.toString(model.getTeam(teamNum).getPlayer(i).getTotalScore()));
            playerCorrect.get(i).setText(Integer.toString(model.getTeam(teamNum).getPlayer(i).getNumCorrect()));
            playerWrong.get(i).setText(Integer.toString(model.getTeam(teamNum).getPlayer(i).getNumMinus()));
        }

    }

    private void setUpTeam() {
        //sets team and player names to defualt values
        model.getTeam(teamNum).setTeamName(teamName.getText());
        for (int i = 0; i < model.getNumPlayers(); i++) {
            model.getTeam(teamNum).getPlayer(i).setName(playerNames.get(i).getText());
        }
    }

}
