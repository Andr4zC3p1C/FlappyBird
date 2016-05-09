package com.kingcoder.flappy.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager{

    private Thread thread;

    private byte[] data;

    public void writeFile(final String path, byte[] data){
        this.data = data;

        thread = new Thread(new Runnable() {
            public void run() {
                write(path);
            }
        });
        thread.start();
    }

    public byte[] readFile(final String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(new byte[]{0});
                fos.close();
            }

            data = new byte[(int)file.length()];

            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            fis.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return data;
    }

    private void write(String path){
        try {
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
