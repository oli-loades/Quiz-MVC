/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author 14054494
 */
public class QuizView extends Application implements Observer {

    private final int PREF_HEIGHT = 30;
    private final int TEXTFIELD_WIDTH = 100;
    private final int BUTTON_WIDTH = 100;
    private final int PREF_GAP_SIZE = 15;

    private final TextField tournamentField = new TextField("Tournament");
    private final TextField roundField = new TextField("Round");
    private final TextField roomField = new TextField("Room");
    private final TextField readerField = new TextField("Reader");

    private final TextArea questionField = new TextArea();

    private final VBox gameInfo = new VBox();

    private final GridPane buttons = new GridPane();
    private final ArrayList<Button> buzzButtons = new ArrayList<>();
    private final ArrayList<Button> bonusButtons = new ArrayList<>();

    private final Button next = new Button(">");

    private final ArrayList<TeamPane> teamPanes = new ArrayList<>();

    private Stage secondaryStage; //POP UP

    private QuizModel model;
    private QuizController controller;

    private int numTeams;

    @Override
    public void start(Stage primaryStage) {
        model = new QuizModel();
        numTeams = model.getNumTeams();

        controller = new QuizController(this, model);

        createButtons(); //creates and adds buttons to a list of buttons
        createGameInfoLayout(); //creates addition fields to input game information

        for (int i = 0; i < numTeams; i++) {
            teamPanes.add(new TeamPane(model, i));//creates the layour for each team
        }

        questionField.setPrefSize(700, 200);
        questionField.setWrapText(true);

        GridPane root = new GridPane();
        root.add(gameInfo, 0, 0);
        root.add(questionField, 0, 1, 3, 1);
        root.add(buttons, 1, 2, 3, 1);

        for (int pos = 0; pos < teamPanes.size(); pos++) {
            root.add(teamPanes.get(pos), pos + 1, 0); //adds each team to main gridpane
        }

        buzzButtons.get(0).setOnAction((ActionEvent event) -> {
            controller.buzz(0); // left team buzz
        });

        buzzButtons.get(1).setOnAction((ActionEvent event) -> {
            controller.buzz(1); //right team buzz
        });

        bonusButtons.get(0).setOnAction((ActionEvent event) -> {
            controller.bonus(0); //left team bonus
        });

        bonusButtons.get(1).setOnAction((ActionEvent event) -> {
            controller.bonus(1); //right team bonus
        });

        next.setOnAction((ActionEvent event) -> {
            controller.next();
        });

        root.setVgap(PREF_GAP_SIZE);
        root.setHgap(PREF_GAP_SIZE);

        model.addObserver(this);
        update(null, null);

        Scene scene = new Scene(root, 650, 520);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void createButtons() {
        for (int i = 0; i < 2; i++) {
            buzzButtons.add(new Button("Buzz!"));
            bonusButtons.add(new Button("+Bonus"));
        }

        for (Button b : buzzButtons) {
            b.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
        }

        for (Button b : bonusButtons) {
            b.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
            b.setDisable(true);//bonus disabled at start
        }

        int pos = 0;
        for (int i = 0; i < 2; i++) {
            if (i == 1) {//if team 2 buttons, allows for gap to add next button
                pos = 3;
            }
            buttons.add(buzzButtons.get(i), pos, 0);
            buttons.add(bonusButtons.get(i), pos, 1);
        }

        next.setMinSize(BUTTON_WIDTH, PREF_HEIGHT);
        buttons.add(next, 1, 2);
        buttons.setHgap(PREF_GAP_SIZE);
        buttons.setVgap(PREF_GAP_SIZE);
    }

    public void createGameInfoLayout() {
        tournamentField.setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
        roundField.setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
        roomField.setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
        readerField.setPrefSize(TEXTFIELD_WIDTH, PREF_HEIGHT);
        gameInfo.getChildren().add(tournamentField);
        gameInfo.getChildren().add(roundField);
        gameInfo.getChildren().add(roomField);
        gameInfo.getChildren().add(readerField);
    }

    public void createBuzzPopUp(int team) {
        BuzzPopUp dialog = new BuzzPopUp(team, model, controller);
        secondaryStage = new Stage();
        Scene secondaryScene = new Scene(dialog, 200, 150);
        secondaryStage.setTitle("BUZZ");
        secondaryStage.setScene(secondaryScene);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    public void closePopUp() {
        secondaryStage.close();
    }

    public void disableTeamButtons(int teamNum) {
        buzzButtons.get(teamNum).setDisable(true);
        bonusButtons.get(teamNum).setDisable(true);
    }

    public void enableBonusByTeam(int teamNum) {
        bonusButtons.get(teamNum).setDisable(false);
    }

    public void configureBonus(boolean disable) {
        for (Button b : bonusButtons) {
            b.setDisable(disable);
        }
    }

    public void configureBuzz(boolean enable) {
        for (Button b : buzzButtons) {
            b.setDisable(enable);
        }
    }

    public void configureNext(boolean disable) {
        next.setDisable(disable);
    }

    public void configureAllButtons(boolean disable) {
        configureBuzz(disable);
        configureBonus(disable);
        configureNext(disable);
    }

    @Override
    public void update(Observable o, Object arg) {
        questionField.setText(model.getCurrentQuestion());
    }

}
