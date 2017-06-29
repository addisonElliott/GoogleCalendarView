package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.LocalDate;

public class MonthViewPagerAdapter extends PagerAdapter
{
    /**
     * Number of items to buffer at one time.
     * <p>
     * This number ideally is odd to allow for an even amount of months to the left and right of the
     * active item.
     */
    private static final int ITEM_COUNT = 5;

    private Context context;

    public MonthViewPagerAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        // Create new MonthView object
        MonthView view = new MonthView(context);
        view.setDisplayMonthDate(LocalDate.parse("2017-03-03"));

        // Add the view to the container, which is the ViewPager in this case
        container.addView(view);

        // Return the view as the Object, which is used to identify the object typically (besides position)
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        MonthView view = (MonthView)object;

        container.removeView(view);
    }

    @Override
    public int getCount()
    {
        return ITEM_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        // In this case, the Object is the item that is returned from instantiateItem (which does not
        // always have to be the view), but in this case the view is equal to the object. So, the view
        // is from a particular object if they are equal to each other
        return (view == object);
    }

    public Context getContext()
    {
        return context;
    }
}
