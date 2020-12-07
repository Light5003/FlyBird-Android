package com.example.flybird;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.le.ScanRecord;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.renderscript.ScriptC;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // 设计思路：通过handler发送消息确定事件，timer定时器移动柱子与小鸟
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取窗口
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.bg);
        //获取屏幕尺寸
        int Screen_height;
        int Screen_width;
        Screen_height = metrics.heightPixels;
        Screen_width = metrics.widthPixels;
        //获取游戏视图
        GameView gameView = new GameView(this,Screen_height,Screen_width);
        setContentView(gameView);
        Intent intent = new Intent(MainActivity.this, bgmusicService.class);
        String action = bgmusicService.ACTION_MUSIC;
        intent.setAction(action);
        startService(intent);

    }
}
