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

        // TODO Get attributes
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

        View child = adapter.getView(getCurrentItem(), true);
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
        setCurrentItem(adapter.getRealCount() / 2);
        //setOffscreenPageLimit(6);

        addOnPageChangeListener(new OnPageChangeListener()
        {
            private int scrollState;

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


                if (position == 0)
                {
                    setCurrentItem(adapter.getRealCount() - 1, false);
                    Log.v("CTL", "Set current item to last");
                }
                else if (position == adapter.getCount() - 1)
                {
                    setCurrentItem(0, false);
                    Log.v("CTL", "Set current item to 0");
                }



                // Convert virtual to actual position. (Special functionality for infinite scrolling)
                position = adapter.virtualToActualPosition(position);

                // With the virtual position converted to actual, check if the previous and new positions
                // are the same, if so do nothing
                //if (position == getCurrentItem())
                //    return;

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

                // Select the first day of the new month to show
                newView.setSelectedDay(1);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                if (state == ViewPager.SCROLL_STATE_IDLE)
                {
                    if (scrollState != ViewPager.SCROLL_STATE_SETTLING)
                    {
                        //handleSetNextItem();
                    }
                }

                scrollState = state;
            }

            private void handleSetNextItem()
            {
                /*final int lastPosition = getAdapter().getRealCount() - 1;
                if (getCurrentItem() == 0)
                {
                    setCurrentItem(lastPosition, false);
                }
                else if (getCurrentItem() == lastPosition)
                {
                    setCurrentItem(0, false);
                }*/
            }
        });
    }

    public void syncPages(int position)
    {
        // Swiping left if the new position is LESS THAN the current position
        boolean swipeLeft = (position < getCurrentItem());
        if (position == 0)
        {

        }
    }

    @Override
    public void setCurrentItem(int item)
    {
        // Offset the current item to ensure there is space to scroll
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll)
    {
        MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)getAdapter();

        if (adapter.getRealCount() == 0)
        {
            super.setCurrentItem(item, smoothScroll);
        }
        else
        {
            // The item number is incremented by one because there is the first page that is
            // actually the last page
            super.setCurrentItem(item + 1);
        }
    }

    @Override
    public int getCurrentItem()
    {
        MonthViewPagerAdapter adapter = (MonthViewPagerAdapter)getAdapter();

        if (adapter.getRealCount() == 0)
            return super.getCurrentItem();
        else
            return (adapter.virtualToActualPosition(super.getCurrentItem()));
    }

    public Locale getLocale()
    {
        return ((MonthViewPagerAdapter)getAdapter()).getLocale();
    }

    public void setLocale(Locale locale)
    {
        ((MonthViewPagerAdapter)getAdapter()).setLocale(locale);
    }

    public DayOfWeek getStartDayOfWeek()
    {
        return ((MonthViewPagerAdapter)getAdapter()).getStartDayOfWeek();
    }

    public void setStartDayOfWeek(DayOfWeek startDayOfWeek)
    {
        ((MonthViewPagerAdapter)getAdapter()).setStartDayOfWeek(startDayOfWeek);
    }

    @DayOfWeekDisplay
    public int getDayOfWeekDisplay()
    {
        return ((MonthViewPagerAdapter)getAdapter()).getDayOfWeekDisplay();
    }

    public void setDayOfWeekDisplay(@DayOfWeekDisplay int dayOfWeekDisplay)
    {
        ((MonthViewPagerAdapter)getAdapter()).setDayOfWeekDisplay(dayOfWeekDisplay);
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
        setAdapter(adapter);
        setCurrentItem(oldCurrentItem);
    }

    @LayoutRes
    public int getMonthViewHeaderLayout()
    {
        return ((MonthViewPagerAdapter)getAdapter()).getMonthViewHeaderLayout();
    }

    public void setMonthViewHeaderLayout(@LayoutRes int monthViewHeaderLayout)
    {
        ((MonthViewPagerAdapter)getAdapter()).setMonthViewHeaderLayout(monthViewHeaderLayout);
    }

    @LayoutRes
    public int getMonthViewCellLayout()
    {
        return ((MonthViewPagerAdapter)getAdapter()).getMonthViewCellLayout();
    }

    public void setMonthViewCellLayout(@LayoutRes int monthViewCellLayout)
    {
        ((MonthViewPagerAdapter)getAdapter()).setMonthViewCellLayout(monthViewCellLayout);
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
