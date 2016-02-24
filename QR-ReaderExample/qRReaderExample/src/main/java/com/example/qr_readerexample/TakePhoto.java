package com.example.qr_readerexample;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.client.android.camera.open.CameraManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DetectorResult;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.detector.Detector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends Activity {
    final int REQUEST_CAMERA=0;
    ImageView photoView,videoView,imageView;
    TextView text;
    private static final String TAG ="Photo";
    CameraManager mCameraManager;

    String mImageFileLocation=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCameraManager=new CameraManager(this);

        setContentView(R.layout.activity_take_photo);
        imageView=(ImageView)findViewById(R.id.imageView);
        photoView = (ImageView) findViewById(R.id.PhotoView);
        text =(TextView)findViewById(R.id.textView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        videoView=(ImageView)findViewById(R.id.videoView);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });
    }
    public void takeVideo(){
        Intent intent = new Intent(this, DecoderActivity_597.class);
        startActivity(intent);
    }
    public void takePhoto(){


        Intent intent= new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=null;
        try{
            photoFile=createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(intent, REQUEST_CAMERA);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0&&resultCode== Activity.RESULT_OK){
/*            Bitmap thumbnail=(Bitmap)data.getExtras().get("data");
            photo.setImageBitmap(thumbnail);*/
            Bitmap photoCapturedBitmap= BitmapFactory.decodeFile(mImageFileLocation);


            imageView.setImageBitmap(photoCapturedBitmap);
            String result=byteToString(photoCapturedBitmap);
            text.setText(result);






        }
        Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();

    }
    public String byteToString(Bitmap mBitmap){
        String result="Error";

        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        int[] pixels = new int[width * height];
        mBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        LuminanceSource source = new RGBLuminanceSource(width, height, pixels);


        HybridBinarizer hybBin = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(hybBin);
        try {
            BitMatrix bitMatrixAfterDetection = getResultAfterDectection_597(bitmap);
            result= bitMatrixAfterDetection.toString();
            SaveFile.saveFileOnSD(result);
            result="Saved";


        }catch (NotFoundException e) {
            // Notify QR not found
            Toast.makeText(getApplicationContext(), "NotFoundException", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "NotFoundException");
            e.printStackTrace();

        } catch (FormatException e) {
            Toast.makeText(getApplicationContext(), "FormatException", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "FormatException");
            e.printStackTrace();
        }
        return result;
    }

    public BitMatrix getResultAfterDectection_597(BinaryBitmap image) throws NotFoundException, FormatException {
        DetectorResult result = (new Detector(image.getBlackMatrix())).detect(null);
        BitMatrix bitMatrix=result.getBits();
        return  bitMatrix;

    }
    File createImageFile()throws IOException{
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="IMAGE"+timeStamp+"_";
        File storageDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDirectory);
        mImageFileLocation= image.getAbsolutePath();

        return image;
    }

}
