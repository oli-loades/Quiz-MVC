/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author 14054494
 */
public class BuzzPopUp extends GridPane {

    private final int PREF_H_GAP = 10;
    private final int PREF_V_GAP = 10;

    private final int BUTTON_WIDTH = 50;
    private final int PREF_HEIGHT = 30;

    private final QuizModel model;
    private final int teamNum;
    private final ArrayList<RadioButton> players;
    private final ArrayList<Button> buttons;
    private final Button correct = new Button("Correct");
    private final Button wrong = new Button("Wrong");
    private final Button early = new Button("Early");
    private final HBox buttonHBox = new HBox(PREF_H_GAP);

    private final ToggleGroup playerGroup = new ToggleGroup();

    private QuizController controller;

    public BuzzPopUp(int teamNum, QuizModel model, QuizController controller) {
        this.model = model;
        this.teamNum = teamNum;
        this.controller = controller;
        players = new ArrayList<>();
        buttons = new ArrayList<>();

        init();

        players.get(0).setSelected(true);//selects top radio button to be selected by defualt

        correct.setOnAction((ActionEvent event) -> {
            this.controller.correct(teamNum, getSelectedPlayer());
        });

        wrong.setOnAction((ActionEvent event) -> {
            this.controller.wrong(teamNum);
        });

        early.setOnAction((ActionEvent event) -> {
            this.controller.early(teamNum, getSelectedPlayer());
        });
    }

    private void init() {

        int numPlayers = model.getNumPlayers();

        for (int i = 0; i < numPlayers; i++) {
            players.add(new RadioButton(model.getTeam(teamNum).getPlayer(i).getName()));//create new radio button with players name
        }

        addRadioPlayers();//adds components to grid
        setPlayerGroups(); //sets a toggle group so only 1 player can be selected
        setGap();
        setButtonSize();
        addButtons();
    }

    private void addRadioPlayers() {
        int pos = 1;
        for (RadioButton rb : players) {
            add(rb, 1, pos);
            pos++;
        }
    }

    private void setButtonSize() {
        correct.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
        wrong.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
        early.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
    }

    private void addButtons() {
        buttons.add(correct);
        buttons.add(wrong);
        buttons.add(early);

        int y = model.getNumPlayers() + 2;

        for (Button b : buttons) {
            buttonHBox.getChildren().add(b);
        }

        add(buttonHBox, 1, y);
    }

    private void setPlayerGroups() {
        for (RadioButton rb : players) {
            rb.setToggleGroup(playerGroup);
        }
    }

    private void setGap() {
        setHgap(PREF_H_GAP);
        setVgap(PREF_V_GAP);
    }

    private int getSelectedPlayer() {
        RadioButton rb = (RadioButton) playerGroup.getSelectedToggle();//selected radio button
        String selected = rb.getText();
        int numSelected = 0;
        Team team = model.getTeam(teamNum);
        int numPlayers = model.getNumPlayers();
        for (int i = 0; i < numPlayers; i++) {
            if (selected.equals(team.getPlayer(i).getName())) {//finds position within teh list of players that has been selected
                numSelected = i;
            }
        }
        return numSelected;
    }

}
