/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

import java.util.ArrayList;

/**
 *
 * @author 14054494
 */
public class Bonus extends Question {

    private final ArrayList<String> subQuestions;
    private int subNum;

    public Bonus(String topic) {
        super(topic);
        subQuestions = new ArrayList<>();
        subNum = 0;
    }

    public String getNextSub() {
        String temp = subQuestions.get(subNum);
        subNum++;
        return temp;
    }

    public void addSub(String question) {
        subQuestions.add(question);
    }

    public boolean hasNextSub() {
        return subNum < subQuestions.size();
    }

    public int getSubNum() {
        return subNum;
    }

    public int getSubSize() {
        return subQuestions.size();
    }

}
