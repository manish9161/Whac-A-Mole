package com.kokonetworks.theapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;


public class MainActivity extends AppCompatActivity {

    private Field field;
    private TextView tvLevel;
    private TextView tvScore;

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        tvLevel = findViewById(R.id.tvLevel);
        btnStart = findViewById(R.id.btnStart);
        tvScore = findViewById(R.id.tvScore);

        setEventListeners();
    }

    void setEventListeners(){
        btnStart.setOnClickListener(view -> {
            btnStart.setVisibility(View.GONE);
            //tvScore.setVisibility(View.GONE);
            field.startGame();
        });

        //field.setListener(listener);

        observeField();
    }

    private void observeField() {

        field.levelLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer level) {
                Log.e("level", "level" + level);
                tvLevel.setText(String.format(getString(R.string.level), level));
            }
        });

        field.scoreLiveData.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer score) {
                Log.e("score", "score" + score);
                tvScore.setText(String.format(getString(R.string.your_score), score));
            }
        });

        field.gameEndLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean gameEnd) {
                if(gameEnd) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.GONE);
                }
            }
        });
    }

    /*private final Field.Listener listener = new Field.Listener() {

        @Override
        public void onGameEnded(int score) {
            btnStart.setVisibility(View.VISIBLE);
            tvScore.setVisibility(View.VISIBLE);
            //tvScore.setText(String.format(getString(R.string.your_score), score));
        }

        @Override
        public void onLevelChange(int level) {
            tvLevel.setText(String.format(getString(R.string.level), level));
        }
    };*/
}