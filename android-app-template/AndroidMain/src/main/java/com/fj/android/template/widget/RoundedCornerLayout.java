/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.FrameLayout;
import com.fj.android.template.R;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @see <a href="http://stackoverflow.com/questions/26074784/how-to-make-a-view-in-android-with-rounded-corners">
 * How to make a view in android with rounded corners</a> in stackoverflow.com
 * @see <a href="https://developer.android.com/reference/android/view/View.html#setClipToOutline(boolean)">
 * android.view.View#setClipToOutline(boolean)</a>
 * @since 30 - Sep - 2016
 */
public class RoundedCornerLayout extends FrameLayout {
    private static final float DEFAULT_CORNER_RADIUS = 2.0f;
    private Bitmap maskBitmap;
    private Paint  paint, maskPaint;
    private float cornerRadius;

    public RoundedCornerLayout(final Context context) {
        this(context, null);
    }

    public RoundedCornerLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedCornerLayout(final Context context, final AttributeSet attrs, final int style) {
        super(context, attrs, style);
        init(attrs);
    }

    private void init(final AttributeSet attrs) {
        final Context        context = getContext();
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final TypedArray     a       = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornerLayout);
        final float radius = a.getDimension(R.styleable.RoundedCornerLayout_cornerRadius, DEFAULT_CORNER_RADIUS);
        a.recycle();

        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, metrics);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }

    @Override
    public void draw(final Canvas canvas) {
        final Bitmap offscreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config
                .ARGB_8888);
        final Canvas offscreenCanvas = new Canvas(offscreenBitmap);

        super.draw(offscreenCanvas);

        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }

        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);
    }

    private Bitmap createMask(final int width, final int height) {
        final Bitmap mask   = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        final Canvas canvas = new Canvas(mask);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);

        return mask;
    }
}
