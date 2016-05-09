package com.kingcoder.flappy.scenes;

import com.kingcoder.flappy.Main;
import com.kingcoder.flappy.entities.Pipe;
import com.kingcoder.flappy.entities.Player;
import com.kingcoder.flappy.handlers.FileManager;
import com.kingcoder.flappy.handlers.Input;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Play {

    private boolean started = false;

    private Player player;
    private BufferedImage background, floor1, floor2, left_click;
    private BufferedImage[] digits = new BufferedImage[10];
    private AffineTransform af;
    private float f1X, f2X;

    private Pipe pipe1, pipe2;
    private long timer = System.currentTimeMillis();
    private boolean spawning = false;

    private int score = 0;
    private int highscore = 0;
    private boolean scored = false;

    private Font h_font = new Font(Font.SANS_SERIF, Font.BOLD, 35);
    private Font p_font = new Font(Font.SANS_SERIF, 0, 30);

    public boolean init(){
        player = new Player();

        try{
            background = ImageIO.read(getClass().getResourceAsStream("/img/background.png"));
            floor1 =  ImageIO.read(getClass().getResourceAsStream("/img/floor.png"));
            floor2 =  ImageIO.read(getClass().getResourceAsStream("/img/floor.png"));
            left_click = ImageIO.read(getClass().getResourceAsStream("/img/left_click.png"));

            af = AffineTransform.getTranslateInstance(200, 250);
            af.scale(0.25, 0.25);

            for(int i=0; i<digits.length;i++){
                digits[i] = ImageIO.read(getClass().getResourceAsStream("/img/num" + i + ".png"));
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        f1X = 0;
        f2X = Main.WIDTH;

        pipe1 = new Pipe();
        pipe2 = new Pipe();
        pipe2.setX(Main.WIDTH + Main.WIDTH/2 + pipe2.getWidth()/2);

        // reading the highscore
        byte[] data;
        data = Main.fileManager.readFile(System.getProperty("user.home") + "/flappy_bird.hs");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < data.length; i++){
            sb.append(data[i]);
        }
        highscore = Integer.valueOf(sb.toString());

        return true;
    }

    public void update(){
        if(!started){
            if(Input.isMouseButtonPressed(MouseEvent.BUTTON1)){
                started = true;
                restart();
            }

            f1X -= 3;
            f2X -= 3;
            if (f1X <= -Main.WIDTH) {
                f1X = Main.WIDTH;
            }
            if (f2X <= -Main.WIDTH) {
                f2X = Main.WIDTH;
            }
        }else {
            player.update();

            if(!player.isDead()) {
                f1X -= 3;
                f2X -= 3;
                if (f1X <= -Main.WIDTH) {
                    f1X = Main.WIDTH;
                }
                if (f2X <= -Main.WIDTH) {
                    f2X = Main.WIDTH;
                }

                if(spawning){
                    pipe1.update();
                    pipe2.update();
                }else{
                    if(System.currentTimeMillis() - timer >= 3000){
                        timer = System.currentTimeMillis();
                        pipe1.update();
                        pipe2.update();
                        spawning = true;
                    }
                }

                score();
                checkCollision();
            }else {
                if(Input.isMouseButtonPressed(MouseEvent.BUTTON1)){
                    restart();
                }
            }
        }
    }

    public void render(Graphics2D g2d){
        g2d.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);

        pipe1.render(g2d);
        pipe2.render(g2d);

        player.render(g2d);

        g2d.drawImage(floor1, (int)f1X, 600, Main.WIDTH, 200, null);
        g2d.drawImage(floor2, (int)f2X, 600, Main.WIDTH, 200, null);

        if(player.isDead()){
            int str_width;

            // rendering the highscore
            g2d.setColor(Color.WHITE);
            g2d.setFont(h_font);
            String s = "HIGHSCORE " + highscore;
            str_width = g2d.getFontMetrics(h_font).stringWidth(s);
            g2d.drawString(s, Main.WIDTH/2 - str_width/2, Main.HEIGHT/2 - 200);

            // rendering the click text
            g2d.setFont(p_font);
            g2d.drawString("Click", 120, Main.HEIGHT/2 - 100);
            g2d.drawImage(left_click, af, null);
            g2d.drawString("to play!", 270, Main.HEIGHT/2 - 100);
        }

        char[] data = String.valueOf(score).toCharArray();
        int imgWidth = digits[0].getWidth();
        for(int i = 0; i < data.length; i++){
            g2d.drawImage(digits[Integer.parseInt(String.valueOf(data[i]))], Main.WIDTH/2 - imgWidth/2 + i * imgWidth, 50, null);
        }
    }

    private void checkCollision(){
        // Bounding box collision
        float playerX1 = player.getPos().x - player.getWidth()/2;
        float playerY1 = player.getPos().y - player.getHeight()/2;
        float playerX2 = player.getPos().x + player.getWidth()/2;
        float playerY2 = player.getPos().y - player.getHeight()/2;
        float playerX3 = player.getPos().x - player.getWidth()/2;
        float playerY3 = player.getPos().y + player.getHeight()/2;
        float playerX4 = player.getPos().x + player.getWidth()/2;
        float playerY4 = player.getPos().y + player.getHeight()/2;

        boolean dead = false;

        // 1. pipe
        if(playerX1 >= pipe1.getX() && playerX1 <= pipe1.getX() + pipe1.getWidth()){
            if(playerY1 <= pipe1.getY() - 90 || playerY1 >= pipe1.getY() + 80){
                dead = true;
            }
        }

        if(playerX2 >= pipe1.getX() && playerX2 <= pipe1.getX() + pipe1.getWidth()){
            if(playerY2 <= pipe1.getY() - 90 || playerY2 >= pipe1.getY() + 80){
                dead = true;
            }
        }

        if(playerX3 >= pipe1.getX() && playerX3 <= pipe1.getX() + pipe1.getWidth()){
            if(playerY3 <= pipe1.getY() - 90 || playerY3 >= pipe1.getY() + 80){
                dead = true;
            }
        }

        if(playerX4 >= pipe1.getX() && playerX4 <= pipe1.getX() + pipe1.getWidth()){
            if(playerY4 <= pipe1.getY() - 90 || playerY4 >= pipe1.getY() + 80){
                dead = true;
            }
        }

        // 2. pipe
        if(playerX1 >= pipe2.getX() && playerX1 <= pipe2.getX() + pipe2.getWidth()){
            if(playerY1 <= pipe2.getY() - 90 || playerY1 >= pipe2.getY() + 80){
                dead = true;
            }
        }

        if(playerX2 >= pipe2.getX() && playerX2 <= pipe2.getX() + pipe2.getWidth()){
            if(playerY2 <= pipe2.getY() - 90 || playerY2 >= pipe2.getY() + 80){
                dead = true;
            }
        }

        if(playerX3 >= pipe2.getX() && playerX3 <= pipe2.getX() + pipe2.getWidth()){
            if(playerY3 <= pipe2.getY() - 90 || playerY3 >= pipe2.getY() + 80){
                dead = true;
            }
        }

        if(playerX4 >= pipe2.getX() && playerX4 <= pipe2.getX() + pipe2.getWidth()){
            if(playerY4 <= pipe2.getY() - 150 || playerY4 >= pipe2.getY() + 150){
                dead = true;
            }
        }

        if(dead){
            scored = false;
            player.setDead(true);

            // writing the highscore to a file
            char[] tmp = String.valueOf(highscore).toCharArray();
            byte[] data = new byte[tmp.length];
            for(int i=0; i < data.length; i++){
                data[i] = Byte.valueOf(String.valueOf(tmp[i]));
            }

            Main.fileManager.writeFile(System.getProperty("user.home") + "/flappy_bird.hs", data);
        }
    }


    private void score(){
        if(!scored) {
            if (player.getPos().x > pipe1.getX() && player.getPos().x < pipe1.getX() + pipe1.getWidth()){
                scored = true;
                score++;
            }
        }else if(player.getPos().x > pipe1.getX() + pipe1.getWidth()){
            scored = false;
        }

        if(!scored) {
            if (player.getPos().x > pipe2.getX() && player.getPos().x < pipe2.getX() + pipe2.getWidth()){
                scored = true;
                score++;
            }
        }else if(player.getPos().x > pipe2.getX() + pipe2.getWidth()){
            scored = false;
        }

        if(score > highscore){
            highscore = score;
        }
    }


    private void restart(){
        score = 0;
        player.setDead(false);
        player.getPos().y = Main.HEIGHT/2;
        player.setVel(Player.START_SPEED);
        pipe1.respawn();
        pipe2.respawn();
        pipe2.setX(Main.WIDTH + Main.WIDTH / 2 + pipe2.getWidth() / 2);
        spawning = false;
        timer = System.currentTimeMillis();
    }


}
