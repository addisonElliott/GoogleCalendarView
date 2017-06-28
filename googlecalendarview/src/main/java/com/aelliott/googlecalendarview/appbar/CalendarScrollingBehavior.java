package com.aelliott.googlecalendarview.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.aelliott.googlecalendarview.R;

public class CalendarScrollingBehavior extends AppBarLayout.Behavior
{
    // Constants for attributes
    private static final int DRAG_TYPE_NORMAL = 0;
    private static final int DRAG_TYPE_DENY_ALL = 1;
    private static final int DRAG_TYPE_ALLOW_ALL = 2;

    private static final int SCROLL_TOP_ACTION_NONE = 0;
    private static final int SCROLL_TOP_ACTION_OPEN = 1;

    // Variable for the current value
    private int dragType = DRAG_TYPE_NORMAL;
    private int scrollTopAction = SCROLL_TOP_ACTION_OPEN;

    public CalendarScrollingBehavior(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarScrollingBehavior);

        try
        {
            setDragType(a.getInteger(R.styleable.CalendarScrollingBehavior_behavior_dragType,
                    DRAG_TYPE_NORMAL));

            setScrollTopAction(
                    a.getInteger(R.styleable.CalendarScrollingBehavior_behavior_scrollTopAction,
                            SCROLL_TOP_ACTION_OPEN));
        }
        finally
        {
            a.recycle();
        }
    }

    public int getDragType()
    {
        return dragType;
    }

    public void setDragType(int dragType)
    {
        this.dragType = dragType;

        if (dragType != DRAG_TYPE_NORMAL)
        {
            setDragCallback(new DragCallback()
            {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout)
                {
                    // If dragType is to allow all, return true always to allow dragging
                    // Otherwise return false because dragType is to deny all
                    return (CalendarScrollingBehavior.this.dragType == DRAG_TYPE_ALLOW_ALL);
                }
            });
        }
        else
            setDragCallback(null);
    }

    public int getScrollTopAction()
    {
        return scrollTopAction;
    }

    public void setScrollTopAction(int scrollTopAction)
    {
        this.scrollTopAction = scrollTopAction;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
            int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        if (scrollTopAction == SCROLL_TOP_ACTION_OPEN || (scrollTopAction == SCROLL_TOP_ACTION_NONE && dyUnconsumed >= 0))
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                    dxUnconsumed, dyUnconsumed);
    }
}