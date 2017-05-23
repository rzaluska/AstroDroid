package io.github.luxurypro.astrodroid;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public abstract class RiseSetInfoActivity extends AppCompatActivity {
    protected LocationProviderService locationProviderService;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            locationProviderService = ((LocationProviderService.LocalBinder) service).getService();
            if (!locationProviderService.isEnabled()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RiseSetInfoActivity.this);
                builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
            updateFields();
        }

        public void onServiceDisconnected(ComponentName className) {
            locationProviderService = null;
        }
    };
    private boolean mIsBoundToService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rise_set_info);
        doSetBackground();
    }

    protected abstract void doSetBackground();

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
        setTitle();

    }

    public void setTitle() {
        TextView title = (TextView) findViewById(R.id.name);
        doUpdateTitle(title);
    }

    public abstract void doUpdateTitle(TextView title);

    @Override
    protected void onPause() {
        super.onPause();
        doUnbindService();
    }

    void doBindService() {
        bindService(new Intent(RiseSetInfoActivity.this, LocationProviderService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBoundToService = true;
    }


    void doUnbindService() {
        if (mIsBoundToService) {
            unbindService(mConnection);
            mIsBoundToService = false;
        }
    }

    void updateFields() {
        TextView rise = (TextView) findViewById(R.id.rise);
        TextView transit = (TextView) findViewById(R.id.transit);
        TextView set = (TextView) findViewById(R.id.set);
        doUpdateFields(rise, transit, set);
    }

    public abstract void doUpdateFields(TextView rise, TextView transit, TextView set);
}
