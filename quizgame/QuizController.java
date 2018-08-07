/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

/**
 *
 * @author 14054494
 */
public class QuizController {

    private final QuizView view;
    private final QuizModel model;

    private boolean isBonus;
    private boolean noMoreStarters;
    private boolean gameEnd;

    public QuizController(QuizView view, QuizModel model) {
        this.view = view;
        this.model = model;
        isBonus = false;
        noMoreStarters = false;
        gameEnd = false;
    }

    public void buzz(int team) {
        view.createBuzzPopUp(team);
    }

    public void correct(int team, int player) {
        model.questionCorrect(team, player);
        model.getNextBonus();

        isBonus = true;

        view.closePopUp();

        view.enableBonusByTeam(team);//enables bonus button for which team got it correct
        view.configureBuzz(true);//disables buzz buttons
        view.configureNext(false); //enables net question button
    }

    public void wrong(int team) {
        view.disableTeamButtons(team);//disables tams buttons
        view.closePopUp();
    }

    public void early(int team, int player) {
        model.buzzEarly(team, player);// minus points
        view.disableTeamButtons(team);
        view.closePopUp();
    }

    public void bonus(int team) {
        model.bonus(team);
        if (gameEnd) {//final bonus question
            view.configureAllButtons(true);
        }
    }

    public void next() {
        if (noMoreStarters) {
            NoMoreStaters();
        } else {
            if (model.hasNextStarter()) {
                moreStarters();
            }
            if (model.finalStarter()) {
                startersEnding();
            }
        }
    }

    private void startersEnding() {
        noMoreStarters = true;
        view.configureNext(true);
    }

    private void NoMoreStaters() {
        //no more starters
        if (isBonus) {//last bonus round
            if (!model.finalSub()) {//not last sub
                model.getNextBonusSub();
            } else {//last sub
                model.getNextBonusSub();
                gameEnd = true;
                view.configureNext(true);
            }
        }
    }

    private void moreStarters() {
        if (!isBonus) {
            //next starters question
            model.getNextStarter();
            view.configureBuzz(false);
            view.configureNext(false);
        } else if (model.moreBonusSub()) {
            //next bonus sub question
            model.getNextBonusSub();
            view.configureNext(false);
        } else {
            //if bonus round has ended
            isBonus = false;
            model.getNextStarter();
            view.configureAllButtons(false);
            view.configureBonus(true);
        }
    }
}
