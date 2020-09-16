package com.example.bitmapcuttest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    private ImageView mOcrCutImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mOcrCutImage = (ImageView) findViewById(R.id.ocr_cut_image);

        Bitmap bitmap = BitmapUtil.getInstance().getImage();
        mOcrCutImage.setImageBitmap(bitmap);
        copyLanguageTrainedDataToSdCard();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                TessBaseAPI tessBaseAPI = new TessBaseAPI();
                tessBaseAPI.init(getExternalCacheDir().getAbsolutePath(),"chi_sim");
                tessBaseAPI.setImage(BitmapUtil.getInstance().getImage());
                tessBaseAPI.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
                String text = tessBaseAPI.getUTF8Text();
                Log.d(TAG, "text"+text);
            }
        });
        t.start();
    }

    private void copyLanguageTrainedDataToSdCard(){
        try {
            String filePath_parent = getExternalCacheDir().getAbsolutePath()+"/tessdata";
            File lvfile = new File(filePath_parent);
            if (!lvfile.exists()){
                Log.d(TAG, "copyLanguageTrainedDataToSdCard: 创建路径");
                if (lvfile.mkdirs()){
                    Log.d(TAG, "copyLanguageTrainedDataToSdCard: 创建成功");
                }else{
                    Log.d(TAG, "copyLanguageTrainedDataToSdCard: 创建失败");
                }
            }
            String filepath = getExternalCacheDir().getAbsolutePath()+"/tessdata/chi_sim.traineddata";
            lvfile = new File(filepath);
            InputStream lvInputStream ;
            OutputStream lvOutputStream = new FileOutputStream(lvfile);
            //拷贝文件
            lvInputStream = getAssets().open("chi_sim.traineddata");
            byte[] buffer = new byte[1024];
            int length = lvInputStream.read(buffer);
            while(length>0){
                lvOutputStream.write(buffer,0,length);
                length = lvInputStream.read(buffer);
            }
            lvOutputStream.flush();
            lvInputStream.close();
            lvOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}