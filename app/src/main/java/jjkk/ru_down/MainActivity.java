package jjkk.ru_down;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //writeNewUser("Jonathan Zelaya", "zelaya0805@gmail.com", "password");


        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_login,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Login or Register").setView(view)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(userValid()){
                            // close dialog and store values in SharedPreferences

                        }else{
                            Toast.makeText(MainActivity.this, "Invalid login, try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText emailET = (EditText) view.findViewById(R.id.et_email);
                        EditText passwordET = (EditText) view.findViewById(R.id.et_password);

                        String email = emailET.getText().toString();
                        String password = passwordET.getText().toString();

                        writeNewUser("User",email,password);
                        storeUserInfo();
                    }
                })
                .setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void goToSports(View view){
        Intent intent = new Intent(this, SportsListActivity.class);
        startActivity(intent);

    }

    private void writeNewUser(String name, String email, String password) {
        email = email.replace(".", "dot");
        User user = new User(name, email, password);
        mDatabase.child("users").child(email).setValue(user);
    }

    private void storeUserInfo(){

    }

    private boolean userValid(){
        return false;
    }
}
