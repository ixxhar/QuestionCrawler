package izharhussain.questioncrawler;

import android.net.Uri;

public class UserModelClass {
    private String name, email, password, city;

    //modifications for chat
    //modifications for chat
    //modifications for chat
    private String connection;
    private long createdAt;
    private String recipientID;

    public String createUniqueChatRef(long createdAtCurrentUser, String currentUserEmail) {
        String uniqueChatRef = "";
        if (createdAtCurrentUser > getCreatedAt()) {
            uniqueChatRef = cleanEmailAddress(currentUserEmail) + "-" + cleanEmailAddress(getUserEmail());
        } else {

            uniqueChatRef = cleanEmailAddress(getUserEmail()) + "-" + cleanEmailAddress(currentUserEmail);
        }
        return uniqueChatRef;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    private String cleanEmailAddress(String email) {
        //replace dot with comma since firebase does not allow dot
        return email.replace(".", "-");
    }

    private String getUserEmail() {
        //Log.e("user email  ", userEmail);
        return email;
    }

    public String getConnection() {
        return connection;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    //modifications for chat
    //modifications for chat
    //modifications for chat


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserModelClass{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", connection='" + connection + '\'' +
                ", createdAt=" + createdAt +
                ", recipientID='" + recipientID + '\'' +
                '}';
    }
}
