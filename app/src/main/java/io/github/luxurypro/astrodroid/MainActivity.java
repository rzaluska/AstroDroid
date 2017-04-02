package io.github.luxurypro.astrodroid;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "io.github.luxurypro.astrodroid.MESSAGE";
    private Handler someHandler;
    private TextView jDateField;

    public MainActivity() {
        this.someHandler = new Handler();
    }

    private TextView dateField;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.v(TAG, "Running loop");
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            double julianDay = DateUtil.toJulianDay(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
            dateField.setText(currentDateTimeString);
            jDateField.setText(String.format(new Locale("PL"), "%f", julianDay));
            someHandler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateField = (TextView) this.findViewById(R.id.data);
        jDateField = (TextView) this.findViewById(R.id.dataJ);
        someHandler.post(runnable);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        someHandler.removeCallbacks(this.runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        someHandler.post(this.runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        someHandler.removeCallbacks(this.runnable);
        someHandler = null;
    }
}
