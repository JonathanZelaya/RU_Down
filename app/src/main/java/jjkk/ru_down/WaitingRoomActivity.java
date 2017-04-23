package jjkk.ru_down;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class WaitingRoomActivity extends AppCompatActivity {

    private ArrayList<String> users = new ArrayList<String>();
    private DatabaseReference mDatabase;
    private String sportName;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Intent intent = getIntent();
        sportName = intent.getStringExtra("Category");

        Context context = WaitingRoomActivity.this;

        SharedPreferences sharedPref = context.getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        String email = sharedPref.getString("email",null);
        String name = sharedPref.getString("name",null);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("Child added");
                recreateList(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("child changed");
                recreateList(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("child remove");
                recreateList(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("child move");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("child cancel");
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("sports").child(sportName).child("users");
        ref.addChildEventListener(childEventListener);
        mDatabase.child("sports").child(sportName).child("users").child(email).setValue(name);

        TextView header = (TextView) findViewById(R.id.tv_Category);
        header.setText(sportName);

        adapter = new ArrayAdapter<String>(
                this,                   // Context for the activity
                R.layout.usernames,     // Layout to use (Created)
                users);                 // Items to be displayed

        ListView list = (ListView) findViewById(R.id.lv_Users);
        list.setAdapter(adapter);

        //populateListView();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Context context = WaitingRoomActivity.this;

        SharedPreferences sharedPref = context.getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        String email = sharedPref.getString("email",null);
        String name = sharedPref.getString("name",null);
        mDatabase.child("sports").child(sportName).child("users").child(email).setValue(null);
    }

    private void recreateList(DataSnapshot ds){
        users.clear();
        ArrayList<String> newList = new ArrayList<String>();
        Iterator<DataSnapshot> iterator = ds.getChildren().iterator();
        while(iterator.hasNext()){
            newList.add(iterator.next().getValue().toString());
            System.out.println("value : " + iterator.next().getValue().toString());
        }
        users.addAll(newList);
        adapter.notifyDataSetChanged();

    }

    private void populateListView(){
        // Create List of Items

        // Build Adapter
        adapter = new ArrayAdapter<String>(
                this,                   // Context for the activity
                R.layout.usernames,     // Layout to use (Created)
                users);                 // Items to be displayed

        ListView list = (ListView) findViewById(R.id.lv_Users);
        list.setAdapter(adapter);
    }
}
