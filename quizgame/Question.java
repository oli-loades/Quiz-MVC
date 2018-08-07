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
public class Question {

    private String questionAndAnswer;

    public Question(String question) {
        questionAndAnswer = question;
    }

    @Override
    public String toString() {
        return questionAndAnswer;
    }

    public void setQuestionAndAnswer(String question) {
        questionAndAnswer = question;
    }

}
