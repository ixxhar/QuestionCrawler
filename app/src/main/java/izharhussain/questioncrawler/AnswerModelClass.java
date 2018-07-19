package izharhussain.questioncrawler;

import java.io.Serializable;

public class AnswerModelClass implements Serializable {
    private String questionID, description, answerBy, date, answerByEmail;

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswerBy() {
        return answerBy;
    }

    public void setAnswerBy(String answerBy) {
        this.answerBy = answerBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnswerByEmail() {
        return answerByEmail;
    }

    public void setAnswerByEmail(String answerByEmail) {
        this.answerByEmail = answerByEmail;
    }

    @Override
    public String toString() {
        return "AnswerModelClass{" +
                "questionID='" + questionID + '\'' +
                ", description='" + description + '\'' +
                ", answerBy='" + answerBy + '\'' +
                ", date='" + date + '\'' +
                ", answerByEmail='" + answerByEmail + '\'' +
                '}';
    }
}
