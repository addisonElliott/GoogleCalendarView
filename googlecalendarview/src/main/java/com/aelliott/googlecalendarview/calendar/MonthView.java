package com.aelliott.googlecalendarview.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aelliott.googlecalendarview.R;

import org.threeten.bp.LocalDate;

public class MonthView extends RecyclerView
{
    private static final String TAG = "MonthView";

    /**
     * Number of columns in the GridLayoutManager, equal to the number of days in a week
     */
    private static final int GRID_COLUMN_COUNT = 7;

    private LocalDate displayMonthDate;

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
    }

    public LocalDate getDisplayMonthDate()
    {
        return displayMonthDate;
    }

    public void setDisplayMonthDate(LocalDate displayMonthDate)
    {
        this.displayMonthDate = displayMonthDate;

        Adapter adapter = new Adapter(getContext(), displayMonthDate);
        setAdapter(adapter);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
    {
        private Context context;
        private LocalDate date = null;

        public Adapter(Context context, LocalDate date)
        {
            this.context = context;
            this.date = date;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            holder.myTextView.setText(Integer.toString((position + 1)));
        }

        @Override
        public int getItemCount()
        {
            if (date == null)
                return 0;
            else
                return date.getMonth().length(date.isLeapYear());
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView myTextView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                myTextView = (TextView)itemView.findViewById(R.id.info_text);
            }
        }
    }
}
