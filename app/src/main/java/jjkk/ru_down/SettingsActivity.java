package jjkk.ru_down;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPref = getSharedPreferences("sharedPref", SettingsActivity.this.MODE_PRIVATE);
        String campus = sharedPref.getString("campus",null);
        String gender = sharedPref.getString("gender",null);
        String name = sharedPref.getString("name",null);
        String phone = sharedPref.getString("phone",null);

        Spinner campusSpinner = (Spinner) findViewById(R.id.spin);
        Spinner genderSpinner = (Spinner) findViewById(R.id.spinner2);
        EditText displayName = (EditText) findViewById(R.id.editText);
        EditText phoneNumber = (EditText) findViewById(R.id.editText2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Campus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(adapter);
        if (!campus.equals(null)) {
            int spinnerPosition = adapter.getPosition(campus);
            campusSpinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter2);
        if (!gender.equals(null)) {
            int spinnerPosition = adapter2.getPosition(gender);
            genderSpinner.setSelection(spinnerPosition);
        }

        displayName.setText(name);
        phoneNumber.setText(phone);
    }

    public void save(View view){
        Context context = SettingsActivity.this;
        SharedPreferences sharedPref = context.getSharedPreferences("sharedPref", context.MODE_PRIVATE);
        String email = sharedPref.getString("email","Not found");

        Spinner campus = (Spinner) findViewById(R.id.spin);
        Spinner gender = (Spinner) findViewById(R.id.spinner2);
        EditText displayName = (EditText) findViewById(R.id.editText);
        EditText phoneNumber = (EditText) findViewById(R.id.editText2);

        String campusString = campus.getSelectedItem().toString();
        String genderString = gender.getSelectedItem().toString();
        String name = displayName.getText().toString();
        String phone = phoneNumber.getText().toString();

        mDatabase.child("users").child(email).child("preferredCampus").setValue(campusString);
        mDatabase.child("users").child(email).child("preferredGender").setValue(genderString);
        mDatabase.child("users").child(email).child("fullName").setValue(name);
        mDatabase.child("users").child(email).child("phoneNumber").setValue(phone);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("campus",campusString);
        editor.putString("gender",genderString);
        editor.putString("name",name);
        editor.putString("phone",phone);
        editor.commit();

        finish();

    }
}
