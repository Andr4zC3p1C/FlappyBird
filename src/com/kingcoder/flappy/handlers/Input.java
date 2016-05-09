package com.kingcoder.flappy.handlers;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener{

    // keyboard
    private static boolean[] keys = new boolean[1024];
    private static boolean[] p_keys = new boolean[1024];

    public void keyTyped(KeyEvent e){}

    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()] = false;
    }


    // mouse
    private static boolean[] buttons = new boolean[256];
    private static int mouseX, mouseY;
    private static boolean mousePressed = false;

    public void mousePressed(MouseEvent e){
        buttons[e.getButton()] = true;
    }

    public void mouseReleased(MouseEvent e){
        buttons[e.getButton()] = false;
        mousePressed = false;
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}

    public void mouseMoved(MouseEvent e){
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public static boolean isMouseButtonPressed(int button){
        boolean result =  buttons[button] && !mousePressed;

        if(result)
         mousePressed = true;

        return result;
    }

    public static int getMouseX(){
        return mouseX;
    }

    public static int getMouseY(){
        return mouseY;
    }

}
