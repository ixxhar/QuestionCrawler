package izharhussain.questioncrawler;

public class Like {
    public String questionID;
    public String userID;

    public Like() {
        // Default constructor required for calls to DataSnapshot.getValue(Like.class)
    }

    public Like(String questionID, String userID) {
        this.questionID = questionID;
        this.userID = userID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}