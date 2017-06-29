package com.aelliott.samplegooglecalendarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aelliott.googlecalendarview.calendar.CalendarView;

import org.threeten.bp.DayOfWeek;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        calendarView.setMonthViewLayout(R.layout.month_view);
        calendarView.setMonthViewHeaderLayout(R.layout.month_view_item_header);
    }

    @OnClick(R.id.button1)
    public void button1_onClick(View view)
    {
        //calendarView.setMonthViewLayout(R.layout.month_view);
        calendarView.setStartDayOfWeek(DayOfWeek.WEDNESDAY);
        calendarView.setDayOfWeekDisplay(CalendarView.DAY_WEEK_DISPLAY_BRIEF);
    }
}
