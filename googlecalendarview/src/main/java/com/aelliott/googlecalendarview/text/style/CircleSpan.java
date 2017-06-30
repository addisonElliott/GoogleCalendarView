package com.aelliott.googlecalendarview.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;

/**
 * Text span that draws a circle around text, pads to cover at least 2 characters
 */
public class CircleSpan extends ReplacementSpan
{
    /**
     * Default padding used
     */
    public static final float DEFAULT_PADDING = 8;

    /**
     * Default color used for text color and circle color
     */
    public static final int DEFAULT_COLOR = 0;

    private final float padding;
    private final int circleColor;
    private final int textColor;

    public CircleSpan()
    {
        this(DEFAULT_PADDING, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    public CircleSpan(float padding)
    {
        this(padding, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    public CircleSpan(int circleColor)
    {
        this(DEFAULT_PADDING, circleColor, DEFAULT_COLOR);
    }

    public CircleSpan(int circleColor, int textColor)
    {
        this(DEFAULT_PADDING, circleColor, textColor);
    }

    public CircleSpan(float padding, int circleColor, int textColor)
    {
        super();

        this.padding = padding; // XXX
        this.circleColor = circleColor;
        this.textColor = textColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm)
    {
        return Math.round(paint.measureText(text, start, end) + padding * 2); // left + right
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
            int bottom, Paint paint)
    {
        if (TextUtils.isEmpty(text))
            return;

        // Get text size and the old paint color
        float textSize = paint.measureText(text, start, end);
        int oldColor = paint.getColor();

        // If circle color given, then set to paint with that, otherwise use default in paint
        if (circleColor != 0)
            paint.setColor(circleColor);

        // Ensure radius covers at least 2 characters even if there is only 1
        canvas.drawCircle(x + textSize / 2 + padding, // center X
                (top + bottom) / 2, // center Y
                (text.length() == 1 ? textSize : textSize / 2) + padding, // radius
                paint);

        // If text color given, then set to paint with that, otherwise use default in paint
        // Set this to the oldColor (default) in case a circleColor was specified
        if (textColor != 0)
            paint.setColor(textColor);
        else
            paint.setColor(oldColor);

        // Draw text
        canvas.drawText(text, start, end, padding + x, y, paint);

        // Reset color back to original
        paint.setColor(oldColor);
    }
}