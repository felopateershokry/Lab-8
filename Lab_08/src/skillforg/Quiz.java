/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforg;
import java.util.List;
/**
 *
 * @author Enter Computer
 */
public class Quiz {
    private List<String> questions;
    private List<List<String>> options;
    private List<Integer> correct;
    private boolean retryOption;
    public Quiz(List<String> questions,
                List<List<String>> options,
                List<Integer> correct,
                boolean retryOption) {

        this.questions = questions;
        this.options = options;
        this.correct = correct;
        this.retryOption = retryOption;
    }

    public List<String> getQuestions() { return questions; }
    public List<List<String>> getOptions() { return options; }
    public List<Integer> getCorrectAnswers() { return correct; }
    public boolean isRetryAllowed() { return retryOption; }
} 
    

