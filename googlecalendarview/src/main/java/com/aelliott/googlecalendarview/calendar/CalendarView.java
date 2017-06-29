package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CalendarView extends ViewPager
{
    private MonthViewPagerAdapter pagerAdapter;

    public CalendarView(Context context)
    {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        setupDefaultAdapter();
    }

    public void setupDefaultAdapter()
    {
        // Create new pager adapter and set it as the current adapter for the ViewPager
        pagerAdapter = new MonthViewPagerAdapter(getContext());
        setAdapter(pagerAdapter);

        // Set current item to be in the middle of the circular pager adapter
        setCurrentItem(pagerAdapter.getCount() / 2);
    }
}
