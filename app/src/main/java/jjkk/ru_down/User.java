package jjkk.ru_down;

/**
 * Created by Jonathan on 4/22/17.
 */

public class User {
    public String fullName;
    public String email;
    public String password;
    public String preferredCampus;
    public String preferredGender;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;

        // Default values
        preferredCampus = "No Preference";
        preferredGender = "No Preference";
    }

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public String getPreferredGender() {
        return preferredGender;
    }
}
