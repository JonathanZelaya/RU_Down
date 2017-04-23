package jjkk.ru_down;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VideoGamesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_games_list);

    }

    public void enterLobby(View button){
        Intent intent = new Intent(this, WaitingRoomActivity.class);
        Button b = (Button) button;
        String sport = b.getText().toString();
        intent.putExtra("Category",sport);
        startActivity(intent);
    }
}
