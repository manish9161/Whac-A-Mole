package com.kokonetworks.theapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class Field extends LinearLayout {
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final SquareButton[] circles = new SquareButton[9];
    private int currentCircle;
    //private Listener listener;

    //private int score;
    private Mole mole;

    private final int ACTIVE_TAG_KEY = 873374234;

    MutableLiveData<Integer> _liveDataLevel = new MutableLiveData<Integer>();
    LiveData<Integer> levelLiveData = _liveDataLevel;

    MutableLiveData<Integer> _liveDataScore = new MutableLiveData<Integer>();
    LiveData<Integer> scoreLiveData = _liveDataScore;

    MutableLiveData<Boolean> _liveDataGameEnd = new MutableLiveData<Boolean>();
    LiveData<Boolean> gameEndLiveData = _liveDataGameEnd;

    public Field(Context context) {
        super(context);
        initializeViews(context);
    }

    public Field(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public Field(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public Field(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    public int totalCircles() {
        return circles.length;
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_field, this, true);

        circles[0] = (SquareButton) findViewById(R.id.hole1);
        circles[1] = (SquareButton) findViewById(R.id.hole2);
        circles[2] = (SquareButton) findViewById(R.id.hole3);
        circles[3] = (SquareButton) findViewById(R.id.hole4);
        circles[4] = (SquareButton) findViewById(R.id.hole5);
        circles[5] = (SquareButton) findViewById(R.id.hole6);
        circles[6] = (SquareButton) findViewById(R.id.hole7);
        circles[7] = (SquareButton) findViewById(R.id.hole8);
        circles[8] = (SquareButton) findViewById(R.id.hole9);

    }

    private void resetScore() {
        _liveDataScore.setValue(0);
    }

    private void resetEndGame() {
        _liveDataGameEnd.setValue(false);
    }

    public void startGame() {
        resetEndGame();
        resetScore();
        resetCircles();
        for (SquareButton squareButton : circles) {
            squareButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!_liveDataGameEnd.getValue()) {
                        boolean active = (boolean) view.getTag(ACTIVE_TAG_KEY);
                        if (active) {
                            //score += mole.getCurrentLevel() * 2;
                            int score = Integer.valueOf(_liveDataScore.getValue()) + mole.getCurrentLevel() * 2;
                            _liveDataScore.setValue(score);
                        } else {
                            mole.stopHopping();
                            // add this line
                            int score = Integer.valueOf(_liveDataScore.getValue());
                            _liveDataGameEnd.setValue(true);
                            setWrong(view);
                            //listener.onGameEnded(score);
                        }
                    }
                }
            });
        }

        mole = new Mole(this);
        mole.startHopping();
    }

    public int getCurrentCircle() {
        return currentCircle;
    }

    private void resetCircles() {
        for (SquareButton squareButton : circles) {
            squareButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hole_inactive));
            squareButton.setTag(ACTIVE_TAG_KEY, false);
        }
    }

    public void setActive(int index) {
        mainHandler.post(() -> {
            resetCircles();
            circles[index].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hole_active));
            circles[index].setTag(ACTIVE_TAG_KEY, true);
            currentCircle = index;
        });
    }

    public void setWrong(View view) {
        mainHandler.post(() -> {
            view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.hole_wrong));
            view.setTag(ACTIVE_TAG_KEY, true);
        });
    }

    public void onLevelChange(int currentLevel) {
        _liveDataLevel.postValue(currentLevel);
    }


    /*public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onGameEnded(int score);

        void onLevelChange(int level);
    }*/
}