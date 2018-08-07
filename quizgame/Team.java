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
public class Team {

    private String name;
    private final ArrayList<Player> players;
    private int bonusAnswered; //tracks the number of binsu questions answered
    private final static int NUM_PLAYERS = 4;

    public Team() {
        name = "";
        players = new ArrayList<>();
        bonusAnswered = 0;
    }

    public void setTeamName(String newTeamName) {
        name = newTeamName;
    }

    public String getTeamName() {
        return name;
    }

    public void addbonusAnswered() {
        bonusAnswered++;
    }

    public int getBonusAnswered() {
        return bonusAnswered;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public int claculateTotalScore() {
        int total = 0;
        for (Player p : players) {
            total = total + p.getTotalScore();//calculates ttoal of all player in the team
        }
        return total + (getBonusAnswered() * 5);//adds the points of the bonus questions answered
    }

    public int getTotalScore() {
        return claculateTotalScore();
    }

    public static int getNumPlayers() {
        return NUM_PLAYERS;
    }

}
