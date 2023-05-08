package com.example.authapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private int notificationId=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.setbtn).setOnClickListener(this);
        findViewById(R.id.cancelbtn).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_manu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(MainActivity2.this,SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onClick(View view) {

        EditText editText=findViewById(R.id.editText);
        TimePicker timePicker=findViewById(R.id.timePicker);

        Intent intent=new Intent(MainActivity2.this,AlarmReceiver.class);
        intent.putExtra("notificationId",notificationId);
        intent.putExtra("message", editText.getText().toString());

        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                MainActivity2.this,0, intent,PendingIntent.FLAG_CANCEL_CURRENT
        );

        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);



        switch (view.getId()) {
            case R.id.setbtn:
                int hour =timePicker.getCurrentHour();
                int minute =timePicker.getCurrentMinute();
                Calendar startTime =Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND,0);
                long alarmStartTime=startTime.getTimeInMillis();
                alarmManager.set(alarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);

                Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();
                break;


            case R.id.cancelbtn:
                alarmManager.cancel(alarmIntent);
                Toast.makeText(this,"canceled",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}