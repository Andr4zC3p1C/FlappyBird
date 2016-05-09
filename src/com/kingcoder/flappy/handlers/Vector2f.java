package com.kingcoder.flappy.handlers;

public class Vector2f {

    public float x, y;

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float length(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public void normalize(){
        float length = length();
        x /= length;
        y /= length;
    }

}
