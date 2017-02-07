package com.example.owner.rover_viewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void pickRover(View view) {

        Intent roverIntent = new Intent(this, DateSelect.class);

        switch (view.getId()) {
            case (R.id.spiritButt):
                roverIntent.putExtra("roverID","Spirit");
                startActivity(roverIntent);
                break;
            case (R.id.opportunityButt):
                roverIntent.putExtra("roverID","Opportunity");
                startActivity(roverIntent);
                break;
            case (R.id.curiosityButt):
                roverIntent.putExtra("roverID","Curiosity");
                startActivity(roverIntent);
                break;
        }

    }
}
