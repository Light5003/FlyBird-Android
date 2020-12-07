package com.example.flybird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class Button {
    //property:x y、pic、status
    private int x,y;
    private Bitmap but_pic,press_pic;
    private RectF but_area;
    private boolean isClick = false;

    public Button(Bitmap bpic,Bitmap ppic,int view_width,int view_height){
        this.but_pic = bpic;
        this.press_pic = ppic;
        this.x = view_width/2 - but_pic.getWidth()/2;//left line
        this.y = view_height/2;
        this.but_area = new RectF();
    }
    public void drawButton(Canvas canvas){
        canvas.save();
        but_area.set(x,y,x+but_pic.getWidth(),y+but_pic.getHeight());
        if(isClick){
            canvas.drawBitmap(press_pic,null,but_area,null);
        }else{
            canvas.drawBitmap(but_pic,null,but_area,null);
        }
        canvas.restore();
    }
    //click status
    public void setClick(boolean isclick) {
        this.isClick = isclick;
    }
    public boolean isClick(int newX,int newY) {
        Rect rect = new Rect(x,y,x+press_pic.getWidth(),y+press_pic.getHeight());
        isClick = rect.contains(newX,newY);
        return isClick;
    }
    //监听是否点击
    public void click(){
        if(mListener != null){
            mListener.click();
        }
    }
    private OnButtonClickListener mListener;
    public interface OnButtonClickListener{
        void click();
    }
    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.mListener = listener;
    }
    public void setY(int y){
        this.y = y;

    }
}
