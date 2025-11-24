/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillfrog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GAMING
 */
public class QuizBuilder {

    private List<String> questions = new ArrayList<>();
    private List<List<String>> options = new ArrayList<>();
    private List<Integer> correctAnswers = new ArrayList<>();
    private boolean retryAllowed;

    public QuizBuilder(boolean retryAllowed) {
        this.retryAllowed = retryAllowed;
    }

    public void addQuestion(String question, List<String> choices, int correctIndex) {
        questions.add(question);
        options.add(choices);
        correctAnswers.add(correctIndex);
    }

    public Quiz build() {
        return new Quiz(questions, options, correctAnswers, retryAllowed);
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<List<String>> getOptions() {
        return options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public boolean isRetryAllowed() {
        return retryAllowed;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public void setOptions(List<List<String>> options) {
        this.options = options;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setRetryAllowed(boolean retryAllowed) {
        this.retryAllowed = retryAllowed;
    }

}
