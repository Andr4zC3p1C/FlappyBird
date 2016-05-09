package com.kingcoder.flappy;

import com.kingcoder.flappy.handlers.FileManager;
import com.kingcoder.flappy.handlers.Input;
import com.kingcoder.flappy.scenes.Play;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Arrays;

public class Main extends Canvas implements Runnable{

    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Flappy Bird";

    private Thread thread;
    private static boolean running;
    private Play play;

    public static FileManager fileManager;

    public void run(){
        int updatePerSec = 60;
        long updateTimer = System.nanoTime();
        int loops;
        int maxFrameSkip = 10;
        int skipTicks = 1000000000 / updatePerSec;
        long timerSec = System.nanoTime();
        int updates = 0;
        int frames = 0;

        while(running){
            loops = 0;
            while(System.nanoTime() > updateTimer && loops < maxFrameSkip){
                updateTimer += skipTicks;
                loops++;
                update();
                updates++;
            }

            render();
            frames++;

            if(System.nanoTime() - timerSec >= 1000000000){
                timerSec = System.nanoTime();
                System.out.println("FPS: " + frames + "\t" + "UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

        close();
    }

    public boolean init(){
        running = true;
        thread = new Thread(this);

        // setting input
        Input input = new Input();
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);

        fileManager = new FileManager();

        play = new Play();
        if(!play.init()){
            return false;
        }

        thread.start();

        return true;
    }

    public void close(){
        System.exit(0);
    }

    public void update(){
        play.update();
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        // SETTING G2D ANTIALIAS
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        RenderingHints trh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHints(trh);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // clearing the screen
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0,0, WIDTH, HEIGHT);

        play.render(g2d);

        g.dispose();
        bs.show();
    }

    public static void setRunning(boolean _running){
        running = _running;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame(TITLE);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.setAutoRequestFocus(true);

        Main main = new Main();
        jFrame.add(main);
        jFrame.pack();
        jFrame.setVisible(true);

        if(!main.init()){
            System.out.println("Error initializing!");
            System.exit(1);
        }
    }
}
