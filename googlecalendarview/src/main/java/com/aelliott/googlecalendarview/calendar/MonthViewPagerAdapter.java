package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MonthViewPagerAdapter extends PagerAdapter
{
    public interface OnCreateViewListener
    {
        /**
         * Fired when a new MonthView is instantiated
         * <p>
         * Useful for adding custom styles and item decorations to the view before adding to the calendar view
         *
         * @param view new view that was created
         */
        void onCreateView(MonthView view);
    }

    /**
     * Number of items to buffer at one time.
     * <p>
     * This number ideally is odd to allow for an even amount of months to the left and right of the
     * active item.
     */
    private static final int ITEM_COUNT = 5;

    private Context context;
    private final List<LocalDate> months = new ArrayList<>(getCount());
    private final List<MonthView> views = new ArrayList<>(getCount());
    private OnCreateViewListener createViewListener;
    @LayoutRes
    private int monthViewLayout = 0;
    private MonthView.OnChangeListener onChangeListener;

    public MonthViewPagerAdapter(Context context)
    {
        this(context, null, null);
    }

    public MonthViewPagerAdapter(Context context, MonthView.OnChangeListener onChangeListener)
    {
        this(context, null, onChangeListener);
    }

    public MonthViewPagerAdapter(Context context, LocalDate startDate,
            MonthView.OnChangeListener onChangeListener)
    {
        this.context = context;
        this.onChangeListener = onChangeListener;

        if (startDate == null)
            startDate = LocalDate.now();

        int mid = ITEM_COUNT / 2;
        startDate = startDate.withDayOfMonth(1);
        for (int i = 0; i < ITEM_COUNT; ++i)
        {
            months.add(startDate.plusMonths(i - mid));
            views.add(null);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        CalendarView parent = (CalendarView)container;
        MonthView view;

        // If a layout resource is given for the month view, inflate that, otherwise create default
        // MonthView
        if (monthViewLayout != 0)
        {
            LayoutInflater inflater = LayoutInflater.from(context);

            view = (MonthView)inflater.inflate(monthViewLayout, container, false);
        }
        else
        {
            view = new MonthView(context);
        }

        // Synchronize settings in adapter to the new view
        view.setLocale(parent.getLocale());
        view.setStartDayOfWeek(parent.getStartDayOfWeek());
        view.setDayOfWeekDisplay(parent.getDayOfWeekDisplay());
        view.setHeaderLayout(parent.getMonthViewHeaderLayout());
        view.setCellLayout(parent.getMonthViewCellLayout());
        view.setOnChangeListener(onChangeListener);
        view.setDisplayMonthDate(months.get(position));

        // Notify user that a new view has been created so they can add any styles
        // or line decorations
        if (createViewListener != null)
            createViewListener.onCreateView(view);

        // Set view to its current position
        // The order of the views added is not the order of the position necessarily
        views.set(position, view);

        // Add the view to the container, which is the ViewPager in this case
        container.addView(view);
        bind(position);

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

    void bind(int position)
    {
        if (views.get(position) != null)
            views.get(position).setDisplayMonthDate(months.get(position));
    }

    public Context getContext()
    {
        return context;
    }

    public MonthView getView(int position)
    {
        return views.get(position);
    }

    @LayoutRes
    public int getMonthViewLayout()
    {
        return monthViewLayout;
    }

    public void setMonthViewLayout(@LayoutRes int monthViewLayout)
    {
        this.monthViewLayout = monthViewLayout;
    }

    /**
     * Sets listener to be notified upon creation of new MonthView class
     *
     * @param listener listener to be notified
     */
    public void setOnCreateViewListener(OnCreateViewListener listener)
    {
        createViewListener = listener;
    }
}
