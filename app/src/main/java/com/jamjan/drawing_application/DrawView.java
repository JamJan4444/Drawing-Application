package com.jamjan.drawing_application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class DrawView extends android.support.v7.widget.AppCompatImageView {

    Canvas canvas = null;
    Bitmap mask = null;

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
        this.setBackgroundColor(Color.RED);
        this.setAlpha(0.8f);
        this.setScaleType(ScaleType.FIT_XY);
        this.setLayoutParams(new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
    }

    public void setMaskSize(int width, int height) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        mask = Bitmap.createBitmap(width, height, conf);
        canvas = new Canvas(mask);
        this.setImageBitmap(mask);
    }
}
