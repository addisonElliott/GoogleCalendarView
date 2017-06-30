package com.aelliott.googlecalendarview;

import android.view.View;
import android.widget.TextView;

import com.aelliott.googlecalendarview.calendar.CalendarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Coordinator utility that synchronizes widgets as selected date changes
 */
public class Coordinator
{
    private TextView titleTextView;
    private CalendarView calendarView;
    //private AgendaView agendaView;

    public Coordinator(TextView titleTextView, CalendarView calendarView)
    {
        this.titleTextView = titleTextView;
        this.calendarView = calendarView;
    }

    void reset()
    {

    }

    private void sync(long dayMillis, View originator)
    {

    }

    private void updateTitle(LocalDate date)
    {
        if (titleTextView != null)
            titleTextView.setText(date.format(DateTimeFormatter.ofPattern("MMMM YYYY")));
    }

    public TextView getTitleTextView()
    {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView)
    {
        this.titleTextView = titleTextView;
    }

    public CalendarView getCalendarView()
    {
        return calendarView;
    }

    public void setCalendarView(CalendarView calendarView)
    {
        this.calendarView = calendarView;
    }
}