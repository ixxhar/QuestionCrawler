package izharhussain.questioncrawler;

public class AnswerModelClass {
    private String questionID, description, answerBy, date;

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

    @Override
    public String toString() {
        return "AnswerModelClass{" +
                "questionID='" + questionID + '\'' +
                ", description='" + description + '\'' +
                ", answerBy='" + answerBy + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
