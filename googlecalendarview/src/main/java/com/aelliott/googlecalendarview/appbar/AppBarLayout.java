package com.aelliott.googlecalendarview.appbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import com.aelliott.googlecalendarview.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppBarLayout extends android.support.design.widget.AppBarLayout
{
    /**
     * @hide
     */
    @IntDef({STATE_EXPANDED, STATE_COLLAPSED, STATE_EXPANDING, STATE_COLLAPSING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public static final int STATE_EXPANDED = 0;
    public static final int STATE_COLLAPSED = 1;
    public static final int STATE_EXPANDING = 2;
    public static final int STATE_COLLAPSING = 4;

    private int state;
    private View rotateView = null;
    @IdRes
    private int rotateViewResourceId = 0;
    private OnOffsetChangedListener onOffsetChangedListener;

    public AppBarLayout(Context context)
    {
        this(context, null);
    }

    public AppBarLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBarLayout);

        try
        {
            rotateViewResourceId = a.getResourceId(R.styleable.AppBarLayout_rotateViewId, 0);
        }
        finally
        {
            a.recycle();
        }

        onOffsetChangedListener = new OffsetUpdateListener();
        addOnOffsetChangedListener(onOffsetChangedListener);
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();

        if (rotateViewResourceId != 0)
            rotateView = getRootView().findViewById(rotateViewResourceId);
    }

    @State
    public int getState()
    {
        return state;
    }

    public View getRotateView()
    {
        return rotateView;
    }

    public void setRotateView(View rotateView)
    {
        // Reset rotation for previous view back to 0 degrees
        if (rotateView != null)
            rotateView.setRotation(0.0f);

        this.rotateView = rotateView;
    }

    private class OffsetUpdateListener implements OnOffsetChangedListener
    {
        @Override
        public void onOffsetChanged(android.support.design.widget.AppBarLayout appBarLayout,
                int verticalOffset)
        {
            verticalOffset = Math.abs(verticalOffset);

            if (verticalOffset == appBarLayout.getTotalScrollRange()) // Completely collapsed
                state = STATE_COLLAPSED;
            else if (verticalOffset == 0) // Completely expanded
                state = STATE_EXPANDED;
            else if (state == STATE_COLLAPSED) // Transitioning, previous state was collapsed so it is expanding
                state = STATE_EXPANDING;
            else if (state == STATE_EXPANDED) // Transitioning, previous state was expanded so it is collapsing
                state = STATE_COLLAPSING;

            if (rotateView != null)
            {
                float percentComplete = 1.0f - ((float)verticalOffset / appBarLayout.getTotalScrollRange());

                rotateView.setRotation(-180.0f * percentComplete);
            }
        }
    }
}
