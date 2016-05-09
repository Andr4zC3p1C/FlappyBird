package com.kingcoder.flappy.entities;

import com.kingcoder.flappy.Main;
import com.kingcoder.flappy.handlers.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Pipe {

    private Vector2f pos;
    private BufferedImage pipeUp, pipeDown;
    private int width, height;

    private long timer = System.currentTimeMillis();

    static Random random = new Random();

    public Pipe(){
        float y = 200 + 25 * (random.nextInt(100) / 10);
        pos = new Vector2f(Main.WIDTH, y);

        try {
            pipeDown = ImageIO.read(getClass().getResourceAsStream("/img/pipe_down.png"));
            pipeUp = ImageIO.read(getClass().getResourceAsStream("/img/pipe_up.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

        width = 82;
        height = 420;
    }

    public void update(){
        pos.x += -3;
        if(pos.x + width < 0){
            respawn();
        }
    }

    public void render(Graphics2D g2d){
        g2d.drawImage(pipeUp, (int)pos.x, (int)pos.y + 90, width, height, null);
        g2d.drawImage(pipeDown, (int)pos.x, (int)pos.y - 90 - height, width, height, null);
    }


    public void respawn(){
        float y = 200 + 25 * (random.nextInt(100) / 10);
        pos = new Vector2f(Main.WIDTH, y);
    }


    public float getCenterX(){
        return pos.x + pipeUp.getWidth()/2;
    }

    public float getX(){
        return pos.x;
    }
    public float getY() { return pos.y; }

    public void setX(float x){
        pos.x = x;
    }

    public int getWidth(){return width;}
    public int getHeight(){return height;}

}
