/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Scanner;

/**
 *
 * @author 14054494
 */
public class QuizModel extends Observable {

    private final int NUM_TEAMS = 2;
    private final File FILE = new File("questions.txt");

    private ArrayList<Team> teams;
    private ArrayList<Question> starterQuestions;
    private ArrayList<Bonus> bonusQuestions;

    private Iterator<Question> starterIterator;
    private Iterator<Bonus> bonusIterator;

    private Question currentStarter;
    private Bonus currentBonus;
    private String currentSub;
    private String currentQuestion;

    public QuizModel() {
        initialise();
    }

    public int getNumTeams() {
        //PRE: A QuizModel object exists
        //POST: Number of teams returned
        return NUM_TEAMS;
    }

    public Team getTeam(int index) {
        //PRE: index<=teams.size()
        //POST: team in position index is redturned
        assert (teams.size() > index) : "index out of bounds";
        return teams.get(index);
    }

    public void addStarter(Question newStarter) {
        //PRE:list exists && question != null
        //POST: question is added to starter list
        assert ((newStarter != null) && (starterQuestions != null)) : "question cannot be null or list does not exist";
        starterQuestions.add(newStarter);
    }

    public void addBonus(Bonus newBonus) {
        //PRE:list exists && bonus != null
        //POST: question is added to bonus list
        assert ((newBonus != null) && (starterQuestions != null)) : "bonus cannot be null or list doesn ot exist";
        bonusQuestions.add(newBonus);
    }

    public void readFile() {
        //PRE:QuizModel istance has been created
        //POST: question have been added to the lists from the text file
        boolean isStarter = true;

        try (Scanner fileScanner = new Scanner(FILE, "Unicode")) {//specified encoding as i had issues with scanner and ANSI and  UTF-8

            String question, line;

            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();
                switch (line) {
                    case "STARTERS":
                        //do nothing but ignore that line
                        break;
                    case "BONUSES":
                        isStarter = false;
                        break;
                    default:
                        if (!line.equals("")) {//not empty line
                            if (isStarter) {//first 20 questions
                                question = line;
                                fileScanner.nextLine();
                                line = fileScanner.nextLine(); //answer
                                addStarter(new Question(question + "\n" + "\n" + line));//creates new starter question and adds it to list
                            } else {//bonus questions
                                question = line;
                                question += "\n";
                                fileScanner.nextLine();
                                Bonus newBonus = new Bonus(question);//creates new bonus object

                                for (int i = 0; i < 3; i++) {
                                    question = "";

                                    line = fileScanner.nextLine();
                                    question += line; //sub question
                                    question += "\n" + "\n";
                                    fileScanner.nextLine(); //ignore line
                                    line = fileScanner.nextLine();
                                    question += line; //sub answer
                                    fileScanner.nextLine();
                                    newBonus.addSub(question);//add sub question to bonus
                                }

                                addBonus(newBonus);//adds bonsu question to list
                            }
                        }
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("no file found");
        }
    }

    public void getNextBonus() {
        //PRE:list exists && there is more bonsu questions
        //POST: current question becomes the next question in the bonus list
        assert ((bonusIterator.hasNext()) && (bonusQuestions != null)) : "final bonus question or list is null";
        currentBonus = bonusIterator.next(); //gets next bonus question
        currentSub = currentBonus.getNextSub(); //gets next sub question

        currentQuestion = currentBonus.toString() + "\n" + currentSub; //used to update GUI

        setChanged();
        notifyObservers();
    }

    public void getNextBonusSub() {
        //PRE:list exists && list has more questions
        //POST: current question becomes the next sub question in the current bonus question
        assert ((currentBonus.hasNextSub()) && (bonusQuestions != null)) : "no more sub question or list is null";
        currentSub = currentBonus.getNextSub();//next sub question
        currentQuestion = currentBonus.toString() + "\n" + currentSub;
        setChanged();
        notifyObservers();
    }

    public String getCurrentQuestion() {
        //PRE:current question is not null
        //POST: returns current question
        assert (currentQuestion != null) : "current question is null";
        return currentQuestion;
    }

    public boolean finalStarter() {
        //PRE:current question is not null
        //POST: boolean return whether current question starts with teh characters 20
        assert (currentQuestion != null) : "current question is null";
        String temp = currentQuestion.substring(0, 2);
        return temp.equals("20");//if its the last starter
    }

    public void getNextStarter() {
        //PRE:there are more starter questions
        //POST: current questiosn becomes next starter question in the list
        assert (starterIterator.hasNext()) : "no more starter questions";
        currentStarter = starterIterator.next();
        currentQuestion = currentStarter.toString();
        setChanged();
        notifyObservers();
    }

    public void questionCorrect(int teamNum, int playerNum) {
        //PRE:teamNum > size of teams list
        //POST: number of questions answered correctly by given player is increased by 1
        assert (teams.size() >= teamNum) : "teamNum is out of bounds";
        getTeam(teamNum).getPlayer(playerNum).correct();//adds 10 to correct player
        setChanged();
        notifyObservers();
    }

    public void buzzEarly(int teamNum, int playerNum) {
        //PRE:teamNum > size of teams list
        //POST: number of times player has interupted inccorectly by given player is increased by 1
        assert (teams.size() >= teamNum) : "teamNum is out of bounds";
        getTeam(teamNum).getPlayer(playerNum).early(); //minus points
        setChanged();
        notifyObservers();
    }

    public void bonus(int teamNum) {
        //PRE:teamNum > size of teams list
        //POST: number of bonus questions answered by given team incrased by 1;
        assert (teams.size() >= teamNum) : "teamNum is out of bounds";
        getTeam(teamNum).addbonusAnswered();//adds 5 to correct team
        setChanged();
        notifyObservers();
    }

    public void initialise() {
        //PRE: QuizModel object has been initilised
        //POST: QuizModel is in a state that can be used by other classes
        teams = new ArrayList<>();
        starterQuestions = new ArrayList<>();
        bonusQuestions = new ArrayList<>();

        createTeams();
        readFile();

        starterIterator = starterQuestions.iterator(); //cretaes iterator
        bonusIterator = bonusQuestions.iterator();

        currentStarter = starterIterator.next();//first starter
        currentQuestion = currentStarter.toString();

        setChanged();
        notifyObservers();
    }

    public int getNumPlayers() {
        //PRE: QuizModel object has been initilised
        //POST: number of players returned
        return Team.getNumPlayers();
    }

    private void createTeams() {
        //PRE: QuizModel object has been initilised
        //POST: NUM_TEAMS teams have been created with given number of players
        for (int i = 0; i < NUM_TEAMS; i++) {//loop through each team
            Team newTeam = new Team();
            for (int j = 0; j < Team.getNumPlayers(); j++) {//loop through each player
                newTeam.addPlayer(new Player());
            }
            teams.add(newTeam);
        }
    }

    public boolean hasNextStarter() {
        //PRE: iterator is not null
        //POST: true if more starter questions. false if not
        assert (starterIterator != null) : "interator is null";
        return starterIterator.hasNext();
    }

    public boolean moreBonusSub() {
        //PRE: currentBonus is not null
        //POST: true if more bonus sub questions. false if not
        assert (currentBonus != null) : "list is null";
        return currentBonus.hasNextSub();
    }

    public boolean finalSub() {
        //PRE: currentBonus is not null
        //POST: true if final bonus sub question of set. false if not
        assert (currentBonus != null) : "list is null";
        return currentBonus.getSubNum() == (currentBonus.getSubSize() - 1);
    }

    public int getTotalScore(int teamNum) {
        //PRE: teamNum<=teams.size()
        //POST: total score of given team is returned
        assert (teams.size() >= teamNum) : "index out of bounds";
        return getTeam(teamNum).getTotalScore();
    }

}
