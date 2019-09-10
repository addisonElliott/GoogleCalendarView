package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.aelliott.googlecalendarview.calendar.CalendarView.DAY_WEEK_DISPLAY_NARROW;

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

    private Locale locale = Locale.US;
    private DayOfWeek startDayOfWeek = DayOfWeek.SUNDAY;
    @CalendarView.DayOfWeekDisplay
    private int dayOfWeekDisplay = DAY_WEEK_DISPLAY_NARROW;
    @LayoutRes
    private int monthViewHeaderLayout = 0;
    @LayoutRes
    private int monthViewCellLayout = 0;

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

        // The startDate specifies the beginning date, including the day of the month to select initially
        // The month and year portion is to initialize the view and upon initialization, the day of the
        // month for the currently selected month view is set to that selected day
        int mid = ITEM_COUNT / 2;
        for (int i = 0; i < ITEM_COUNT; ++i)
        {
            months.add(startDate.plusMonths(i - mid));
            views.add(null);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        // Convert virtual to actual position. (Special functionality for infinite scrolling)
        position = virtualToActualPosition(position);

        CalendarView parent = (CalendarView)container;
        MonthView view = views.get(position);

        // If the view already exists in the list, then add to container and return that
        if (view != null)
        {
            //container.removeAllViewsInLayout();
            if (view.getParent() == null)
                container.addView(view);

            view.setTag((int)view.getTag() + 1);
            return view;
        }

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
        // Only pays attention to month and year of the date, the day of the year does not matter here
        view.setDisplayMonthDate(months.get(position));
        view.setTag(1);

        // If this is the currently selected item upon startup, then set the selected day to the day
        // number in the months variable
        // NOTE: This is the only time when the day of the month of the months list is used
        if (parent.getCurrentItem() == position)
            view.setSelectedDay(months.get(position).getDayOfMonth());

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
        // Convert virtual to actual position. (Special functionality for infinite scrolling)
        position = virtualToActualPosition(position);

        MonthView view = (MonthView)object;

        view.setTag((int)view.getTag() - 1);

        if ((int)view.getTag() == 0)
            container.removeView(view);
    }

    @Override
    public int getCount()
    {
        // Add two to the actual count so that you can duplicate the first and last views at the
        // front and back of the list. Allows for smooth scrolling
        if (getRealCount() == 0)
            return 0;
        else
            return getRealCount() + 2;
    }

    /**
     * @return the actual number of items in the adapter
     */
    public int getRealCount()
    {
        return ITEM_COUNT;
    }

    public int virtualToActualPosition(int position)
    {
        if (getRealCount() == 0)
            return 0;

        // Maps virtual position to actual position
        // Virtual position is 0 -> getRealCount() + 1
        // Actual position is 0 -> getRealCount() - 1
        // First of virtual position maps to last of actual
        // Last of virtual position maps to first of actual
        // This algorithm takes the position, subtracts one and then wraps it around so it does that
        return ((position - 1) + getRealCount()) % getRealCount();
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
        return getView(position, true);
    }

    public MonthView getView(int position, boolean actual)
    {
        if (!actual)
            position = virtualToActualPosition(position);

        return views.get(position);
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

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;

        for (int i = 0; i < getRealCount(); ++i)
        {
            MonthView view = getView(i);

            if (view != null)
                view.setLocale(locale);
        }
    }

    public DayOfWeek getStartDayOfWeek()
    {
        return startDayOfWeek;
    }

    public void setStartDayOfWeek(DayOfWeek startDayOfWeek)
    {
        this.startDayOfWeek = startDayOfWeek;

        for (int i = 0; i < getRealCount(); ++i)
        {
            MonthView view = getView(i);

            if (view != null)
                view.setStartDayOfWeek(startDayOfWeek);
        }
    }

    @CalendarView.DayOfWeekDisplay
    public int getDayOfWeekDisplay()
    {
        return dayOfWeekDisplay;
    }

    public void setDayOfWeekDisplay(@CalendarView.DayOfWeekDisplay int dayOfWeekDisplay)
    {
        this.dayOfWeekDisplay = dayOfWeekDisplay;

        for (int i = 0; i < getRealCount(); ++i)
        {
            MonthView view = getView(i);

            if (view != null)
                view.setDayOfWeekDisplay(dayOfWeekDisplay);
        }
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

    @LayoutRes
    public int getMonthViewHeaderLayout()
    {
        return monthViewHeaderLayout;
    }

    public void setMonthViewHeaderLayout(@LayoutRes int monthViewHeaderLayout)
    {
        this.monthViewHeaderLayout = monthViewHeaderLayout;

        for (int i = 0; i < getRealCount(); ++i)
        {
            MonthView view = getView(i);

            if (view != null)
                view.setHeaderLayout(monthViewHeaderLayout);
        }
    }

    @LayoutRes
    public int getMonthViewCellLayout()
    {
        return monthViewCellLayout;
    }

    public void setMonthViewCellLayout(@LayoutRes int monthViewCellLayout)
    {
        this.monthViewCellLayout = monthViewCellLayout;

        for (int i = 0; i < getRealCount(); ++i)
        {
            MonthView view = getView(i);

            if (view != null)
                view.setCellLayout(monthViewCellLayout);
        }
    }
}
