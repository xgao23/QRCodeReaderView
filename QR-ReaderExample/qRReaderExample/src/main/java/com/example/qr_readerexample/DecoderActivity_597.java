package com.example.qr_readerexample;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_readerexample.QRCodeReaderView_597.OnQRCodeReadListener_597;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class DecoderActivity_597 extends Activity implements OnQRCodeReadListener_597 {

    private TextView myTextView;
	private QRCodeReaderView_597 mydecoderview;
	private ImageView line_image;
	int nameCount=1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_decoder_597);
        
        mydecoderview = (QRCodeReaderView_597) findViewById(R.id.qrdecoderview_597);
        mydecoderview.setOnQRCodeReadListener(this);
        
        myTextView = (TextView) findViewById(R.id.exampleTextView_597);
        
        line_image = (ImageView) findViewById(R.id.red_line_image_597);
        
        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
       mAnimation.setDuration(1000);
       mAnimation.setRepeatCount(-1);
       mAnimation.setRepeatMode(Animation.REVERSE);
       mAnimation.setInterpolator(new LinearInterpolator());
       line_image.setAnimation(mAnimation);
        
    }

    
    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
	@Override
	public void onQRCodeRead(String text, PointF[] points) {
		String display=String.valueOf(nameCount);

		myTextView.setText(display);
		display+=String.valueOf(nameCount);
	//	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式



		generateNoteOnSD(text);


	}

	
	// Called when your device have no camera
	@Override
	public void cameraNotFound() {
		
	}

	// Called when there's no QR codes in the camera preview image
	@Override
	public void QRCodeNotFoundOnCamImage() {
		
	}
    
	@Override
	protected void onResume() {
		super.onResume();
		mydecoderview.getCameraManager().startPreview();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mydecoderview.getCameraManager().stopPreview();
	}

	public void generateNoteOnSD(String sBody){
		try
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Notes");
			if (!root.exists()) {
				root.mkdirs();
			}

/*			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:ms");//设置日期格式
			String sFileName =df.format(new Date())+".txt";// new Date()为获取当前系统时间*/

			String sFileName=nameCount++ + ".txt";

			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		}
		catch(IOException e)
		{
			e.printStackTrace();

		}
	}
}
