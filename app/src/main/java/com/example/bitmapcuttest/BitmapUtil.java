package com.example.bitmapcuttest;

import android.graphics.Bitmap;

public class BitmapUtil {

    private static Bitmap image;

    private BitmapUtil(){
    }

    private static BitmapUtil mInstantce;

    public  static BitmapUtil getInstance(){
        if (mInstantce == null){
            mInstantce = new BitmapUtil();
        }
        return mInstantce;
    }

    public void setImageBitmap(Bitmap bitmap){
        this.image = bitmap;
    }

    public Bitmap getImage() {
        return image;
    }
}
