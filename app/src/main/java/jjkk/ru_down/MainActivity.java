package jjkk.ru_down;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.button;
import static android.R.attr.data;
import static android.R.attr.password;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //writeNewUser("Jonathan Zelaya", "zelaya0805@gmail.com", "password");


        final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_login,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Login or Register").setView(view)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText emailET = (EditText) view.findViewById(R.id.et_email);
                        final String email = emailET.getText().toString().replace(".","dot");

                        EditText passwordET = (EditText) view.findViewById(R.id.et_password);
                        String password = passwordET.getText().toString();

                        if(userValid(email, password)){
                            // close dialog and store values in SharedPreferences
                            DatabaseReference ref = mDatabase.child("users").child(email);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Context context = MainActivity.this;
                                    SharedPreferences sharedPref = getSharedPreferences("sharedPref", context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    editor.putString("campus",dataSnapshot.child("preferredCampus").getValue().toString());
                                    editor.putString("gender",dataSnapshot.child("preferredGender").getValue().toString());
                                    editor.putString("name",dataSnapshot.child("fullName").getValue().toString());
                                    editor.putString("email",email);
                                    editor.commit();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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
                    }
                })
                .setCancelable(false);

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // reads XML
        inflater.inflate(R.menu.menu, menu); // to create
        return super.onCreateOptionsMenu(menu); // the menu
    }

    public void goToSports(View view){
        Intent intent = new Intent(this, SportsListActivity.class);
        startActivity(intent);
    }

    public void goToShows(View view){
        Intent intent = new Intent(this, ShowsActivity.class);
        startActivity(intent);
    }
    public void goToVidGames(View view){
        Intent intent = new Intent(this, VideoGamesListActivity.class);
        startActivity(intent);
    }

    private void writeNewUser(String name, String email, String password) {
        email = email.replace(".", "dot");
        User user = new User(name, email, password);
        mDatabase.child("users").child(email).setValue(user);
    }

    private void storeUserInfo(String email){
        Context context = MainActivity.this;
        SharedPreferences sharedPref = getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        email = email.replace(".","dot");
        editor.putString("email", email);
        editor.commit();

        //mDatabase.addListenerForSingleValueEvent();
    }

    private boolean userValid(String email, final String password){
        DatabaseReference ref = mDatabase.child("users").child(email);
        boolean isValid = false;

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Context context = MainActivity.this;
                //System.out.println("pass is: " + dataSnapshot.child("password").getValue().toString());
                boolean check = dataSnapshot.child("password").getValue().toString().equals(password);
                SharedPreferences sharedPref = getSharedPreferences("sharedPref", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("validLogin",check);
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        Context context = MainActivity.this;
        SharedPreferences sharedPref = getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        isValid = sharedPref.getBoolean("validLogin",false);
        return isValid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
