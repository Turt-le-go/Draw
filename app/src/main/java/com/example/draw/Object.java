package com.example.draw;

import android.graphics.Color;
import android.graphics.Path;

public class Object {
    private MainActivity.DrawObject type;
    private float[] coordinates;
    private int color;
    private Path path;
    private boolean fill = false;

    public Object(){
        this.type = MainActivity.DrawObject.point;
        this.coordinates = new float[]{-1,-1,-1,-1};
        this.path = new Path();
        this.color = Color.BLACK;
        this.fill = true;
    }

    public Object(MainActivity.DrawObject type, Path path, int color){
        this.type = type;
        this.path = path;
        this.color = color;
    }
    public Object(MainActivity.DrawObject type, float[] coordinates, int color, boolean fill){
        this.type = type;
        this.coordinates = coordinates;
        this.color = color;
        this.fill = fill;
    }

    public MainActivity.DrawObject getType(){
        return this.type;
    }

    public float[] getCoordinates() {
        return this.coordinates;
    }

    public int getColor() {
        return this.color;
    }

    public Path getPath(){
        return this.path;
    }

    public boolean isFill(){
        return this.fill;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setType(MainActivity.DrawObject type) {
        this.type = type;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }
}
