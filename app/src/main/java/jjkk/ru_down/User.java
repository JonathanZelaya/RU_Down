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
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;

        // Default values
        this.preferredCampus = "No Preference";
        this.preferredGender = "No Preference";
        this.phone = "";
    }

    public String getPreferredCampus() {
        return preferredCampus;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public String getPhone(){
        return phone;
    }
}
