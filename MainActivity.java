package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timeView;
    private Button startBtn, pauseBtn, resetBtn;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeBuffer = 0L;
    private long updateTime = 0L;
    private boolean isRunning = false;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis() - startTime;
            updateTime = timeBuffer + currentTime;

            int seconds = (int) (updateTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int millis = (int) (updateTime % 1000) / 10;

            String formattedTime = String.format("%02d:%02d:%02d", minutes, seconds, millis);
            timeView.setText(formattedTime);

            handler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.time_view);
        startBtn = findViewById(R.id.start_button);
        pauseBtn = findViewById(R.id.pause_button);
        resetBtn = findViewById(R.id.reset_button);

        startBtn.setOnClickListener(v -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(runnable, 0);
                isRunning = true;
            }
        });

        pauseBtn.setOnClickListener(v -> {
            if (isRunning) {
                timeBuffer += System.currentTimeMillis() - startTime;
                handler.removeCallbacks(runnable);
                isRunning = false;
            }
        });

        resetBtn.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            startTime = 0L;
            timeBuffer = 0L;
            updateTime = 0L;
            isRunning = false;
            timeView.setText("00:00:00");
        });
    }
}
