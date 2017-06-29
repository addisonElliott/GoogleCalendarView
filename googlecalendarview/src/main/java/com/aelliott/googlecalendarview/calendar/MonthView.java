package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aelliott.googlecalendarview.R;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.TextStyle;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.util.Locale;

import static com.aelliott.googlecalendarview.calendar.CalendarView.DAY_WEEK_DISPLAY_BRIEF;
import static com.aelliott.googlecalendarview.calendar.CalendarView.DAY_WEEK_DISPLAY_FULL;
import static com.aelliott.googlecalendarview.calendar.CalendarView.DAY_WEEK_DISPLAY_NARROW;

public class MonthView extends RecyclerView
{
    private static final String TAG = "MonthView";

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CONTENT = 1;

    /**
     * Number of columns in the GridLayoutManager, equal to the number of days in a week
     */
    private static final int GRID_COLUMN_COUNT = 7;

    private LocalDate displayMonthDate = null;
    private Locale locale = Locale.US;
    private DayOfWeek startDayOfWeek = DayOfWeek.SUNDAY;
    @CalendarView.DayOfWeekDisplay
    private int dayOfWeekDisplay = DAY_WEEK_DISPLAY_NARROW;
    @LayoutRes
    private int headerLayout = 0;
    @LayoutRes
    private int cellLayout = 0;

    public MonthView(Context context)
    {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        setLayoutManager(new GridLayoutManager(context, GRID_COLUMN_COUNT));

        Adapter adapter = new Adapter(context);
        setAdapter(adapter);
    }

    public LocalDate getDisplayMonthDate()
    {
        return displayMonthDate;
    }

    public void setDisplayMonthDate(LocalDate displayMonthDate)
    {
        this.displayMonthDate = displayMonthDate;

        Adapter adapter = (Adapter)getAdapter();
        if (adapter != null)
        {
            adapter.update();
            adapter.notifyDataSetChanged();
        }
    }

    public Locale getLocale()
    {
        return locale;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;

        // If there is a adapter, then notify that changes were made to the data set
        Adapter adapter = (Adapter)getAdapter();
        if (adapter != null)
        {
            adapter.update();
            adapter.notifyDataSetChanged();
        }
    }

    public DayOfWeek getStartDayOfWeek()
    {
        return startDayOfWeek;
    }

    public void setStartDayOfWeek(DayOfWeek startDayOfWeek)
    {
        this.startDayOfWeek = startDayOfWeek;

        // If there is a adapter, then notify that changes were made to the data set
        Adapter adapter = (Adapter)getAdapter();
        if (adapter != null)
        {
            adapter.update();
            adapter.notifyDataSetChanged();
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

        // If there is a adapter, then notify that changes were made to the data set
        Adapter adapter = (Adapter)getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @LayoutRes
    public int getHeaderLayout()
    {
        return headerLayout;
    }

    public void setHeaderLayout(@LayoutRes int headerLayout)
    {
        this.headerLayout = headerLayout;
    }

    @LayoutRes
    public int getCellLayout()
    {
        return cellLayout;
    }

    public void setCellLayout(@LayoutRes int cellLayout)
    {
        this.cellLayout = cellLayout;
    }

    private TextStyle getTextStyle()
    {
        switch (dayOfWeekDisplay)
        {
            case DAY_WEEK_DISPLAY_NARROW:
                return TextStyle.NARROW;
            case DAY_WEEK_DISPLAY_BRIEF:
                return TextStyle.SHORT;
            case DAY_WEEK_DISPLAY_FULL:
                return TextStyle.FULL;
            default:
                return null;
        }
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        private Context context;

        private int monthFirstDayStartOffset = 0;
        private int monthItemCount = 0;

        public Adapter(Context context)
        {
            this.context = context;
        }

        public void update()
        {
            // If the displayMonthDate is invalid, then do nothing
            if (displayMonthDate == null)
                return;

            // Get beginning date of the month
            LocalDate startOfMonth = displayMonthDate.withDayOfMonth(1);
            // Get date of the start of the week. Thus, if today is Thursday, June 1 and the start of
            // the week is Sunday, then the startDate is Sunday, May 28th. The OrSame part means that
            // if today is the start of the week, then return the same date
            LocalDate startDate = startOfMonth.with(
                    TemporalAdjusters.previousOrSame(startDayOfWeek));
            // Get the difference between startOfMonth and startDate to get the number of blank cells
            // to show in the calendar before putting the first day of the month
            monthFirstDayStartOffset = (int)ChronoUnit.DAYS.between(startDate, startOfMonth);

            Log.v("CTL", "Start Date: " + startDate + " StartOfMonth: " + startOfMonth);
            Log.v("CTL", "Day Offset: " + monthFirstDayStartOffset);

            // Total number of items in adapter is one row for the weekday headers plus the empty cell
            // offset and then the number of days in the month
            monthItemCount = GRID_COLUMN_COUNT + monthFirstDayStartOffset + displayMonthDate
                    .getMonth()
                    .length(displayMonthDate.isLeapYear());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == VIEW_TYPE_HEADER)
            {
                View view = inflater.inflate(
                        headerLayout == 0 ? R.layout.month_view_item_header : headerLayout, parent,
                        false);
                return new HeaderViewHolder(view);
            }
            else
            {
                View view = inflater.inflate(
                        cellLayout == 0 ? R.layout.month_view_item_cell : cellLayout, parent,
                        false);
                return new CellViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (holder instanceof HeaderViewHolder)
            {
                HeaderViewHolder viewHolder = (HeaderViewHolder)holder;

                viewHolder.textView.setText(
                        startDayOfWeek.plus(position).getDisplayName(getTextStyle(), locale));
            }
            else
            {
                CellViewHolder viewHolder = (CellViewHolder)holder;

                // Add one to position since it is zero-indexed. We want month days to be 1-30 not 0-29
                // Remove the header row for displaying weekdays by subtracting the number of columns in a row
                // Subtract the first day month offset so that the first day of the month aligns with
                // the correct day of the week
                int dayNumber = position + 1 - GRID_COLUMN_COUNT - monthFirstDayStartOffset;

                // Only print
                if (dayNumber <= 0)
                    viewHolder.textView.setText("");
                else
                    viewHolder.textView.setText(String.format(locale, "%d", dayNumber));
            }
        }

        @Override
        public int getItemCount()
        {
            // Number of items in the grid is equal to the number of days in the month plus another
            // row for the header. If no date is present, set the number of items to zero to show a
            // blank screen
            return monthItemCount;
        }

        @Override
        public int getItemViewType(int position)
        {
            // First row of items are considered the header while the rest are content
            if (position < GRID_COLUMN_COUNT)
                return VIEW_TYPE_HEADER;
            else
                return VIEW_TYPE_CONTENT;
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        final TextView textView;

        public HeaderViewHolder(View view)
        {
            super(view);

            textView = (TextView)view;
        }
    }

    static class CellViewHolder extends RecyclerView.ViewHolder
    {
        final TextView textView;

        public CellViewHolder(View view)
        {
            super(view);

            textView = (TextView)view;
        }
    }
}
