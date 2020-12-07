package com.example.flybird;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;

public class GameView extends SurfaceView implements Callback, Runnable, SurfaceHolder.Callback {
    private Canvas canvas;
    private SurfaceHolder mHolder;
    private ExecutorService mPool;
    private boolean isRunnging;
    private boolean endFlag = false;
    private int viewheight;
    private int viewWidth;
    private int score = 0;
    //bird
    private Bitmap birdPic;
    private Bird mbird;
    private int up_speed = -50;
    private int total_up = 0;
    //pillar
    private Pillar mpillar;
    private int pSpeed = 10;
    //button
    private Button startButton;
    private Bitmap startPic,pressPic;
    private Button reButton;
    private Bitmap rePic, rePrePic;

    //game status
    private GameStatus mStatus = GameStatus.WAITING;
    private enum GameStatus {
        WAITING, RUNNING, OVER
    }
    //-------- construct function -------
    public GameView(Context context,int scr_h, int scr_w) {
        super(context);
        initGameView(scr_h,scr_w);
    }

    public GameView(Context context, AttributeSet attrs,int scr_h, int scr_w) {
        super(context, attrs);
        initGameView(scr_h,scr_w);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr,int scr_h, int scr_w) {
        super(context, attrs, defStyleAttr);
        initGameView(scr_h,scr_w);
    }

    public void initGameView(int scr_h, int scr_w) {
        //super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);
        // 设置画布 背景透明
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        // 焦点设置
        setFocusable(true);
        // 设置触屏
        setFocusableInTouchMode(true);
        // 设置常亮
        setKeepScreenOn(true);
        // pillar and bird
        viewheight = scr_h;
        viewWidth = scr_w;
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.b1);
        birdPic = BitmapFactory.decodeStream(is);
        mbird = new Bird(birdPic,viewheight);
        mpillar = new Pillar(viewheight,viewWidth);
        // button
        @SuppressLint("ResourceType") InputStream ss = getResources().openRawResource(R.drawable.start);
        startPic = BitmapFactory.decodeStream(ss);
        @SuppressLint("ResourceType") InputStream ss2 = getResources().openRawResource(R.drawable.start2);
        pressPic = BitmapFactory.decodeStream(ss2);
        startButton = new Button(startPic,pressPic,viewWidth,viewheight);
        @SuppressLint("ResourceType") InputStream rs = getResources().openRawResource(R.drawable.restart);
        @SuppressLint("ResourceType") InputStream rs2 = getResources().openRawResource(R.drawable.restart1);
        rePic = BitmapFactory.decodeStream(rs);
        rePrePic = BitmapFactory.decodeStream(rs2);
        reButton = new Button(rePic,rePrePic,viewWidth,viewheight);
        if (mStatus == GameStatus.WAITING && startButton != null){
            @SuppressLint("ObjectAnimatorBinding") ObjectAnimator anim = ObjectAnimator.ofInt(startButton, "Y", viewheight,viewheight/2);
            anim.setDuration(2000);
            anim.start();
        }
        startButton.setOnButtonClickListener(new Button.OnButtonClickListener() {
            @Override
            public void click() {
                if (mStatus == GameStatus.WAITING){
                    mStatus = GameStatus.RUNNING;
                }
            }
        });
        reButton.setOnButtonClickListener(new Button.OnButtonClickListener() {
            @Override
            public void click() {
                mStatus = GameStatus.WAITING;
                mbird.resetBird();
            }
        });
    }

    //---------- game event ----------------
    public void PillarWork(){
        if(mpillar.touchSide()){
            // score++
            score++;
            // new pillar
            this.mpillar = new Pillar(viewheight,viewWidth);
        }
        else {
            mpillar.setCoordinate(pSpeed);
        }
    }

    public void checkGameOver(){
        //MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.fly);
        if (mbird.getBird_y() > viewheight - mbird.getBird_height()) {
            //mediaPlayer.start();
            mStatus = GameStatus.OVER;
        }
        if (mpillar.pengBird(mbird)) {
            mStatus = GameStatus.OVER;
        }
    }

    public void Playing(){
        switch (mStatus){
            case WAITING:
                score = 0;
                break;
            case RUNNING:
                //score = 0;
                mbird.setBird_y(5);
                PillarWork();
                //invalidate();
                checkGameOver();
                break;
            case OVER:
                pSpeed = 10;
                mpillar = new Pillar(viewheight,viewWidth);
                break;
        }
    }

    @Override
    public void run() {
        while (isRunnging) {
            long start = System.currentTimeMillis();
            Playing();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 30) {
                SystemClock.sleep(30 - (end - start));
            }
            // make sure refresh time same
        }
    }

    public void draw(){
        //super.onDraw(canvas);
        Paint paint = new Paint();
        //super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        try {
            if(mHolder != null){
                canvas = mHolder.lockCanvas();
                //清屏
                canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
                paint.setColor(Color.rgb(0,0,0));
                paint.setTextSize(80);
                canvas.drawText(score+"",40,80,paint);
                mbird.drawBird(canvas);
                if (mStatus == GameStatus.WAITING){
                    drawGameStart();
                    drawStartButton();
                }
                if(mStatus == GameStatus.RUNNING){
                    mpillar.drawPillar(canvas);
                }
                if (mStatus == GameStatus.OVER){
                    drawGameOver();
                    drawRestartButton();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (mHolder != null && canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }

        }

    }

    //-------- game material --------------
    private void drawStartButton(){
        startButton.drawButton(canvas);
    }
    private void drawRestartButton(){
        reButton.setY(viewheight/2 + 100);
        reButton.drawButton(canvas);
    }

    private void drawGameStart() {
        String title = "----FlyBird----";
        String co_title = "天地逆转";
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 是否抗锯齿
        paint.setStrokeWidth(3);
        paint.setColor(Color.rgb(255,255,255));
        paint.setTextSize(200);
        paint.setShadowLayer(5, 3, 3, 0xff888888);// 设置阴影
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.STROKE); //空心
        paint.setDither(true);
        canvas.drawText(title,viewWidth/2,viewheight/2 - 150,paint);
        paint.setStyle(Paint.Style.FILL); // 实心
        paint.setTextSize(50);
        paint.setTextSkewX(-0.5f);
        canvas.drawText(co_title,viewWidth/2,viewheight/2 - 50,paint);
    }

    private void drawGameOver() {
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 是否抗锯齿
        paint.setStrokeWidth(3);
        paint.setColor(Color.rgb(255,255,255));
        paint.setTextSize(100);
        // paint.setShader(shader);//设置字体
        paint.setShadowLayer(5, 3, 3, 0xffffffff);// 设置阴影
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.STROKE); //空心
        paint.setDither(true);
        canvas.drawText(score+"",viewWidth/2,viewheight/2 - 80,paint);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.FILL); // 实心
        canvas.drawText("GAME OVER",viewWidth/2,viewheight/2,paint);
    }

    // ------ click ----------

    private int mDownX = 0;
    private int mDownY = 0;
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                if(mStatus == GameStatus.WAITING){
                    if(startButton.isClick(mDownX,mDownY)){
                        // click the start button
                        startButton.click();
                        //mp = MediaPlayer.create(this, R.raw.button);

                    }
                }else if(mStatus == GameStatus.RUNNING){
                    mbird.setBird_y( up_speed);
                    if (score > 10){
                        if(pSpeed < 16){
                            pSpeed += 2;
                        }
                    } else if (score > 20){
                        if(pSpeed < 24) {
                            pSpeed += 3;
                        }
                    } else if(score > 30){
                        pSpeed += 1;
                    }

                }else if(mStatus == GameStatus.OVER){
                    if(reButton.isClick(mDownX,mDownY)){
                            reButton.click();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (startButton != null){
                    startButton.setClick(false);
                }
                if (reButton != null){
                    reButton.setClick(false);
                }
                break;
        }
        return true;
    }


    //callback monitor
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isRunnging = true;
        mPool = Executors.newFixedThreadPool(5);
        mPool.execute(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isRunnging = false;
    }
}
