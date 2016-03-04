package com.example.rober.stopwatch;

import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView timeTxt;
    private Button startPauseBtn;
    private Button resetBtn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> times;

    private long startTime;
    private long elapsedTime;

    private long hours, minutes,secs,milli;

    private Handler handler;

    boolean isStop, isRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        timeTxt = (TextView)findViewById(R.id.timeTxtView);
        startPauseBtn = (Button)findViewById(R.id.StartStopBtn);
        resetBtn = (Button)findViewById(R.id.ResetBtn);
        list = (ListView)findViewById(R.id.timesListView);

        //List
        times = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, times);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                times.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        //Handler
        handler = new Handler();

        //Flags
        isStop = false;
        isRunning = false;

    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            elapsedTime = System.currentTimeMillis() - startTime;

            minutes = (elapsedTime / 60000) % 60;
            secs = (elapsedTime / 1000) % 60;
            milli = elapsedTime % 1000;

            String time = String.format("%02d:%02d.%03d", minutes, secs, milli);

            timeTxt.setText(time);
            handler.postDelayed(this,10);
        }
    };

    public void StartPauseBtn(View view){


        //Initial (Isn't stop and isn't running)
        if(!isStop && !isRunning){
            Log.d("Estado", "Initial.");
            isRunning = true;
            startPauseBtn.setText("Pause");
            startTime = System.currentTimeMillis();
            handler.post(myRunnable);
        }else if(isRunning) {                           //If is Running and press the button
            Log.d("Estado", "Está corriendo.");
            isRunning = false;
            isStop = true;
            startPauseBtn.setText("Start");
            handler.removeCallbacks(myRunnable);
        } else if (isStop){                             //If is Stopped and press the button
            Log.d("Estado", "Está stop.");
            isRunning = true;
            isStop = false;
            startPauseBtn.setText("Start");
            handler.post(myRunnable);
        }

    }

    public void ResetBtn(View view){
        startTime = 0;
        elapsedTime = 0;
        isStop = false;
        isRunning = false;
        startPauseBtn.setText("Start");
        handler.removeCallbacks(myRunnable);
        timeTxt.setText("00:00.000");
        times.add(String.format("%02d:%02d.%03d", this.minutes, this.secs, this.milli));
        adapter.notifyDataSetChanged();
    }


}
