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
public class Player {

    private String name;
    private int numCorrect;//tracks number of corretc answers
    private int numMinus;//trcks the number of timnes minus points

    public Player() {
        name = "";
        numCorrect = 0;
        numMinus = 0;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void correct() {
        numCorrect++;
    }

    public void early() {
        numMinus++;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public int getNumMinus() {
        return numMinus;
    }

    public int getTotalScore() {
        return calculateTotalScore();
    }

    private int calculateTotalScore() {
        //times numcorrect by 10 as each question is worth 10
        //same with minus
        return (10 * numCorrect) + (-5 * numMinus);
    }
}
