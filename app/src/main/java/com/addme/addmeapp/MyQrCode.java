package com.addme.addmeapp;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyQrCode extends AppCompatActivity {

    String TAG="GenerateQrCode";
    String inputValue;
    private EditText edttxt;
    private Button start;
    private ImageView qrimg;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);

        qrimg = (ImageView) findViewById(R.id.qrCode);
        edttxt = (EditText) findViewById(R.id.editText);

        start = (Button) findViewById(R.id.qrGenerateBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValue = edttxt.getText().toString().trim();
                if (inputValue.length() > 0){
                    WindowManager manager = (WindowManager)getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerdimension = width < height ? width:height;
                    smallerdimension = smallerdimension*3/4;
                    qrgEncoder = new QRGEncoder(inputValue,null, QRGContents.Type.TEXT,
                            smallerdimension);
                    try{
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrimg.setImageBitmap(bitmap);

                    }catch (WriterException e){
                        Log.v(TAG,e.toString());
                    }
                }else{
                    edttxt.setError("Required");
                }
            }
        });
    }
}
