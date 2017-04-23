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
import com.google.firebase.database.ValueEventListener;

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
        String phone = sharedPref.getString("phone",null);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("Child added");
                /*DatabaseReference refUsers = mDatabase.child("users");
                refUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userdb) {
                        final String guysPhone = userdb.child(dataSnapshot.getKey()).child("phoneNumber").toString();
                        users.add(dataSnapshot.getValue().toString() + "\t" + "#: " + guysPhone);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });*/
                users.add(dataSnapshot.getKey().toString());
                recreateList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("child changed");
                recreateList();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("child remove");
                users.remove(dataSnapshot.getKey().toString());
                recreateList();
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

    private void recreateList(){
        //users.clear();
        //ArrayList<String> newList = new ArrayList<String>();
        /*final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sports/Basketball/users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*Iterator<DataSnapshot> iterator = ds.getChildren().iterator();
        System.out.println("before the while");
        while(iterator.hasNext()){
            System.out.println("inside the while");
            newList.add(iterator.next().getValue().toString());
            System.out.println("value : " + iterator.next().getValue().toString());
        }*/
        //users.addAll(newList);
        adapter.notifyDataSetChanged();

    }

    private void populateListView(){
        // Create List of Items

        // Build Adapter
        adapter = new ArrayAdapter<String>(
                this,                   // Context for the activity
                android.R.layout.simple_list_item_1,     // Layout to use (Created)
                users);                 // Items to be displayed

        ListView list = (ListView) findViewById(R.id.lv_Users);
        list.setAdapter(adapter);
    }
}
