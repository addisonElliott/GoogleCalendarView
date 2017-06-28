package com.aelliott.googlecalendarview.appbar;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppBarLayout extends android.support.design.widget.AppBarLayout
{
    /**
     * @hide
     */
    @IntDef({STATE_EXPANDED, STATE_COLLAPSED, STATE_TRANSITIONING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public static final int STATE_EXPANDED = 0;
    public static final int STATE_COLLAPSED = 1;
    public static final int STATE_TRANSITIONING = 2;

    private int state;

    public AppBarLayout(Context context)
    {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        addOnOffsetChangedListener(new OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(android.support.design.widget.AppBarLayout appBarLayout,
                    int verticalOffset)
            {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange())
                    state = STATE_COLLAPSED;
                else if (verticalOffset == 0)
                    state = STATE_EXPANDED;
                else
                    state = STATE_TRANSITIONING;
            }
        });
    }

    @State
    public int getState()
    {
        return state;
    }
}
