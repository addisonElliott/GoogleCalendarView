package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aelliott.googlecalendarview.R;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class CalendarView extends ViewPager
{
    /**
     * Callback interface for calendar view change events
     */
    public interface OnChangeListener
    {
        /**
         * Fired when selected day has been changed via UI interaction
         *
         * @param date new day that was selected day
         */
        void onSelectedDayChange(LocalDate date);
    }

    /**
     * @hide
     */
    @IntDef({DAY_WEEK_DISPLAY_NARROW, DAY_WEEK_DISPLAY_BRIEF, DAY_WEEK_DISPLAY_FULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DayOfWeekDisplay {}

    public static final int DAY_WEEK_DISPLAY_NARROW = 0;
    public static final int DAY_WEEK_DISPLAY_BRIEF = 1;
    public static final int DAY_WEEK_DISPLAY_FULL = 2;

    private Locale locale = Locale.US;
    private DayOfWeek startDayOfWeek = DayOfWeek.SUNDAY;
    @DayOfWeekDisplay
    private int dayOfWeekDisplay = DAY_WEEK_DISPLAY_NARROW;
    @LayoutRes
    private int monthViewHeaderLayout = 0;
    @LayoutRes
    private int monthViewCellLayout = 0;

    private OnChangeListener onChangeListener;
    private final OnMonthViewChange onMonthViewChange = new OnMonthViewChange();

    public CalendarView(Context context)
    {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try
        {
            a.getInteger(R.styleable.CalendarView_startDayOfWeek, 1);
            a.getInteger(R.styleable.CalendarView_dayOfWeekDisplay, 0);
        }
        finally
        {
            a.recycle();
        }

        setupDefaultAdapter();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)getAdapter();

        View child = adapter.getView(getCurrentItem());
        if (child != null)
        {
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int height = child.getMeasuredHeight();
            setMeasuredDimension(getMeasuredWidth(), height);

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setupDefaultAdapter()
    {
        // Create new pager adapter and set it as the current adapter for the ViewPager
        MonthViewPagerAdapter adapter = new MonthViewPagerAdapter(getContext(), onMonthViewChange);
        setAdapter(adapter);

        // Set current item to be in the middle of the circular pager adapter
        setCurrentItem(adapter.getCount() / 2);

        addOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                Log.v("CTL", "On Page Selected: " + position);
                // Get adapter and the month view of the selected page. If the selected page has a
                // null view, then return because it means the page has not been created yet
                MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)getAdapter();

                // Get old month view and set selected position to invalid
                // This is useful so that when the user comes back there is a onChangeSelectedDay
                // event fired since the old selected day will NOT equal new selected day
                MonthView oldView = adapter.getView(getCurrentItem());
                if (oldView != null)
                    oldView.setSelectedPosition(MonthView.POSITION_INVALID);

                // Get new month view
                MonthView newView = adapter.getView(position);
                if (newView == null)
                    return;

                Log.v("CTL", "On Page Selected Finished: " + position);

                // Set new page to the first of the month
                newView.setSelectedDay(1);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                // If page state changed to idle, then it just finished switching pages
                if (state == SCROLL_STATE_IDLE)
                {

                }
            }
        });
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;

        for (int i = 0; i < getChildCount(); ++i)
        {
            MonthView view = (MonthView)getChildAt(i);

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

        for (int i = 0; i < getChildCount(); ++i)
        {
            MonthView view = (MonthView)getChildAt(i);

            view.setStartDayOfWeek(startDayOfWeek);
        }
    }

    @DayOfWeekDisplay
    public int getDayOfWeekDisplay()
    {
        return dayOfWeekDisplay;
    }

    public void setDayOfWeekDisplay(@DayOfWeekDisplay int dayOfWeekDisplay)
    {
        this.dayOfWeekDisplay = dayOfWeekDisplay;

        for (int i = 0; i < getChildCount(); ++i)
        {
            MonthView view = (MonthView)getChildAt(i);

            view.setDayOfWeekDisplay(dayOfWeekDisplay);
        }
    }

    public void setMonthViewLayout(@LayoutRes int monthViewLayout)
    {
        MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)getAdapter();

        // Since this operation is a bit expensive, only do something if the monthViewLayout changes
        if (adapter.getMonthViewLayout() == monthViewLayout)
            return;

        adapter.setMonthViewLayout(monthViewLayout);

        // Force refresh of all items in adapter. Each item should be deleted and re-instantiate will
        // be called
        int oldCurrentItem = getCurrentItem();
        setAdapter(getAdapter());
        setCurrentItem(oldCurrentItem);
    }

    @LayoutRes
    public int getMonthViewHeaderLayout()
    {
        return monthViewHeaderLayout;
    }

    public void setMonthViewHeaderLayout(@LayoutRes int monthViewHeaderLayout)
    {
        this.monthViewHeaderLayout = monthViewHeaderLayout;

        for (int i = 0; i < getChildCount(); ++i)
        {
            MonthView view = (MonthView)getChildAt(i);

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

        for (int i = 0; i < getChildCount(); ++i)
        {
            MonthView view = (MonthView)getChildAt(i);

            view.setCellLayout(monthViewCellLayout);
        }
    }

    public void setOnChangeListener(OnChangeListener onChangeListener)
    {
        this.onChangeListener = onChangeListener;
    }

    class OnMonthViewChange implements MonthView.OnChangeListener
    {
        @Override
        public void onSelectedDayChange(LocalDate date)
        {
            if (date != null && onChangeListener != null)
                onChangeListener.onSelectedDayChange(date);
        }
    }
}
