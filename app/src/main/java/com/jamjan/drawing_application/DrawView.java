package com.jamjan.drawing_application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DrawView extends android.support.v7.widget.AppCompatImageView {

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private Canvas canvas = null;
    private Bitmap mask = null;
    private Path path;
    private Paint paint;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.path = new Path();
        setupPaint();
        this.setBackgroundColor(Color.RED);
        this.setAlpha(0.8f);
        this.setScaleType(ScaleType.FIT_XY);
        this.setLayoutParams(new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
    }

    private void setupPaint() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(10);
    }

    public void setMaskSize(int width, int height) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        mask = Bitmap.createBitmap(width, height, conf);
        canvas = new Canvas(mask);
        this.setImageBitmap(mask);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }




    private void touch_start(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }


    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }


    private void touch_up() {
        path.lineTo(mX, mY);
        // commit the path to our offscreen
        canvas.drawPath(path,  paint);
        // kill this so we don't double draw
        path.reset();
    }
}
