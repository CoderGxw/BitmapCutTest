package com.example.bitmapcuttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private com.example.bitmapcuttest.ScreenShotView screenShotView;
    private Bitmap bmp;
    private Bitmap ocrBitmap;
    private TextView certainBtn;
    private TextView cancelBtn;
    private TextView restartBtn;
    private int screenWidth;
    private int screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenShotView = (ScreenShotView) findViewById(R.id.screenShotView);
        cancelBtn = (TextView) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
        certainBtn = (TextView) findViewById(R.id.certain_btn);
        certainBtn.setOnClickListener(this);
        restartBtn = (TextView)findViewById(R.id.restart_btn);
        restartBtn.setOnClickListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Resources r = this.getResources();

        bmp = BitmapFactory.decodeResource(r,R.drawable.test);
        screenShotView.setBitmap(bmp, screenHeight, screenWidth);
        screenShotView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                finish();
                break;
            case R.id.certain_btn:
                if (ocrBitmap != null) {
                    BitmapUtil.getInstance().setImageBitmap(ocrBitmap);
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"请选择截取区域",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.restart_btn:
                screenShotView.restart();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                screenShotView.setStartDot(new Dot(motionEvent.getX(), motionEvent.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                screenShotView.setEndDot(new Dot(motionEvent.getX(), motionEvent.getY()));
                screenShotView.setBitmap(bmp, screenHeight, screenWidth);
                break;
            case MotionEvent.ACTION_UP:
                ocrBitmap = screenShotView.getBitmap();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenShotView.restart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        screenShotView.restart();
    }
}