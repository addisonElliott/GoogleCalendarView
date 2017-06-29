package com.aelliott.samplegooglecalendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aelliott.googlecalendarview.calendar.CalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Testing extends AppCompatActivity
{
    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        // Initialize all variables annotated with @BindView and other variants
        ButterKnife.bind(this);

        //MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)calendarView.getAdapter();
        //adapter.setMonthViewLayout(R.layout.month_view, calendarView);
        calendarView.setMonthViewLayout(R.layout.month_view);
    }
}
