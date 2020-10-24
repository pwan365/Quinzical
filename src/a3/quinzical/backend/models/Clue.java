package a3.quinzical.backend.models;

import java.util.ArrayList;
import java.util.List;

import a3.quinzical.backend.tasks.Formatting;

/**
 * This class is used as a model for all the clues of our Quinzical game. It contains various
 * fields and methods which would make it easier for the developers to interact with these
 * entities.
 *
 * @author Shrey Tailor, Jason Wang
 */
public class Clue {

    // Fields required for the model of our Clue.
    private String question;
    private String prefix;
    private String answer;
    private int prize;
    private Category category;

    /**
     * This is the only constructor for creating an object for Clue.
     * @param question the question of the clue.
     * @param answer the answer of the clue. Note that this string can also include (What is) etc
     * @param category the category the clue belongs to.
     * part of the string as well, because there is a method to compare the answer.
     */
    public Clue(String Question, String Prefix, String Answer, Category Category) {
        Question = question;
        Prefix = prefix;
        Answer = answer;
        Category = category;
    }

    public String getQuestion() {
        return question;
    }

    /**
     * This method is used to get the answer for the current clue.
     * @return String the answer for the current clue.
     */
    public String getAnswer() {
        return Formatting.removeTheA(answer);
    }
    
    /**
     * This method is used to get the non-sanitized answer for the current clue.
     * @return String the answer for the current clue.
     */
    public String getFullAnswer() {
    	return answer;
    }
    
    /**
     * This method is used to get the answers for the current clue.
     * @return a list of strings representing the possible answers of the clue.
     */
    public List<String> getAnswersList() {
    	List<String> answerList = new ArrayList<String>();
    	String[] answerArray = answer.split("/");
    	for(int i = 0; i < answerArray.length; i++) {
    		answerList.add(Formatting.removeTheA(answerArray[i]));
    	}
    	return answerList;
    }

    /**
     * This method is used to check the user's answer against the correct answer. It'll do all the
     * processing required regarding taking care if the user had (What is), (Who is) etc, or even
     * if it doesn't. The method does the work of trimming the answer firstly, and keeping things
     * case-insensitive while comparing.
     * @param answer the user's answer which will be checked against the correct answer.
     * @return boolean true if the answer was correct, otherwise false.
     */
    public boolean checkAnswer(String answer) {
    	// Removing any repeated space characters from the user's input.
        answer = Formatting.sanitize(answer);
    	
        // Loop through multiple answers if exist
    	String[] answerList = answer.split("/");
    	for(int i = 0; i < answerList.length; i++) {
    		answerList[i] = Formatting.sanitize(answerList[i]);
    		if (answer.equalsIgnoreCase(answerList[i])){
    			return true;
    		}
    	}
        
        // If no matches, then just return false.
        return false;
    }

    public void setPrize(int p) {
        p = prize;
    }

    public int getPrize() {
        return prize;
    }
    
    public Category getCategory() {
    	return category;
    }
    
    public String getPrefix() {
    	return prefix;
    }
}
