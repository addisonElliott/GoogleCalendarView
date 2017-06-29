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

public class MonthView extends RecyclerView
{
    /**
     * Number of columns in the GridLayoutManager, equal to the number of days in a week
     */
    private static final int GRID_COLUMN_COUNT = 7;

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

        // data to populate the RecyclerView with
        String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};

        Adapter adapter = new Adapter(context, data);
        setAdapter(adapter);
    }

    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
    {
        private String[] data = new String[0];
        private LayoutInflater inflater;

        // data is passed into the constructor
        public Adapter(Context context, String[] data)
        {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        // binds the data to the textview in each cell
        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            String animal = data[position];
            holder.myTextView.setText(animal);
        }

        // total number of cells
        @Override
        public int getItemCount()
        {
            return data.length;
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

        // convenience method for getting data at click position
        public String getItem(int id)
        {
            return data[id];
        }
    }
}
