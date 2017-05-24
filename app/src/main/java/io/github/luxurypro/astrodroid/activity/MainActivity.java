package io.github.luxurypro.astrodroid.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.github.luxurypro.astrodroid.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "io.github.luxurypro.astrodroid.MESSAGE";
    public static final int MY_PRESMISSIO_REQUEST_LOCATION = 1;
    public static boolean permission_granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PRESMISSIO_REQUEST_LOCATION);
        } else {
            permission_granted = true;
        }
    }

    public void runSkyView(View view) {
        startActivity(new Intent(this, SkyActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                goToSettings(null);
                return true;
            case R.id.about:
                showAboutDialog(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showAboutDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");

        final String aboutDialogText = "\n\nAstroDroid v1.0\nAD 2017";
        TextView textView = new TextView(this);
        textView.setText(aboutDialogText);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        builder.setView(textView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void goToSettings(View v) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    public void sunInfo(View v) {
        Intent sunInfo = new Intent(this, SunRiseSetInfoActivity.class);
        startActivity(sunInfo);
    }

    public void moonInfo(View v) {
        Intent moonInfo = new Intent(this, MoonRiseSetInfoActivity.class);
        startActivity(moonInfo);
    }

    public void timeInfoActivity(View v) {
        Intent timeInfo = new Intent(this, LocalTimeInfoActivity.class);
        startActivity(timeInfo);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PRESMISSIO_REQUEST_LOCATION: {
                permission_granted = grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                if (!permission_granted) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    prefs.edit().putBoolean("manual_location", true).apply();
                    Toast.makeText(MainActivity.this, "Permission for location denied :(", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
