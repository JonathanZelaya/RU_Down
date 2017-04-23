package jjkk.ru_down;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WaitingRoomActivity extends AppCompatActivity {

    private ArrayList<String> users = new ArrayList<String>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Intent intent = getIntent();
        String sportName = intent.getStringExtra("Category");

        Context context = WaitingRoomActivity.this;

        SharedPreferences sharedPref = context.getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        String user = sharedPref.getString("user",null);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("sports").child(sportName).child("users").child(user).setValue(user);

        TextView header = (TextView) findViewById(R.id.tv_Category);
        header.setText(sportName);

        populateListView();
    }

    private void populateListView(){
        // Create List of Items
        this.users.add("User 1");

        // Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,                   // Context for the activity
                R.layout.usernames,     // Layout to use (Created)
                users);                 // Items to be displayed

        ListView list = (ListView) findViewById(R.id.lv_Users);
        list.setAdapter(adapter);
    }
}
