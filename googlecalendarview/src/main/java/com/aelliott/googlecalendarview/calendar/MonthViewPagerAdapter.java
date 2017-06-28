package com.aelliott.googlecalendarview.calendar;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public class MonthViewPagerAdapter extends PagerAdapter
{
    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return false;
    }
}
