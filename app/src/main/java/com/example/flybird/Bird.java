package com.example.flybird;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.InputStream;

public class Bird {
    //Property:x、y,size、pic、
    private int bird_x,bird_y;
    private Bitmap birdPic;
    private int bird_height,bird_width;
    private RectF birdArea;
    //private int bird_drop = 5;
    private int gameHeight; // 判断触底
    //functions:init、更改小鸟位置
    public void setBird_y(int y){
        this.bird_y += y;
    }
    public void setBird_x(int x){
        this.bird_x=x;
    }
    public int getBird_x(){
        return bird_x;
    }
    public int getBird_y(){
        return bird_y;
    }
    public int getBird_height(){
        return bird_height;
    }
    public int getBird_width(){
        return bird_width;
    }
    public int getView_height(){
        return gameHeight;
    }
    public RectF getBird_Rec(){
        return birdArea;
    }

    public Bird(Bitmap bitmap,int view_height){
        this.birdPic = bitmap;
        this.bird_width = bitmap.getWidth();
        this.bird_height = bitmap.getHeight();
        this.bird_x = 0;
        this.bird_y = view_height/2;
        this.birdArea = new RectF();
        this.gameHeight = view_height;
    }

    public void  drawBird(Canvas canvas){
        birdArea.set(bird_x,bird_y,bird_x+bird_width,bird_y+bird_height);
        canvas.drawBitmap(birdPic,null,birdArea,null);
    }

    public void resetBird(){
        //the position init when restart game
        bird_y = gameHeight/2;
        bird_x = 0;
    }
}
