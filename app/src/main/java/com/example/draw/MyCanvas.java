package com.example.draw;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.Vector;

public class MyCanvas extends androidx.appcompat.widget.AppCompatImageView {

    private Vector<Object> objects = new Vector<Object>();
    private Object object;
    private float[] coordinates;
    private Paint paint = new Paint();
    private float[] currentCoordinates = new float[]{-1, -1, -1, -1};
    private Path path = new Path();
    private Object current = new Object();


    public MyCanvas(Context context) {
        super(context);
        paint.setStrokeWidth(10);
    }

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
    }

    public MyCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setStrokeWidth(10);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator iObject = objects.iterator();
        while (iObject.hasNext()) {
            object = (Object) iObject.next();
            coordinates = object.getCoordinates();
            paint.setColor(object.getColor());
            if (object.isFill()) {
                paint.setStyle(Paint.Style.FILL);
            } else paint.setStyle(Paint.Style.STROKE);
            switch (object.getType()) {
                case point:
                    canvas.drawPoint(coordinates[2],
                            coordinates[3],
                            paint);
                    break;
                case line:
                    canvas.drawLine(coordinates[0],
                            coordinates[1],
                            coordinates[2],
                            coordinates[3],
                            paint);
                    break;
                case curline:
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawPath(object.getPath(), paint);
                    paint.setStyle(Paint.Style.FILL);
                    break;
                case rect:
                    canvas.drawRect(coordinates[0],
                            coordinates[1],
                            coordinates[2],
                            coordinates[3],
                            paint);
                    break;
                case oval:
                    canvas.drawOval(coordinates[0],
                            coordinates[1],
                            coordinates[2],
                            coordinates[3],
                            paint);
                    break;
            }
        }
        currentCoordinates = current.getCoordinates();
        paint.setColor(current.getColor());
        if (current.isFill()) {
            paint.setStyle(Paint.Style.FILL);
        } else paint.setStyle(Paint.Style.STROKE);
        switch (current.getType()) {
            case point:
                canvas.drawPoint(currentCoordinates[2],
                        currentCoordinates[3],
                        paint);
                break;
            case line:
                canvas.drawLine(currentCoordinates[0],
                        currentCoordinates[1],
                        currentCoordinates[2],
                        currentCoordinates[3],
                        paint);
                break;
            case curline:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(this.current.getPath(), paint);
                paint.setStyle(Paint.Style.FILL);
            case rect:
                canvas.drawRect(currentCoordinates[0],
                        currentCoordinates[1],
                        currentCoordinates[2],
                        currentCoordinates[3],
                        paint);
                break;
            case oval:
                canvas.drawOval(currentCoordinates[0],
                        currentCoordinates[1],
                        currentCoordinates[2],
                        currentCoordinates[3],
                        paint);
                break;
        }
    }

    public void clear() {
        setImageURI(Uri.EMPTY);
        objects = new Vector<Object>();
        this.current = new Object();
        invalidate();
    }

    public void setColor(int color) {
        this.current.setColor(color);
    }

    public void setCurrentType(MainActivity.DrawObject type) {
        this.current.setType(type);
    }

    public void setCurrentObjectCoordinates(float x1, float y1, float x2, float y2) {
        this.current.setCoordinates(new float[]{x1, y1, x2, y2});
    }

    public void setCurrentObjectCoordinates() {
        this.current.setCoordinates(new float[]{-1, -1, -1, -1});
    }

    public void currentPath(Path path) {
        this.current.setPath(path);
        this.setCurrentObjectCoordinates();
        this.current.setFill(false);
    }

    public void addObject(Object object) {
        this.objects.add(object);
    }

    public void undo() {
        int size = objects.size();
        if (size > 0) {
            objects.remove(size - 1);
        }
        this.setCurrentObjectCoordinates();
        invalidate();
    }

    public void setFill(boolean fill) {
        this.current.setFill(fill);
    }
}
