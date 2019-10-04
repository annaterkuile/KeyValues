package com.example.keyvalues.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.keyvalues.R;
import android.os.CountDownTimer;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private TextView timerText;
    private Button countdownbutton;
    private CountDownTimer timer;
    private long timeLeftInMilliseconds = 10000;
    private boolean timerRunning;

    private ImageButton button;
    private int highscore;
    private int defaultValue;
    private int timesPressed;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        timerText = root.findViewById(R.id.timerText);
        countdownbutton = root.findViewById(R.id.countdownbutton);

        countdownbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();

            }
        });

        button = root.findViewById(R.id.mash_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                defaultValue = getResources().getInteger(R.integer.saved_times_pressed_default_key);
                timesPressed = sharedPref.getInt(getString(R.string.saved_button_press_count_key), defaultValue);

                int newTimesPressed = timesPressed + 1;

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.saved_button_press_count_key), newTimesPressed);
                editor.commit();

                // be amazing, do something
                textView.setText("Button has been pressed " + Integer.toString(newTimesPressed)+ " times!");
            }
        });

        updateTimer();
        return root;
    }

    public void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer() {
        timer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        } .start();

        countdownbutton.setText("PAUSE");
        timerRunning = true;
    }

    public void stopTimer() {
        timer.cancel();
        countdownbutton.setText("START");
        timerRunning = false;
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds/ 600000;
        int seconds = (int) timeLeftInMilliseconds % 600000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText+= 0;
        timeLeftText += seconds;

        timerText.setText(timeLeftText);
    }
}


