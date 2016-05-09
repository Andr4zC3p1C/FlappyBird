package com.kingcoder.flappy.entities;

import com.kingcoder.flappy.Main;
import com.kingcoder.flappy.handlers.Input;
import com.kingcoder.flappy.handlers.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {

    public static int START_SPEED = 10;

    private Vector2f pos;
    private float scale = 0.8f;
    private float width, height;
    private AffineTransform imageProperties;
    private BufferedImage[] sprites = new BufferedImage[3];
    private BufferedImage currentSprite;
    private int spriteCounter = 0;
    private long animationTimer = System.currentTimeMillis();

    private boolean dead = true;
    private float gAccel = -5;
    private float vel = START_SPEED;

    private float angle = 0;

    public Player(){
        try{
            sprites[0] = ImageIO.read(getClass().getResourceAsStream("/img/bird1.png"));
            sprites[1] = ImageIO.read(getClass().getResourceAsStream("/img/bird2.png"));
            sprites[2] = ImageIO.read(getClass().getResourceAsStream("/img/bird3.png"));
            currentSprite = sprites[0];
        }catch(IOException e){
            e.printStackTrace();
        }

        width = currentSprite.getWidth() * scale;
        height = currentSprite.getHeight() * scale;

        pos = new Vector2f( Main.WIDTH/2 - 50, Main.HEIGHT/2);
        imageProperties = AffineTransform.getTranslateInstance(pos.x, pos.y);
        imageProperties.scale(scale,scale);
    }


    public void update(){
        if(!dead) {
            // The animation
            if(System.currentTimeMillis() - animationTimer >= 1000/12){
                animationTimer = System.currentTimeMillis();
                currentSprite = sprites[spriteCounter];
                spriteCounter++;
                if(spriteCounter > 2) spriteCounter = 0;
            }

            if (Input.isMouseButtonPressed(MouseEvent.BUTTON1)) {
                vel = START_SPEED;
            } else if (vel > -10) {
                vel += gAccel / 6;
            }
            pos.y -= vel;

            if(pos.y > 600 - height/2) {
                dead = true;
                vel = 0;
                currentSprite = sprites[1];
                angle = 0;
            }
        }else{
            if(pos.y < 600 - height/2) {
                vel -= gAccel / 6;
                pos.y += vel;
            }else{
                pos.y = 600 - height/2;
            }
        }

        imageProperties.setTransform(AffineTransform.getTranslateInstance(pos.x - width/2, pos.y - height/2));
        imageProperties.scale(scale,scale);
    }

    public void render(Graphics2D g2d){
        g2d.drawImage(currentSprite, imageProperties, null);
    }

    public void rotateImage(double angle, float focusX, float focusY){
        imageProperties.rotate(Math.toRadians(angle), focusX, focusY);
    }

    public boolean isDead(){
        return dead;
    }

    public void setDead(boolean dead){
        this.dead = dead;
    }

    public void setVel(float vel) { this.vel = vel; }

    public Vector2f getPos(){ return pos;}

    public float getWidth(){ return width;}
    public float getHeight(){ return height;}

}
