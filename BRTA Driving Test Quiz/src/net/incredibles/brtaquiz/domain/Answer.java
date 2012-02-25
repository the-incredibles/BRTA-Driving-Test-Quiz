package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author sharafat
 * @Created 2/15/12 8:37 PM
 */
public class Answer {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, canBeNull = false, index = true)
    private Question question;

    @DatabaseField(columnName = "answer_sign_id", foreign = true, canBeNull = false)
    private Sign answer;

    @DatabaseField
    private Boolean correct;

    @DatabaseField
    private Boolean marked;

    public Answer() {
    }

    public Answer(int id) {
        this.id = id;
    }

    public Answer(Question question, Sign answer) {
        this.question = question;
        this.answer = answer;
    }

    public Answer(Question question, Sign answer, boolean  correct, boolean marked) {
        this.question = question;
        this.answer = answer;
        this.correct = correct;
        this.marked = marked;
    }

    public int getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public Sign getAnswer() {
        return answer;
    }

    public void setQuestionAndAnswer(Question question, Sign answer) {
        this.question = question;
        this.answer = answer;
    }

    public Boolean isCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Boolean isMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", question=" + question +
                ", answer=" + answer +
                '}';
    }
}
