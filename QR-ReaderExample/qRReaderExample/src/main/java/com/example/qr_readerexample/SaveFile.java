package com.example.qr_readerexample;

import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiangao on 2/24/16.
 */
public class SaveFile {
    public static void saveFileOnSD(String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }

			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:ms");//设置日期格式
			String sFileName =df.format(new Date())+".txt";// new Date()为获取当前系统时间



            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }
}
