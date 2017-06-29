package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.aelliott.googlecalendarview.R;

import org.threeten.bp.DayOfWeek;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class CalendarView extends ViewPager
{
    /**
     * @hide
     */
    @IntDef({DAY_WEEK_DISPLAY_NARROW, DAY_WEEK_DISPLAY_BRIEF, DAY_WEEK_DISPLAY_FULL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DayOfWeekDisplay {}

    public static final int DAY_WEEK_DISPLAY_NARROW = 0;
    public static final int DAY_WEEK_DISPLAY_BRIEF = 1;
    public static final int DAY_WEEK_DISPLAY_FULL = 2;

    private MonthViewPagerAdapter pagerAdapter;
    private Locale locale = Locale.US;
    private DayOfWeek startDayOfWeek = DayOfWeek.SUNDAY;
    @DayOfWeekDisplay
    private int dayOfWeekDisplay = DAY_WEEK_DISPLAY_NARROW;
    @LayoutRes
    private int monthViewHeaderLayout = 0;
    @LayoutRes
    private int monthViewCellLayout = 0;

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
        int height = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setupDefaultAdapter()
    {
        // Create new pager adapter and set it as the current adapter for the ViewPager
        pagerAdapter = new MonthViewPagerAdapter(getContext());
        setAdapter(pagerAdapter);

        // Set current item to be in the middle of the circular pager adapter
        setCurrentItem(pagerAdapter.getCount() / 2);
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
        setAdapter(getAdapter());
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
}
