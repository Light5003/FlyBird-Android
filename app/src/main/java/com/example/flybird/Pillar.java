package com.example.flybird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;

import java.util.Random;

public class Pillar {
    private int lx,rx,ly,ry; //ly = 0, ry = pil_height
    private int lx2,rx2,ly2,ry2; //lx2 = lx, ly2 = ScrennHeight-pilheight, rx2 = rx, ry2 = ScrH
    private int pillar_height;
    private int pillar_height2;
    private int pillar_width;
    // 柱子坐标生成的矩形，判断与小鸟矩形是否相撞
    private RectF topPil;
    private RectF botPil;
    // 随机数生成不同的柱子
    private Random random = new Random();

    public Pillar(int viewHtight,int viewWidth){
        this.topPil = new RectF();
        this.botPil = new RectF();
//            Paint paint = new Paint();
//            paint.setStyle(Paint.Style.FILL); //样式
//            paint.setAntiAlias(true); //抗锯齿
        //topPillar
        while (true) {
            randomHeight(viewHtight);
            randomHeight2(viewHtight);
            if (viewHtight - pillar_height - pillar_height2 > viewHtight/8 && pillar_height + pillar_height2 > 175) {
                break;
            }
        }
        randomWidth(viewWidth);
//            paint.setColor(Color.rgb(0,255,0));
        lx = viewWidth-pillar_width-30;
        ly = 0;
        rx = viewWidth-30;
        ry = pillar_height;
        //canvas.drawRect(lx, ly,rx,ry,paint);
        this.topPil.set(lx,ly,rx,ry);
        //this.topWind.createScaledBitmap(topPic,pillar_width,pillar_height,true);
        //botPillar
        lx2 = lx;
        ly2 = viewHtight-pillar_height;
        rx2 = rx;
        ry2 = viewHtight;
        this.botPil.set(lx2,ly2,rx2,ry2);
        //this.botTimg.createScaledBitmap(botPic,pillar_width,pillar_height,true);
        //paint.setColor(Color.rgb(0,0,255));
        //canvas.drawRect(lx2,ly2 ,rx2,ry2,paint);
    }

    public void randomWidth(int viewWidth){
        pillar_width = random.nextInt((260-80) + viewWidth/12);
    }
    public void randomHeight(int viewHeigt){
        pillar_height = random.nextInt((viewHeigt/2 - 50)+ 50);
        if(viewHeigt - (ry + pillar_height) < 75){
            randomHeight(viewHeigt);
        }
    }
    public void randomHeight2(int viewHeigt){
        pillar_height2 = random.nextInt((viewHeigt/3 - 50)+ 50);
        if(viewHeigt - (ry + pillar_height) < 75){
            randomHeight(viewHeigt);
        }
    }

    public void drawPillar(Canvas canvas){
        //柱子是直接绘画上去的，canvas paint
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL); //样式
        paint.setAntiAlias(true); //抗锯齿
        //topPillar
        paint.setColor(Color.rgb(0,255,0));
        canvas.drawRect(lx, ly, rx, ry, paint);
        //botPillar
        paint.setColor(Color.rgb(85,205,202));
        canvas.drawRect(lx2, ly2 ,rx2, ry2, paint);
    }

    public void setCoordinate(int x_minnus){
        // move pillar left，移动柱子，x坐标减去
        this.lx -= x_minnus;
        this.rx -= x_minnus;
        this.lx2 -= x_minnus;
        this.rx2 -= x_minnus;
    }

    public boolean pengBird(Bird bird) {
        //judge the bird fly in the pillar area，判断小鸟是否撞上柱子，intersect或者intersects函数
        RectF tmp = new RectF();
        if (topPil.intersects(tmp.left,tmp.top,tmp.right,tmp.bottom)){
            return true; //永假，原因未明
        }
        if (bird.getBird_width()+bird.getBird_x() >= lx && (bird.getBird_y() < ry)){
            return true;
        }
        if (bird.getBird_width()+bird.getBird_x() >= lx && (bird.getBird_y() + bird.getBird_height() > ly2)){
            return true;
        }
        return false;
        //true game over,false continue
    }

    public boolean touchSide(){
        if(rx <= 0){// touch the screen side
            return true;
        }else {
            return false;
        }
    }

    public int getLx(){
        return lx;
    }


}
