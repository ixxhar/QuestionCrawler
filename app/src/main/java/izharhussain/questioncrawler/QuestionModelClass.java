package izharhussain.questioncrawler;

import java.io.Serializable;

public class QuestionModelClass implements Serializable {
    private String id, category, title, description, date, postedBy, timeLimit, postedByEmail;

    public String getPostedByEmail() {
        return postedByEmail;
    }

    public void setPostedByEmail(String postedByEmail) {
        this.postedByEmail = postedByEmail;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    @Override
    public String toString() {
        return "QuestionModelClass{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", postedBy='" + postedBy + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", postedByEmail='" + postedByEmail + '\'' +
                '}';
    }
}
