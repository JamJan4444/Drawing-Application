package com.jamjan.drawing_application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DrawView extends android.support.v7.widget.AppCompatImageView {

    private static final float TOUCH_TOLERANCE = 4;
    public static final int MAX_STROKE_WIDTH = 50;

    private SeekBar seekBar;
    private Button saveButton;
    private Bitmap image;

    private float mX, mY;
    private Canvas canvas = null;
    private Bitmap mask = null;
    private Path active_path;
    private Paint paint;

    // Save the paths in the arraylist to provide redo and undo functionality
    private ArrayList<Path> myPaths;

    public DrawView(Context context, Bitmap image) {
        super(context);
        init(image);
    }

    public DrawView(Context context, AttributeSet attrs, Bitmap image) {
        super(context, attrs);
        init(image);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, Bitmap image) {
        super(context, attrs, defStyleAttr);
        init(image);
    }

    private void init(Bitmap image) {

        this.image = image;

        saveButton = ((Activity) getContext()).findViewById(R.id.fragment_drawing_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // save the picture
                Bitmap result = Utils.mask(DrawView.this.image, DrawView.this.mask);
                result.hasAlpha();

                try (FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/test.png")) {
                    result.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBar = ((Activity) getContext()).findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DrawView.this.paint.setStrokeWidth(MAX_STROKE_WIDTH * ((float)seekBar.getProgress() / 100));
            }
        });

        myPaths = new ArrayList<>();
        setupPaint();
        //this.setBackgroundColor(Color.RED);
        //this.setAlpha(0.8f);
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
        if(active_path == null) {
            active_path = new Path();
            active_path.moveTo(x, y);
            mX = x;
            mY = y;
        }
    }


    private void touch_move(float x, float y) {
        if(active_path != null) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                active_path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }
    }


    private void touch_up() {
        if(active_path != null) {
            active_path.lineTo(mX, mY);
            // commit the path to our offscreen
            canvas.drawPath(active_path, paint);
            // kill this so we don't double draw
            myPaths.add(active_path);
            active_path = null;
        }
    }
}
