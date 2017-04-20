package io.github.luxurypro.astrodroid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class DateTimePickerDialog extends Dialog implements View.OnClickListener {
    private TimePicker timePicker;
    private DatePicker datePicked;
    private double julianDate;

    public DateTimePickerDialog(@NonNull Context context) {
        super(context);
    }

    public DateTimePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DateTimePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_layout);
        setTitle("Select date and time");
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicked = (DatePicker) findViewById(R.id.datePicker);
    }

    double getJulianDate() {
        return julianDate;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
        } else if (v.getId() == R.id.ok) {
        }
        this.dismiss();
    }
}


