package com.aelliott.googlecalendarview.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.aelliott.googlecalendarview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CalendarScrollingBehavior extends AppBarLayout.Behavior
{
    /**
     * @hide
     */
    @IntDef({DRAG_TYPE_NORMAL, DRAG_TYPE_DENY_ALL, DRAG_TYPE_ALLOW_ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DragType {}

    public static final int DRAG_TYPE_NORMAL = 0;
    public static final int DRAG_TYPE_DENY_ALL = 1;
    public static final int DRAG_TYPE_ALLOW_ALL = 2;

    /**
     * @hide
     */
    @IntDef({SCROLL_TOP_ACTION_NONE, SCROLL_TOP_ACTION_OPEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollTopAction {}

    public static final int SCROLL_TOP_ACTION_NONE = 0;
    public static final int SCROLL_TOP_ACTION_OPEN = 1;

    // Variable for the current value
    private int dragType = DRAG_TYPE_NORMAL;
    private int scrollTopAction = SCROLL_TOP_ACTION_OPEN;

    public CalendarScrollingBehavior(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarScrollingBehavior);

        try
        {
            @DragType int dragType = a.getInt(
                    R.styleable.CalendarScrollingBehavior_behavior_dragType, DRAG_TYPE_NORMAL);
            setDragType(dragType);

            @ScrollTopAction int scrollTopAction = a.getInt(
                    R.styleable.CalendarScrollingBehavior_behavior_scrollTopAction,
                    SCROLL_TOP_ACTION_OPEN);
            setScrollTopAction(scrollTopAction);
        }
        finally
        {
            a.recycle();
        }
    }

    @DragType
    public int getDragType()
    {
        return dragType;
    }

    public void setDragType(@DragType int dragType)
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

    @ScrollTopAction
    public int getScrollTopAction()
    {
        return scrollTopAction;
    }

    public void setScrollTopAction(@ScrollTopAction int scrollTopAction)
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